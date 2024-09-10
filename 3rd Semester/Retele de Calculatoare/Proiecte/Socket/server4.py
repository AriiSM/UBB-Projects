import socket

TCP_IP ="127.0.0.1"
TCP_PORT = 8888

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((TCP_IP, TCP_PORT))
server_socket.listen(1)

while True:
    conn, addr = server_socket.accept()
    data = conn.recv(1024)

    data_string = data.decode().split(',')
    sir1 = data_string[0]
    sir2 = data_string[1]
    print('Am primit de la client:', sir1)
    print("Am primit de la client: ", sir2)
    sir_interclasat = ''
    lungime_minima = min(len(sir1), len(sir2))
    len1 = len2 = 0
    print(lungime_minima)
    while len1 < len(sir1) and len2 < len(sir2):
        if (sir1[len1] < sir2[len2]):
            sir_interclasat += sir1[len1]
            len1 += 1
        else:
            sir_interclasat += sir2[len2]
            len2 += 1

    sir_interclasat += sir1[len1:] + sir2[len2:]
    conn.send(str(sir_interclasat).encode())
    if not sir1 or not sir2:
        break
    conn.close()