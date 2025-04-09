from collections import defaultdict, deque

# Aceasta încarcă o gramatică dintr-un fișier text și o transformă într-un format utilizabil
def read_grammar(filename):
    grammar = defaultdict(list)  # Inițializează un dicționar unde fiecare cheie este un neterminal.
    with open(filename, 'r') as f:  # Deschide fișierul specificat.
        for line in f:  # Citește fiecare linie din fișier.
            left, right = line.strip().split("->")  # Împarte linia în partea stângă și dreaptă.
            left = left.strip()  # Elimină spațiile inutile din partea stângă.
            right = right.strip().replace("epsilon", "eps").replace('\"', '')
            productions = [prod.strip() for prod in right.split('|')]  # Împarte partea dreaptă în producții separate.
            for production in productions:  # Iterează prin producțiile din partea dreaptă.
                grammar[left].append(production.split())  # Adaugă producția la neterminalul corespunzător.
    return grammar  # Returnează gramatica sub formă de dicționar.


#Aceasta calculează mulțimile FIRST pentru fiecare neterminal și simbol din gramatică
def calculate_first(grammar):
    FIRST = defaultdict(set)  # Dicționar pentru mulțimile FIRST
    non_terminals = set(grammar.keys())  # Identificăm neterminalele
    terminals = set()  # Set pentru terminale

    # Identificăm toate terminalele din gramatică
    for productions in grammar.values():
        for production in productions:
            for symbol in production:
                if symbol not in non_terminals:  # Dacă simbolul nu e neterminal, este terminal
                    terminals.add(symbol)

    # Funcție internă pentru a calcula FIRST pentru un simbol
    def first_of(symbol):
        # Dacă simbolul este terminal, FIRST este simbolul însuși
        if symbol in terminals:
            return {symbol}
        # Dacă simbolul este neterminal și deja a fost calculat, îl returnăm
        if symbol in FIRST and FIRST[symbol]:
            return FIRST[symbol]

        # Calculăm FIRST pentru simbolul curent
        for production in grammar.get(symbol, []):  
            for sym in production:  # Parcurgem fiecare simbol din producție
                sym_first = first_of(sym)  # Calculăm FIRST pentru simbolul curent
                FIRST[symbol].update(sym_first - {"eps"})  # Adăugăm tot, fără epsilon
                # Dacă simbolul curent NU poate produce epsilon, ne oprim
                if "eps" not in sym_first:
                    break
            else:
                # Dacă toate simbolurile din producție produc epsilon, adăugăm epsilon
                FIRST[symbol].add("eps")
        return FIRST[symbol]

    # Calculăm FIRST pentru toate neterminalele
    for non_terminal in non_terminals:
        first_of(non_terminal)

    return FIRST  # Returnăm mulțimile FIRST



# Aceasta calculează mulțimile FOLLOW pentru fiecare neterminal din gramatică
def calculate_follow(grammar, FIRST):
    FOLLOW = defaultdict(set)  # Dicționar pentru FOLLOW.
    non_terminals = set(grammar.keys())  # Neterminale.
    terminals = set()  # Terminale.

    # Identifică terminalele din gramatică.
    for productions in grammar.values():
        for production in productions:
            for symbol in production:
                if symbol not in non_terminals:
                    terminals.add(symbol)

    start_symbol = next(iter(non_terminals))  # Simbolul de start este primul neterminal.
    FOLLOW[start_symbol].add('$')  # Simbolul de start are `$` în FOLLOW.

    updated = True  # Flag pentru a verifica modificările.
    while updated:
        updated = False
        for lhs, productions in grammar.items():  # Parcurge toate producțiile.
            for production in productions:
                follow_temp = FOLLOW[lhs].copy()  # FOLLOW-ul curent.
                for i in range(len(production) - 1, -1, -1):  # Iterează de la dreapta la stânga.
                    symbol = production[i]
                    if symbol in non_terminals:  # Dacă simbolul este un neterminal.
                        before_update = len(FOLLOW[symbol])
                        FOLLOW[symbol].update(follow_temp)  # Adaugă FOLLOW curent.
                        if "eps" in FIRST[symbol]:  # Dacă simbolul poate produce epsilon.
                            follow_temp = follow_temp.union(FIRST[symbol] - {"eps"})
                        else:
                            follow_temp = FIRST[symbol]
                        updated |= len(FOLLOW[symbol]) > before_update  # Actualizează flagul dacă FOLLOW s-a schimbat.
                    elif symbol in terminals:  # Dacă simbolul este un terminal.
                        follow_temp = {symbol}  # FOLLOW devine simbolul curent.
    return FOLLOW  # Returnează mulțimile FOLLOW.


