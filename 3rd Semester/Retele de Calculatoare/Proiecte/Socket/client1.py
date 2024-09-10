import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 8888

# Get the number of numbers as input
num_of_numbers = int(input("Enter the number of numbers: "))

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

# Sending the number of numbers to the server
s.send(str(num_of_numbers).encode())

print("Sent to server:", num_of_numbers)

# Sending individual numbers to the server
for i in range(num_of_numbers):
    print(i)
    message = input("Enter a number:")
    s.send(str(message).encode())  # Convert message to string before sending
    print("Sent to server:", message)

# Receiving the response from the server
data = s.recv(10).decode()
print("Received from server:", data)

s.close()

