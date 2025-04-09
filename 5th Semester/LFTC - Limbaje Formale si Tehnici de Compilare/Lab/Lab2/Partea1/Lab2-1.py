class Automaton:
    def __init__(self):
        self.states = set()
        self.alphabet = set()
        self.transitions = {}
        self.final_states = set()
        self.initial_state = None
        print("\033[92mAutomaton initialized\033[0m")

    def read_from_file(self, filename):
        with open(filename, 'r') as file:
            self.initial_state = file.readline().strip().split(":")[1]
            self.states = set(file.readline().strip().split(":")[1].split(","))
            self.alphabet = set(file.readline().strip().split(":")[1].split(","))
            transitions = file.readline().strip().split(":")[1].split(";")
            for transition in transitions:
                parts = transition.split("->")
                if parts[0] not in self.transitions:
                    self.transitions[parts[0]] = {}
                self.transitions[parts[0]][parts[1]] = parts[2]
            self.final_states = set(file.readline().strip().split(":")[1].split(","))
        print(f"\033[92mAutomaton read from file: {filename}\033[0m")

    def read_from_input(self):
        self.initial_state = input("Enter initial state: ")
        self.states = set(input("Enter states (comma separated): ").split(","))
        self.alphabet = set(input("Enter alphabet (comma separated): ").split(","))
        transitions = input("Enter transitions (format: state->symbol->state;...): ").split(";")
        for transition in transitions:
            parts = transition.split("->")
            if parts[0] not in self.transitions:
                self.transitions[parts[0]] = {}
            self.transitions[parts[0]][parts[1]] = parts[2]
        self.final_states = set(input("Enter final states (comma separated): ").split(","))
        print("\033[92mAutomaton read from input\033[0m")

    def display_elements(self):
        print("\033[94mDisplaying automaton elements:\033[0m")
        print("\033[94mInitial State:\033[0m", self.initial_state)
        print("\033[94mStates:\033[0m", self.states)
        print("\033[94mAlphabet:\033[0m", self.alphabet)
        print("\033[94mTransitions:\033[0m", self.transitions)
        print("\033[94mFinal States:\033[0m", self.final_states)

    def este_determinist(self):    
        for (stare, simbol) in self.transitions.items():
            destinatii_duplicate = [dest for (s, sym), dest in self.transitions.items() if s == stare and sym == simbol]
            if len(destinatii_duplicate) > 1:
                return False    
        return True


    def is_accepted(self, sequence):
        print(f"\033[93mChecking if sequence is accepted: {sequence}\033[0m")
        current_state = self.initial_state
        print(f"\033[93mInitial state: {current_state}\033[0m")
        for symbol in sequence:
            if symbol not in self.alphabet:
                print(f"\033[91mSequence not accepted (invalid symbol: {symbol})\033[0m")
                return False
            if symbol in self.transitions[current_state]:
                current_state = self.transitions[current_state][symbol]
                print(f"\033[93mTransitioned to state: {current_state}\033[0m")
            else:
                print(f"\033[91mSequence not accepted (no transition for symbol: {symbol} in state: {current_state})\033[0m")
                return False
        if current_state in self.final_states:
            print("\033[92mSequence accepted\033[0m")
            return True
        else:
            print("\033[91mSequence not accepted (final state not reached)\033[0m")
            return False

    def longest_prefix(self, sequence):
        print(f"\033[93mFinding longest accepted prefix for sequence: {sequence}\033[0m")
        current_state = self.initial_state
        longest_prefix = ""
        current_prefix = ""
        
        initial_is_final = current_state in self.final_states

        for symbol in sequence:
            if symbol not in self.alphabet:
                print(f"\033[91mInvalid symbol encountered: {symbol}\033[0m")
                break
            if symbol in self.transitions[current_state]:
                current_state = self.transitions[current_state][symbol]
                current_prefix += symbol
                print(f"\033[93mTransitioned to state: {current_state}\033[0m")
                if current_state in self.final_states:
                    longest_prefix = current_prefix
                    print(f"\033[92mCurrent longest accepted prefix: {longest_prefix}\033[0m")
            else:
                print(f"\033[91mNo transition for symbol: {symbol} in state: {current_state}\033[0m")
                break

       
        if longest_prefix == "" and initial_is_final:
            longest_prefix = "ε"
            print(f"\033[93mLongest accepted prefix: {longest_prefix}\033[0m")
        elif longest_prefix != "":
            print(f"\033[93mLongest accepted prefix: {longest_prefix}\033[0m")
        
        return longest_prefix



def main():
    automaton = Automaton()
    while True:
        print("\n\033[96mMenu:\033[0m")
        print("\033[96m1. Read automaton from file\033[0m")
        print("\033[96m2. Read automaton from input\033[0m")
        print("\033[96m3. Display automaton elements\033[0m")
        print("\033[96m4. Check if sequence is accepted\033[0m")
        print("\033[96m5. Find longest accepted prefix\033[0m")
        print("\033[96m6. Exit\033[0m")
        choice = input("\033[96mEnter your choice: \033[0m")
        if choice == '1':
            filename = input("\033[96mEnter filename: \033[0m")
            automaton.read_from_file(filename)
            if not automaton.este_determinist():
                print("\033[91mAutomatul nu este determinist\033[0m")
                o = input("Doriti sa afisati elementele? (da/nu) ")
                if o.lower() == 'da':
                    automaton.display_elements()
                else:
                    print("\033[91mExiting program\033[0m")
                    break
        elif choice == '2':
            automaton.read_from_input()
        elif choice == '3':
            automaton.display_elements()
        elif choice == '4':
            sequence = input("\033[96mEnter sequence: \033[0m")
            automaton.is_accepted(sequence)
        elif choice == '5':
            sequence = input("\033[96mEnter sequence: \033[0m")
            automaton.longest_prefix(sequence)
        elif choice == '6':
            print("\033[91mExiting program\033[0m")
            break

if __name__ == "__main__":
    main()