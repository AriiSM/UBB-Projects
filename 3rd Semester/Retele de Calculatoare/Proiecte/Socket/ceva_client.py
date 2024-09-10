import socket

# Configurare client
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = ('127.0.0.1', 12345)  # Adresa și portul serverului

# Conectare la server
client_socket.connect(server_address)

# Primește mesajul de la server
message = client_socket.recv(1024)
print(f"Mesaj primit de la server: {message.decode('utf-8')}")

# Închide conexiunea cu serverul
client_socket.close()
