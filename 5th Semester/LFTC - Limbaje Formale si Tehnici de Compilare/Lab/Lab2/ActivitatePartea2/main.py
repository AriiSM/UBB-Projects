from AutomatFinitDeterminist import AutomatFinitDeterminist


def main():
    automat = AutomatFinitDeterminist()
    automat.stare_initiala = 'q0'

    while True:
        print("\n--- Meniu Automat Finit Determinist ---")
        print("1. Citire automat din fisier")
        print("2. Citire automat de la tastatura")
        print("3. Afisare elemente automat")
        print("4. Verificare secventa")
        print("5. Gasire cel mai lung prefix acceptat")
        print("0. Iesire")

        optiune = input("Alegeti o optiune: ")

        if optiune == '1':
            nume_fisier = input("Introduceti numele fisierului: ")
            automat.citire_din_fisier(nume_fisier)
            print("Automatul a fost incarcat din fisier.")
            automat.citire_din_fisier(nume_fisier)
            if not automat.este_determinist():
                opt=input("Doriti sa afisati elementele? ")
                if opt=='da':
                    automat.afisare_elemente()
                else:
                    print("La revedere!")
                    break

        elif optiune == '2':
            automat.citire_din_tastatura()
            print("Automatul a fost configurat de la tastatura.")

        elif optiune == '3':
            automat.afisare_elemente()

        elif optiune == '4':
            secventa = input("Introduceți secvența de verificat: ")
            if automat.verifica_secventa(secventa):
                print("Secventa este acceptata de automat.")
            else:
                print("Secventa nu este acceptata de automat.")

        elif optiune == '5':
            secventa = input("Introduceti secventa pentru a gasi prefixul acceptat: ")
            prefix = automat.cel_mai_lung_prefix(secventa)
            print("Cel mai lung prefix acceptat este:", prefix)

        elif optiune == '0':
            print("La revedere!")
            break

        else:
            print("Opțiune invalida!")

if __name__ == "__main__":
    main()
