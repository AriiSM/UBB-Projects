import socket

# Configurare server
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(('127.0.0.1', 12345))  # Adresa și portul serverului
server_socket.listen(5)  # Numărul maxim de conexiuni în așteptare

print("Serverul asculta...")

while True:
    # Acceptă o conexiune de la un client
    client_socket, client_address = server_socket.accept()
    print(f"Conexiune acceptata de la {client_address}")

    # Trimite un mesaj de întâmpinare către client
    client_socket.send("Bine ai venit la server!".encode('utf-8'))

    # Închide conexiunea cu clientul curent
    client_socket.close()
