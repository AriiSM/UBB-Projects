import socket
import threading
import time
def handle_client(client_socket,addr):
                time.sleep(5)
                print("Lucrez cu: " + str(addr[0]) + " " + str(addr[1]) )
                data=client_socket.recv(1024).decode()
                data_result = data.split(",")
                sir = data_result[0]
                nr_port = data_result[1]
                print(sir)
                print(nr_port)

                minim = min(map(int, sir.split(" ")))
                client_socket.close()
                new_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                new_socket.connect(("0.0.0.0", int(nr_port)))

                response = f"Minimul din șirul {sir} este: {str(minim)}"
                new_socket.send(response.encode())


                new_socket.close()


host="localhost"
port=12345

server=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
server.bind((host,port))
server.listen(5)

while True:
        client,addr=server.accept()
        print("Accepted connection from " + str(addr[0]) + " "  + str(addr[1]) )
        client_handler=threading.Thread(target=handle_client,args=(client,addr))
        client_handler.start()
