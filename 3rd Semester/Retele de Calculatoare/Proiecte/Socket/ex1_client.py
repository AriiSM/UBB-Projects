import socket
import time
TCP_IP = "0.0.0.0"
TCP_PORT = 12345
while True:
    MESSAGE = input("Dati un sir de caractere: ")
    PORT = input("Dati si portul: ")
    de_trimis = MESSAGE + "," + PORT

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((TCP_IP, TCP_PORT))
    s.send(de_trimis.encode("utf-8"))
    print("Am trimis la server: ", de_trimis)
    s.close()


    data_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    data_socket.bind((TCP_IP,int(PORT)))
    data_socket.listen(5)
    client, addr = data_socket.accept()
    print("Accepted connection from " + str(addr[0]) + " " + str(addr[1]))

    print("Aici nu mai vrea")
    data = client.recv(1024).decode()
    print("Data", data)
    print(f"Am primit de la server pe portul {PORT}: {data}")

    data_socket.close()