def is_ll1_grammar(ll1_matrix, non_terminals, terminals):
    terminals = sorted(terminals)  # Sortează terminalele pentru navigare mai ușoară.
    conflicts = []  # Listă pentru conflicte.

    for row, non_terminal in enumerate(non_terminals):
        for col, terminal in enumerate(terminals):
            if len(ll1_matrix[row][col]) > 1:  # Dacă există mai multe producții într-o celulă.
                conflicts.append((non_terminal, terminal, ll1_matrix[row][col]))

    if conflicts:
        print("\nConflicte găsite în tabelul LL(1):")
        for non_terminal, terminal, productions in conflicts:
            print(f"Conflict la neterminalul '{non_terminal}' și terminalul '{terminal}': {productions}")
        return False
    else:
        print("\nGramatica este LL(1).")
        return True



# Aceasta construiește tabelul LL(1) utilizând mulțimile FIRST și FOLLOW
def construct_ll1_matrix(grammar, FIRST, FOLLOW):
    non_terminals = list(grammar.keys())  # Lista neterminalelor.
    terminals = set()  # Set pentru terminale.

    # Identifică terminalele.
    for productions in grammar.values():
        for production in productions:
            for symbol in production:
                if symbol not in non_terminals:
                    terminals.add(symbol)
    terminals.add('$')  # Adaugă simbolul de final.

    # Initializează matricea cu liste goale.
    ll1_matrix = [[[] for _ in range(len(terminals))] for _ in range(len(non_terminals))]
    terminal_index = {terminal: i for i, terminal in enumerate(sorted(terminals))}  # Index pentru terminale.
    non_terminal_index = {non_terminal: i for i, non_terminal in enumerate(non_terminals)}  # Index pentru neterminale.

    for lhs, productions in grammar.items():  # Parcurge gramatică.
        for production in productions:
            if production[0] in non_terminals:
                first_set = FIRST[production[0]] - {'eps'}  # FIRST fără epsilon.
            else:
                first_set = {production[0]}  # FIRST pentru terminale.

            for terminal in first_set:  # Adaugă în matrice pe baza FIRST.
                if terminal != 'eps':
                    row = non_terminal_index[lhs]
                    col = terminal_index[terminal]
                    if production not in ll1_matrix[row][col]:  # Verifică dacă producția nu este deja în listă.
                        ll1_matrix[row][col].append(production)

            if 'eps' in FIRST[lhs]:  # Adaugă pe baza FOLLOW dacă epsilon e în FIRST.
                for terminal in FOLLOW[lhs]:
                    row = non_terminal_index[lhs]
                    col = terminal_index[terminal]
                    if production not in ll1_matrix[row][col]:  # Verifică dacă eps nu este deja adăugat.
                        ll1_matrix[row][col].append(['eps'])

    return ll1_matrix, non_terminals, terminals  # Returnează matricea.


# Acestea parcurg secvența de intrare sau programul și utilizează tabelul LL(1) pentru a decide validitatea secvenței
def print_ll1_matrix(ll1_matrix, non_terminals, terminals):
    print("\nLL(1) Tabel:")
    terminals = sorted(terminals)

    # Determinăm lățimea maximă a fiecărei coloane
    col_widths = [max(len(terminal), 8) for terminal in terminals]
    col_widths.insert(0, max(len(nt) for nt in non_terminals))

    # Afișăm antetul tabelului
    header = "".join(f"{terminal:<{col_widths[j]}}" for j, terminal in enumerate([""] + terminals))
    print(header)
    print("-" * len(header))

    # Afișăm fiecare rând
    for i, non_terminal in enumerate(non_terminals):
        row = f"{non_terminal:<{col_widths[0]}}" + "".join(
            f"{( ' | '.join(' '.join(prod) for prod in ll1_matrix[i][j]) if ll1_matrix[i][j] else '-'):<{col_widths[j + 1]}}"
            for j in range(len(terminals))
        )
        print(row)

    

