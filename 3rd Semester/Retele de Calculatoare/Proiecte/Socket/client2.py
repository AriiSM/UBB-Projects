import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 8888

number = int(input("Enter the number: "))

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

s.send(str(number).encode())

print("Sent to server: ", number)

#Receiving the response from the server
data = s.recv(10).decode()
print("Received form server:", data)

s.close()