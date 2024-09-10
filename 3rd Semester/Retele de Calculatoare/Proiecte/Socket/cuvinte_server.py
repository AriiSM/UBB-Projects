import socket
import struct
import random
import threading

def handle_client(c, id):
    global cuv, chr, len_cuv, gresit

    print(f"CLIENTUL {id} S-A CONECTAT!")

    while not gresit:
        if len_cuv == 0:
            print("\nINCEPE JOCUL!\n")

            chr = chr(random.randint(ord('a'), ord('z')))
            print(f"\nLITERA DE TRIMIS E: {chr}")

            cuv = " "
            len_cuv = 1
            len_cuv_bytes = struct.pack('!I', len_cuv)
            c.send(len_cuv_bytes)
            c.send(cuv.encode('utf-8'))
            c.send(chr.encode('utf-8'))

            len_cuv_recv_bytes = c.recv(4)
            len_cuv_recv = struct.unpack('!I', len_cuv_recv_bytes)[0]
            cuv_recv = c.recv(len_cuv_recv).decode('utf-8')
            chr_recv = c.recv(1).decode('utf-8')

            print(f"\nAM PRIMIT CUVANTUL: {cuv_recv}        SI LITERA: {chr_recv}\n")

        else:
            len_cuv_bytes = struct.pack('!I', len_cuv)
            c.send(len_cuv_bytes)
            c.send(cuv.encode('utf-8'))
            c.send(chr.encode('utf-8'))

            aux = chr

            len_cuv_recv_bytes = c.recv(4)
            len_cuv_recv = struct.unpack('!I', len_cuv_recv_bytes)[0]
            cuv_recv = c.recv(len_cuv_recv).decode('utf-8')
            chr_recv = c.recv(1).decode('utf-8')

            print(f"\nAM PRIMIT CUVANTUL: {cuv_recv}        SI LITERA: {chr_recv}\n")

            # VEZI DACA A PIERDUT
            are_litera = 0
            for i in range(len(cuv_recv)):
                if cuv_recv[i] == aux:
                    are_litera = 1

            if are_litera == 0:
                gresit = 1
                pierzator = "PIERZATOR"
                castigator = "CASTIGATOR"
                len_pierzator = len(pierzator) + 1
                len_castigator = len(castigator) + 1

                len_pierzator_bytes = struct.pack('!I', len_pierzator)
                c.send(len_pierzator_bytes)
                c.send(pierzator.encode('utf-8'))

                len_castigator_bytes = struct.pack('!I', len_castigator)
                c.send(len_castigator_bytes)
                c.send(castigator.encode('utf-8'))

            len_cuv = 0

    c.close()

# Crearea unui socket server
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Fac bind
server_addr = ('127.0.0.1', 3083)
s.bind(server_addr)

# Fac listen, aici devine serverul server
s.listen(5)

c1, _ = s.accept()
c2, _ = s.accept()

print("S-AU CONECTAT CLIENTII!")

cuv, chr, len_cuv, gresit = 0, "", 0, 0

# Server concurent cu threaduri
thread1 = threading.Thread(target=handle_client, args=(c1, 1))
thread2 = threading.Thread(target=handle_client, args=(c2, 2))

thread1.start()
thread2.start()

thread1.join()
thread2.join()

s.close()
