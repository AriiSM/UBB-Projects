import bisect

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
                for line in lines:
                    if line.startswith("initial_state:"):
                        self.stare_initiala = line.split(':')[1].strip()
                    
                    elif line.startswith("states:"):
                        self.stari = set(line.split(':')[1].strip().split(','))
                    
                    elif line.startswith("alphabet:"):
                        self.alfabet = set(line.split(':')[1].strip().split(','))
                    
                    elif line.startswith("transitions:"):
                        tranzitii_raw = line.split(':')[1].strip().split(';')
                        for tranzitie in tranzitii_raw:
                            src, simbol, dest = tranzitie.split('->')
                            src, simbol, dest = src.strip(), simbol.strip(), dest.strip()
                            self.tranzitii[(src, simbol)] = dest
                    
                    elif line.startswith("final_states:"):
                        self.stari_finale = set(line.split(':')[1].strip().split(','))
                        
        except FileNotFoundError:
            print("Fisierul nu a fost gasit :(")

    def verifica_secventa(self, secventa):
        stare_curenta = self.stare_initiala
        for simbol in secventa:
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
            if longest_prefix == "" and self.stare_initiala in self.stari_finale:
                longest_prefix = "\u03B5"
        return longest_prefix

class AnalizorLexical:
    def __init__(self):
        self.automat_identificator = AutomatFinitDeterminist()
        self.automat_identificator.citire_din_fisier("Automate/Automat_Cuvant.txt")
        self.automat_constanta_int = AutomatFinitDeterminist()
        self.automat_constanta_int.citire_din_fisier("Automate/Automat_Intregi.txt")
        self.automat_constanta_real = AutomatFinitDeterminist()
        self.automat_constanta_real.citire_din_fisier("Automate/Automat_Reale.txt")

        self.ts = []
        self.fip = []
        self.next_index = 1

    coduri = {
        "if": 1, "else": 2, "while": 3, "+": 4, "-": 5, "*": 6, "=": 7, "==": 8,
        "!=": 9, "<=": 10, ">=": 11, "<": 12, ">": 13, "(": 14, ")": 15,
        "{": 16, "}": 17, ";": 18, "<<": 19, ">>": 20, "int": 21, "cin": 22,
        "cout": 23, "endl": 24
    }
    sorted_coduri = sorted(coduri.keys(), key=len, reverse=True)

    def adauga_in_ts(self, atom):
        pozitie = bisect.bisect_left([sym for _, sym in self.ts], atom)
        if pozitie < len(self.ts) and self.ts[pozitie][1] == atom:
            return self.ts[pozitie][0]
        self.ts.insert(pozitie, (self.next_index, atom))
        self.next_index += 1
        return self.next_index - 1

    def parse(self, fisier_intrare):
        linie_nr = 0
        with open(fisier_intrare, 'r') as fisier:
            for linie in fisier:
                linie_nr += 1
                pozitie = 0
                while pozitie < len(linie):
                    simbol_gasit = None
                    for simbol in self.sorted_coduri:
                        if linie[pozitie:].startswith(simbol):
                            simbol_gasit = simbol
                            break

                    if simbol_gasit:
                        self.fip.append((self.coduri[simbol_gasit], "-"))
                        pozitie += len(simbol_gasit)
                    elif self.automat_identificator.cel_mai_lung_prefix(linie[pozitie:]) != "":
                        prefix = self.automat_identificator.cel_mai_lung_prefix(linie[pozitie:])
                        index = self.adauga_in_ts(prefix)
                        self.fip.append((0, index))
                        pozitie += len(prefix)
                    elif self.automat_constanta_real.cel_mai_lung_prefix(linie[pozitie:]) != "":
                        prefix = self.automat_constanta_real.cel_mai_lung_prefix(linie[pozitie:])
                        index = self.adauga_in_ts(prefix)
                        self.fip.append((0, index))
                        pozitie += len(prefix)
                    elif self.automat_constanta_int.cel_mai_lung_prefix(linie[pozitie:]) != "":
                        prefix = self.automat_constanta_int.cel_mai_lung_prefix(linie[pozitie:])
                        index = self.adauga_in_ts(prefix)
                        self.fip.append((0, index))
                        pozitie += len(prefix)
                    else:
                        if linie[pozitie] in [" ", "\n"]:
                            pozitie += 1
                        else:
                            print(f"Eroare lexicala la linia {linie_nr}: caracterul '{linie[pozitie]}' necunoscut.")
                            pozitie += 1

    def salvare_fisiere(self):
       # Salvare FIP în format tabelar
        with open("FIP.txt", "w") as fip_file:
            fip_file.write(f"{'Cod':<10}{'Index':<10}\n")
            fip_file.write("=" * 20 + "\n")
            for cod, index in self.fip:
                fip_file.write(f"{cod:<10}{index:<10}\n")

        # Salvare TS în format tabelar
        with open("TS.txt", "w") as ts_file:
            ts_file.write(f"{'Index':<10}{'Simbol':<20}\n")
            ts_file.write("=" * 30 + "\n")
            for index, simbol in self.ts:
                ts_file.write(f"{index:<10}{simbol:<20}\n")

analizor = AnalizorLexical()
analizor.parse("cod.txt")
analizor.salvare_fisiere()
