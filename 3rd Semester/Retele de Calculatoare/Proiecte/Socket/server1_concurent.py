import socket
import threading
import time
def handle_client(client_socket,addr):
        while True:
                time.sleep(5)
                print("Lucrez cu: " + str(addr[0]) + str(addr[1]) )
                data=client_socket.recv(1024).decode()
                print(data)
                count_s=data.count('s')

                response="Numarul de litere s din sir:" + str(count_s)
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
