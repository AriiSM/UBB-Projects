import socket
import struct
import random
import time

# Crearea unui socket client
c = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Conectarea la server
server_addr = ('127.0.0.1', 3083)
c.connect(server_addr)

while True:
    # Primesc lungimea cuvantului și apoi cuvantul de la server
    len_bytes = c.recv(4)
    len_cuv = struct.unpack('!I', len_bytes)[0]
    cuv = c.recv(len_cuv).decode('utf-8')

    # Verific dacă cuvântul este "PIERZATOR" sau "CASTIGATOR"
    if cuv == "PIERZATOR" or cuv == "CASTIGATOR":
        print(cuv)
        break

    # Primesc o literă de la server
    litera = c.recv(1).decode('utf-8')

    print(f"AM PRIMIT CUVANTUL {cuv}   SI LITERA: {litera}\n")

    # Cere utilizatorului să introducă un cuvânt și generează o literă aleatoare
    cuv_nou = input(f"DATI UN CUVANT CU LITERA {litera}: ")
    chr_generated = chr(random.randint(ord('a'), ord('z')))
    print(f"AM GENERAT: {chr_generated}")

    # Trimite informațiile despre noul cuvânt și litera către server
    len_cuv_nou = len(cuv_nou)
    len_cuv_nou_bytes = struct.pack('!I', len_cuv_nou)
    c.send(len_cuv_nou_bytes)
    c.send(cuv_nou.encode('utf-8'))
    c.send(chr_generated.encode('utf-8'))

# Închide conexiunea
c.close()