def parse_input_sequence(input_sequence, ll1_matrix, non_terminal_index, terminal_index, start_symbol):
    stack = deque(['$', start_symbol])  # Inițializare stivă cu simbolul de start și simbolul de final.
    input_sequence.append('$')  # Adăugăm simbolul de final la secvența de intrare.

    i = 0  # Pointer pentru secvența de intrare.
    step = 1  # Contor pentru pasul curent al procesării.

    # Header pentru tabelul de afișare
    print(f"{'Pas':<6} {'Stack':<30} {'Intrare':<30} {'Acțiune'}")
    print("-" * 80)

    while stack:
        stack_content = ' '.join(stack)  # Conținutul stivei ca string.
        input_remaining = ' '.join(input_sequence[i:])  # Simbolurile rămase de intrare.
        top = stack.pop()  # Scoatem vârful stivei.
        current_input = input_sequence[i]  # Simbolul curent din secvența de intrare.

        if top == current_input:  # Dacă simbolul din stivă coincide cu simbolul curent de intrare.
            action = f"Pop: {top}"
            print(f"{step:<6} {stack_content:<30} {input_remaining:<30} {action}")
            if top == '$':  # Dacă simbolul curent este simbolul final, terminăm.
                return "Secvența este validă."
            i += 1  # Avansăm pointerul în secvența de intrare.
        elif top.isupper():  # Dacă simbolul din stivă este un neterminal.
            row = non_terminal_index[top]  # Rândul în matricea LL(1).
            col = terminal_index[current_input]  # Coloana în matricea LL(1).
            if ll1_matrix[row][col]:  # Verificăm dacă există producții în această celulă.
                production = ll1_matrix[row][col][0]  # Selectăm prima producție din celulă.
                action = f"Productia: {top} -> {' '.join(production)} Push: {' '.join(production)}"  
                print(f"{step:<6} {stack_content:<30} {input_remaining:<30} {action}")
                for symbol in reversed(production):  # Adăugăm simbolurile producției în stivă.
                    if symbol != 'eps':  # Ignorăm epsilon.
                        stack.append(symbol)
            else:  # Dacă nu există producție, afișăm eroarea.
                action = f"Error: No production for {top} with {current_input}"
                print(f"{step:<6} {stack_content:<30} {input_remaining:<30} {action}")
                return f"Eroare: Nu există producție pentru {top} cu simbolul {current_input}."
        else:  # Dacă simbolul din stivă este un terminal care nu se potrivește cu intrarea.
            action = f"Error: Unexpected symbol {current_input}"
            print(f"{step:<6} {stack_content:<30} {input_remaining:<30} {action}")
            return f"Eroare: Secvența nu este validă pentru simbolul {current_input}."

        step += 1  # Incrementăm contorul de pas.

    # Dacă stiva nu s-a golit complet, returnăm o eroare.
    return "Eroare: Secvența nu a fost procesată complet."



def parse_input_program(program, ll1_matrix, non_terminal_index, terminal_index, start_symbol):
    stack = deque(['$', start_symbol])  # Inițializare stivă cu simbolul de start și simbolul de final.
    program.append('$')  # Adăugăm simbolul de final la program.

    i = 0  # Pointer pentru programul de intrare.
    step = 1  # Contor pentru pasul curent al procesării.

    while stack:
        print("\n--------------------------------------")
        print(f"Pasul {step}")
        print("--------------------------------------")
        stack_content = ' '.join(stack)  # Conținutul stivei ca string.
        input_remaining = ' '.join(program[i:])  # Simbolurile rămase de procesat.
        top = stack.pop()  # Scoatem vârful stivei.
        current_input = program[i]  # Simbolul curent din program.

        print(f"Stack:   {stack_content}")
        print(f"Intrare: {input_remaining}")

        if top in terminal_index:  # Dacă simbolul din stivă este un terminal.
            if top == current_input:  # Dacă se potrivește cu simbolul curent din intrare.
                print(f"Acțiune: Pop {top}")
                i += 1  # Avansăm pointerul.
            else:  # Dacă simbolurile nu se potrivesc.
                print(f"Acțiune: Error: Expected {top}, but found {current_input}")
                return f"Eroare: Așteptat {top}, dar găsit {current_input}."
        elif top in non_terminal_index:  # Dacă simbolul din stivă este un neterminal.
            row = non_terminal_index[top]  # Rândul în matricea LL(1).
            col = terminal_index.get(current_input, -1)  # Coloana în matricea LL(1).
            if col == -1:  # Dacă simbolul curent nu este un terminal valid.
                print(f"Acțiune: Error: Invalid terminal {current_input}")
                return f"Eroare: Simbolul {current_input} nu este un terminal valid."
            if ll1_matrix[row][col]:  # Verificăm dacă există producții în această celulă.
                production = ll1_matrix[row][col][0]  # Selectăm prima producție din celulă.
                action = f"Productia: {top} -> {' '.join(production)} Push: {' '.join(production)}"  # Transformăm producția într-un string pentru afișare.
                print(f"{step:<6} {stack_content:<30} {input_remaining:<30} {action}")
                for symbol in reversed(production):  # Adăugăm simbolurile producției în stivă.
                    if symbol != 'eps':  # Ignorăm epsilon.
                        stack.append(symbol)
            else:  # Dacă nu există producție, afișăm eroarea.
                action = f"Error: No production for {top} with {current_input}"
                print(f"{step:<6} {stack_content:<30} {input_remaining:<30} {action}")
                return f"Eroare: Nu există producție pentru {top} cu simbolul {current_input}."
        else:  # Dacă simbolul din stivă nu este nici terminal, nici neterminal.
            print(f"Acțiune: Error: Unknown symbol {top}")
            return f"Eroare: Simbolul {top} nu este nici terminal, nici neterminal."

        step += 1  # Incrementăm contorul de pas.

    # Dacă stiva și intrarea s-au procesat complet, programul este valid.
    if list(stack) == program[i:]:
        return "Programul este valid."
    else:  # Dacă rămân simboluri în stivă sau intrare.
        return "Eroare: Programul nu a fost procesat complet."


