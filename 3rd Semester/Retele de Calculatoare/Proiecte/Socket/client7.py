import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 8888

sir = input("Enter the string: ")
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

s.send(sir.encode())

print("Sent to server: ", sir)

pozitie = input("Enter position: ")
s.send(str(pozitie).encode())

lungime = input("Enter length: ")
s.send(str(lungime).encode())

#Receiving the response from the server
data = s.recv(1024).decode()
print("Received form server:", data)

s.close()