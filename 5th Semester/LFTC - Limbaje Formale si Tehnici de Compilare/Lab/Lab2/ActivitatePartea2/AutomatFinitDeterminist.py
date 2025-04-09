class AutomatFinitDeterminist:
    def __init__(self):
        self.stari = set()
        self.alfabet = set()
        self.tranzitii = {}
        self.stare_initiala = "q0"
        self.stari_finale = set()

    def este_determinist(self):

        for (stare, simbol), destinatie in self.tranzitii.items():
            if len([dest for (s, sym), dest in self.tranzitii.items() if s == stare and sym == simbol]) > 1:
                return False
        return True

    def citire_din_fisier(self, nume_fisier):
        try:
            with open(nume_fisier, 'r') as file:
                lines = file.readlines()

                for i, line in enumerate(lines):
                    if line.startswith("Stari:"):
                        self.stari = set(line.split('{')[1].split('}')[0].split(','))

                    elif line.startswith("Alfabet:"):
                        self.alfabet = set(line.split('{')[1].split('}')[0].split(','))

                    elif line.startswith("Tranzitii:"):
                        j = i + 1
                        while j < len(lines) and '->' in lines[j]:
                            tranzitie = lines[j].strip()
                            src, rest = tranzitie.split(',')
                            simbol, dest = rest.split("->")
                            self.tranzitii[(src.strip(), simbol.strip())] = dest.strip()
                            j += 1

                    elif line.startswith("StariFinale:"):
                        self.stari_finale = set(line.split('{')[1].split('}')[0].split(','))
        except FileNotFoundError:
            print(f"Fisierul nu a fost gasit :(")

    def citire_din_tastatura(self):
        self.stari = set(input("Introduceti starile (separate prin virgule): ").split(","))
        self.alfabet = set(input("Introduceti alfabetul (separate prin virgule): ").split(","))

        print(
            "Introduceti tranzitiile in formatul 'stare,simbol->stare destinatie' (introduceti 'stop' pentru a termina):")
        while True:
            tranzitie = input()
            if tranzitie.lower() == 'stop':
                break
            src, rest = tranzitie.split(',')
            simbol, dest = rest.split("->")
            self.tranzitii[(src.strip(), simbol.strip())] = dest.strip()

        self.stari_finale = set(input("Introduceti starile finale (separate prin virgule): ").split(","))

    def afisare_elemente(self):
        print("Multimea starilor:", self.stari)
        print("Alfabetul:", self.alfabet)
        print("Tranzitiile:")
        for (src, simbol), dest in self.tranzitii.items():
            print(f"{src},{simbol}->{dest}")
        print("Starile finale:", self.stari_finale)

    def verifica_secventa(self, secventa):
        stare_curenta = self.stare_initiala
        for simbol in secventa:
            #if (simbol != 'x') and (simbol != '-'):
                #simbol=simbol.upper()
            if (stare_curenta, simbol) not in self.tranzitii:
                return False
            stare_curenta = self.tranzitii[(stare_curenta, simbol)]
        return stare_curenta in self.stari_finale

    def cel_mai_lung_prefix(self, secventa):
        stare_curenta = self.stare_initiala
        prefix_acceptat = ""
        longest_prefix = ""

        for simbol in secventa:
            if (stare_curenta, simbol) in self.tranzitii:
                stare_curenta = self.tranzitii[(stare_curenta, simbol)]
                prefix_acceptat += simbol
                if stare_curenta in self.stari_finale:
                    longest_prefix = prefix_acceptat
            else:
                break
            if (longest_prefix=="") and (self.stare_initiala in self.stari_finale):
                longest_prefix="epsilon"
        return longest_prefix
