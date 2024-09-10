import socket

TCP_IP ="127.0.0.1"
TCP_PORT = 8888

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((TCP_IP, TCP_PORT))
server_socket.listen(1)

while 1:
    conn, addr = server_socket.accept()
    n = conn.recv(10).decode()
    sir_nr = ' '
    print('Received from client:', n)
    for i in range(2, int(n)//2):
        if int(n) % i == 0:
            sir_nr += str(i) + " " + str(int(n)//i) + " "
    print("Sent to client:", sir_nr)
    conn.send(sir_nr.encode())
    if not n:
        break
    conn.close()

