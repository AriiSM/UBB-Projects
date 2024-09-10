import socket
import threading
import time
def handle_client(client_socket, addr):
    while True:
        time.sleep(5)
        print("Lucrez cu: " + str(addr[0]) + str(addr[1]))
        sir = client_socket.recv(1024).decode()
        if not sir:
            break  # Break out of the loop if the client closes the connection

        print('Received from client:', sir)
        sir_inversat = "".join(reversed(sir))
        print("Sent to client:", sir_inversat)
        response = str(sir_inversat)
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