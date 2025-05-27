function [Q, R] = qr_descompunere(A)
% INPUT:
%   A - matrice reala (n x m), cu n >= m si coloane liniar independente
%
% OUTPUT:
%   Q - matrice ortogonala (n x m)
%   R - matrice triunghiulara superioara (m x m)
%
% Metoda: Descompunere QR prin Gram-Schmidt modificat
% A = Q * R

    [n, m] = size(A);
    Q = zeros(n, m);
    R = zeros(m, m);

    for j = 1:m
        v = A(:, j);

        % Eliminam componentele deja ortogonalizate
        for i = 1:j-1
            R(i, j) = Q(:, i)' * A(:, j);
            v = v - R(i, j) * Q(:, i);
        end

        % Normalizam vectorul
        R(j, j) = norm(v);
        Q(:, j) = v / R(j, j);
    end
end

