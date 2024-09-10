import socket

TCP_IP ="127.0.0.1"
TCP_PORT = 8888

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((TCP_IP, TCP_PORT))
server_socket.listen(1)

while 1:
    conn, addr = server_socket.accept()
    sir = conn.recv(100).decode()
    count = 0
    print('Received from client:', sir)

    ipoz = conn.recv(10).decode()
    print('Received from client:', ipoz)
    rez = ''
    l = conn.recv(10).decode()
    print('Received from client:', l)
    for i in range(len(sir)):
        if i+1 == int(ipoz):
            for j in range(int(l)):
                rez += sir[int(ipoz)+j-1]


    conn.send(rez.encode())
    if not sir:
        break
    conn.close()
