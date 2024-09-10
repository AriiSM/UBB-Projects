import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 8888

sir = input("Enter the string: ")
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

s.send(sir.encode())

print("Sent to server: ", sir)

#Receiving the response from the server
data = s.recv(1024).decode()
print("Received form server:", str(data))

s.close()