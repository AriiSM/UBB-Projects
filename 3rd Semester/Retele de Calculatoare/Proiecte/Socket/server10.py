import socket

TCP_IP ="127.0.0.1"
TCP_PORT = 8888

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((TCP_IP, TCP_PORT))
server_socket.listen(1)

while 1:
    conn, addr = server_socket.accept()
    sir1 = conn.recv(100)
    print('Received from client:', sir1)

    sir2 = conn.recv(10)
    print('Received from client:', sir2)
    rez = ''
    frecquency_vector = {}
    len_min = min(len(sir1), len(sir2))
    for i in range(len_min):
        if sir1[i] == sir2[i]:
            if sir1[i ] in frecquency_vector:
                frecquency_vector[sir1[i]] += 1
            else:
                frecquency_vector[sir1[i]] = 1
    max = 0
    c = ''
    for char, count in frecquency_vector.items():
        if count > max:
            max = count
            c = char

    rez = c + " " + str(max)
    if not sir1:
        break
    if not sir2:
        break
    print(rez)
    conn.send(rez)
    conn.close()
