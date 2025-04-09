def parsare(gramatica):
    non_terminale = set()
    terminale = set()
    productii = set()
    simbol_start = None
    
    for rule in gramatica:
        productii.add(rule)
        head, productions = rule.split("->")
        head = head.strip()
        
        if simbol_start is None:
            simbol_start = head
        
        non_terminale.add(head)
        
        for production in productions.split('|'):
            production = production.strip()
            
            if production == 'epsilon':
                production = 'ε'
            
            for symbol in production:
                if not symbol.isupper() and symbol != 'ε':
                    terminale.add(symbol)
                elif symbol.isupper() :
                    non_terminale.add(symbol)
    
    return productii,non_terminale, terminale, simbol_start

def main():
    with open('gramatica.txt', 'r') as file:
        gramatica = [line.strip() for line in file.readlines()]
    
    
    productii,non_terminale, terminale, simbol_start = parsare(gramatica)
    
    print("Simbol start:", simbol_start)
    print("Non terminale:", non_terminale)
    print("Terminale:", terminale)
    print("Productii:", productii)

if __name__ == "__main__":
    main()