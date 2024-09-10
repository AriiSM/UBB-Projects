import socket
import struct

def main():
    c = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_address = ('127.0.0.1', 1234)

    print("DATI SIRUL: ")
    sir = input()

    len_data = struct.pack('!I', len(sir) + 1)
    c.sendto(len_data, server_address)
    c.sendto(sir.encode('utf-8'), server_address)

    len_data, _ = c.recvfrom(struct.calcsize('!H'))
    port = struct.unpack('!H', len_data)[0]
    print(f"AM PRIMIT PORTUL: {port}")

    server_address = ('127.0.0.1', port)

    suma_data, _ = c.recvfrom(struct.calcsize('!I'))
    suma = struct.unpack('!I', suma_data)[0]
    print(f"SUMA CLIENTULUI ANTERIOR: {suma}")

    lung_data, _ = c.recvfrom(struct.calcsize('!I'))
    lung = struct.unpack('!I', lung_data)[0]
    ip_data, _ = c.recvfrom(lung)
    ip = ip_data.decode('utf-8')
    print(f"IP UL CLIENTULUI ANTERIOR: {ip}")

    c.close()

if __name__ == "__main__":
    main()
