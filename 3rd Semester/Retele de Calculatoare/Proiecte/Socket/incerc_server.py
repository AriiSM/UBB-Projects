import socket
import struct
import random

def main():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_address = ('0.0.0.0', 1234)
    s.bind(server_address)

    print("Incepem primirea")

    ok = False
    suma_veche = 0
    ip = ""

    while True:
        len_data, client_address = s.recvfrom(struct.calcsize('!I'))
        len_data = struct.unpack('!I', len_data)[0]

        sir, client_address = s.recvfrom(len_data)
        sir = sir.decode('utf-8')
        len_data -= 1

        suma = sum(map(int, sir.split()))

        # Generez un port de 4 cifre random si il trimit
        port = 1000 + random.randint(0, 1000)
        print(f"PORTUL GENERAT: {port}")
        port = struct.pack('!H', port)
        s.sendto(port, client_address)
        port = struct.unpack('!H', port)[0]

        # Schimb portul si trimit suma calculata
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s1:
            celalalt_server = ('0.0.0.0', port)
            s1.bind(celalalt_server)

            if not ok:
                ip = client_address[0]
                suma_veche = suma
                ok = True

            suma = struct.pack('!I', suma_veche)
            s1.sendto(suma, client_address)

            lung = len(ip) + 1
            lung = struct.pack('!I', lung)
            s1.sendto(lung, client_address)

            s1.sendto(ip.encode('utf-8'), client_address)

if __name__ == "__main__":
    main()
