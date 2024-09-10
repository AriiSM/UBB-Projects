# Enunt: Clientul trimite serverului doua numere. Serverul le primeste, le afiseaza pe ecran si trimite clientului suma lor.
#
# Rulare in doua terminale diferite:
#	python server.py
#	python client.py
import socket

TCP_IP = "0.0.0.0"
TCP_PORT = 12345
while True:
    MESSAGE = input("Dati un sir de caractere: ")
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((TCP_IP, TCP_PORT))
    s.send(MESSAGE.encode("utf-8"))
    print("Am trimis la server: ", MESSAGE)
    data = s.recv(1024).decode()

    print("Am primit de la server: ", data)
    s.close()

