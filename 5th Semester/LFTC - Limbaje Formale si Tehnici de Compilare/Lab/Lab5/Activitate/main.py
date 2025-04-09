def is_productive(grammar,starter):
    productive = set()
    all_non_terminals = set(grammar.keys())
    all_symbols = set()  

    productive.add(starter)
    for non_terminal, rules in grammar.items():
        for rule in rules:
            all_symbols.update(rule)  
            if len(rule) == 1 and rule.islower():
                productive.add(non_terminal)
                break
    
    changed = True
    while changed:
        changed = False
        for non_terminal, rules in grammar.items():
            if non_terminal not in productive:
                for rule in rules:
                    if all(symbol in productive or symbol.islower() for symbol in rule):
                        productive.add(non_terminal)
                        changed = True
                        break
    
    non_productive = all_non_terminals - productive
    
    for symbol in all_symbols:
        if symbol.isupper() and symbol not in grammar.keys():
            non_productive.add(symbol)
    
    return productive, non_productive


def read_grammar_from_file(filename):
    grammar = {}  
    
    with open(filename, 'r') as file:
        for line in file:
            line = line.strip()  
            if "->" in line:
                non_terminal, rules = line.split("->")
                
                non_terminal = non_terminal.strip()  
                rule_list = [rule.strip() for rule in rules.split("|")]
                
                if non_terminal in grammar:
                    grammar[non_terminal].extend(rule_list)
                else:
                    grammar[non_terminal] = rule_list
                    
        print("Gramatica citită din fișier:")
        print(grammar)
    return grammar





filename = "grammar.txt"
grammar = read_grammar_from_file(filename)

starter = next(iter(grammar))
productive_symbols, non_productive_symbols = is_productive(grammar, starter)

print("Productive:")
for symbol in sorted(productive_symbols):
    print(f"  - {symbol}")

print("\nNeproductive:")
for symbol in sorted(non_productive_symbols):
    print(f"  - {symbol}")