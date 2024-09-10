import socket
import threading

def handle_client(connection, address):
    print(f"Conexiune acceptată de la {address}")

    # Află adresa IP a clientului
    client_ip, client_port = connection.getpeername()
    print(f"Adresa IP a clientului: {client_ip}")

    message = "Bine ai venit la server!"
    connection.send(message.encode('utf-8'))

    connection.close()

def start_server(ip, port):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((ip, port))
    server_socket.listen(5)

    print(f"Serverul ascultă la orice adresă IP disponibilă și portul {port}")

    while True:
        connection, address = server_socket.accept()

        # Creează un fir de execuție pentru a gestiona conexiunea
        client_handler = threading.Thread(target=handle_client, args=(connection, address))
        client_handler.start()

if __name__ == "__main__":
    server_port = int(input("Introdu portul serverului: "))
    start_server('0.0.0.0', server_port)