def main():
    while True:
        print("\nMeniu:")
        print("1. Analiza secvenței de intrare")
        print("2. Analiza unui program scris în minilimbajul specificat")
        print("3. Ieșire")
        choice = input("Alegeți o opțiune: ")

        if choice == '1':
            grammar = read_grammar("gramatica.txt")  # Citim gramatica din fișier.
            FIRST = calculate_first(grammar)
            FOLLOW = calculate_follow(grammar, FIRST)
            ll1_matrix, non_terminals, terminals = construct_ll1_matrix(grammar, FIRST, FOLLOW)

            print("\nFIRST sets:")
            for non_terminal, first_set in FIRST.items():
                print(f"{non_terminal}: {first_set}")
            print("----------------------------------------------------------------------")
            print("\nFOLLOW sets:")
            for non_terminal, follow_set in FOLLOW.items():
                print(f"{non_terminal}: {follow_set}")
            print("----------------------------------------------------------------------")


            if not is_ll1_grammar(ll1_matrix, non_terminals, terminals):
                print("Gramatica nu este LL(1).")
            else:
                print("Gramatica este LL(1).")
                print_ll1_matrix(ll1_matrix, non_terminals, sorted(terminals))

                print("\n")

                input_sequence = list(input("Introduceți secvența de intrare: "))
                non_terminal_index = {non_terminal: i for i, non_terminal in enumerate(non_terminals)}
                terminal_index = {terminal: i for i, terminal in enumerate(sorted(terminals))}
                result = parse_input_sequence(input_sequence, ll1_matrix, non_terminal_index, terminal_index, 'S')
                print(result)

        

        elif choice == '2':
            grammar = read_grammar("minilimbaj.txt")  # Citim gramatica pentru minilimbaj din fișier.
            FIRST = calculate_first(grammar)
            FOLLOW = calculate_follow(grammar, FIRST)
            ll1_matrix, non_terminals, terminals = construct_ll1_matrix(grammar, FIRST, FOLLOW)

            if not is_ll1_grammar(ll1_matrix, non_terminals, terminals):
                print("Gramatica nu este LL(1).")
            else:
                print("\nFIRST sets:")
                for non_terminal, first_set in FIRST.items():
                    print(f"{non_terminal}: {first_set}")
                print("----------------------------------------------------------------------")
                print("\nFOLLOW sets:")
                for non_terminal, follow_set in FOLLOW.items():
                    print(f"{non_terminal}: {follow_set}")
                print("----------------------------------------------------------------------")

                
                ## neacc FIP
                # program = [
                #     "int", "main", "(", ")", "{",
                #     "int", "ID", "=", "CONST", ";",
                #     "while", "(", "ID", "==", "CONST", ")", "{",
                #     "if", "(", "ID", ">", "ID", ")", "{", "ID", "=", "ID", "-", "ID", ";", "}",
                #     "}", "else",
                #     "return", "CONST", ";",
                #     "}"
                # ]
                
                #acc FIP
                program = [
                    "int", "main", "(", ")", "{",
                    "int", "ID", "=", "CONST", ";",
                    "while", "(", "ID", "==", "CONST", ")", "{",
                    "if", "(", "ID", ">", "ID", ")", "{", "ID", "=", "ID", "-", "ID", ";", "}",
                    "}", 
                    "return", "CONST", ";",
                    "}"
                ]

                non_terminal_index = {non_terminal: i for i, non_terminal in enumerate(non_terminals)}
                terminal_index = {terminal: i for i, terminal in enumerate(sorted(terminals))}
                result = parse_input_program(program, ll1_matrix, non_terminal_index, terminal_index, 'program')
                print(result)

        elif choice == '3':
            break

        else:
            print("Opțiune invalidă. Încercați din nou.")


if __name__ == "__main__":
    main()