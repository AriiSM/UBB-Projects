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
    sir_inversat = "".join(reversed(sir))
    conn.send(str(sir_inversat).encode())
    if not sir:
        break
    conn.close()
