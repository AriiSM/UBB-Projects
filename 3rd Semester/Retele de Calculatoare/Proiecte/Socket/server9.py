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

    print('Received from client:', sir1)

    sir2 = conn.recv(10)
    print('Received from client:', sir2)
    rez = ''
    list1 = sir1.split()
    list2 = sir2.split()
    for i in range(len(list1)):
        ok = True
        for j in range(len(list2)):
            if list1[i] == list2[j]:
                ok = False
        if ok == True:
            rez+= list1[i]
            rez+= " "
    if not sir1:
        break
    if not sir2:
        break
    print(rez)
    conn.send(rez)
    conn.close()
