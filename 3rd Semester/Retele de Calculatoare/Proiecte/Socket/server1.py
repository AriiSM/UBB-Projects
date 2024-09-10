import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 8888

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((TCP_IP, TCP_PORT))
server_socket.listen(1)

while 1:
    conn, addr = server_socket.accept()
    n = conn.recv(10)
    total_sum = 0
    print("Received from client:", n)
    print(type(n))
    for i in range(int(n)):
        data = conn.recv(10)
        print("Received from client:", data.decode())
        if not data:
            break
        total_sum += int(data)
    print("Sent to client:", total_sum)
    conn.send(str(total_sum).encode())
    if not n:
        break
    conn.close()



