import socket

TCP_IP ="127.0.0.1"
TCP_PORT = 8888

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((TCP_IP, TCP_PORT))
server_socket.listen(1)

while 1:
    conn, addr = server_socket.accept()
    data = conn.recv(100).decode()
    sir_result = data.split(',')
    sir = sir_result[0]
    c = sir_result[1]
    count = 0
    print('Received from client:', sir)
    print('Received from client:', c)
    rez = ''
    for i in range(len(sir)):
        if sir[i] == c:
            rez += str(i+1) + " "

    print(rez)
    conn.send(rez.encode())
    if not sir:
        break
    if not c:
        break
    conn.close()
