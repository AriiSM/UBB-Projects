import socket
import threading
import time
def handle_client(client_socket, addr):
    while True:
        time.sleep(5)
        print("Lucrez cu: " + str(addr[0]) + str(addr[1]))
        n = client_socket.recv(1024).decode()
        if not n:
            break  # Break out of the loop if the client closes the connection

        print('Received from client:', n)

        is_prim = False
        for i in range(2, int(n) // 2 + 1):  # Adjusted the range to include the upper limit
            if int(n) % i == 0:
                is_prim = False
                break
            is_prim = True

        print("Sent to client:", is_prim)
        response = str(is_prim)
        client_socket.send(response.encode())

    client_socket.close()


host="localhost"
port=12345

server=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
server.bind((host,port))
server.listen(5)

while True:
        client,addr=server.accept()
        print("Accepted connection from " + str(addr[0]) + str(addr[1]) )
        client_handler=threading.Thread(target=handle_client,args=(client,addr))
        client_handler.start()