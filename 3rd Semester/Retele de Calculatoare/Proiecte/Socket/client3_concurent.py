import socket

TCP_IP = "0.0.0.0"
TCP_PORT = 12345

while True:
    sir = input("Enter the string: ")

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((TCP_IP, TCP_PORT))

    s.send(sir.encode())

    print("Sent to server: ", sir)

    #Receiving the response from the server
    data = s.recv(10).decode()
    print("Received form server:", data)
s.close()