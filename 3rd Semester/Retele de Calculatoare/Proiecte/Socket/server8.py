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
    sir1 = sir_result[0]
    sir2 = sir_result[1]
    print('Received from client:', sir1)
    print('Received from client:', sir2)
    rez = ''
    list1 = list(sir1)
    list2 = list(sir2)
    print(list1)
    print(list2)
    intersection_list = list(set(list1) & set(list2))
    print('Am trimis la server:',str(intersection_list))
    conn.send(str(intersection_list).encode())
    if not sir1:
        break
    if not sir2:
        break
    conn.close()
