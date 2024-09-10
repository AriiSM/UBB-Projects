import socket

def start_client(ip, port):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect((ip, port))

    message = client_socket.recv(1024)
    print(f"Mesaj primit de la server: {message.decode('utf-8')}")

    client_socket.close()

if __name__ == "__main__":
    # Pentru clientul cu adresa IP localhost
    ip1 = input('Dati adresa: ')
    ip2 = input('Dati adresa: ')
    start_client(ip1, 12345)

    # Pentru clientul cu adresa IP 10.151.1.161
    start_client(ip2, 12345)
