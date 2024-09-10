import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 8888

sir1 = input("Enter the first string: ")
sir2 = input("Enter the second string: ")
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))


print("Sent to server: ", sir1)

print("Sent to server: ", sir2)
s.send((sir1 + ',' +sir2).encode())
#Receiving the response from the server
data = s.recv(1024).decode()
print("Received form server:", str(data))

s.close()