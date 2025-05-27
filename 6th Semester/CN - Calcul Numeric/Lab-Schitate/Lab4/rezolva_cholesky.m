function x = rezolva_cholesky(A, b)
% INPUT:
%   A - matrice simetrica si pozitiv definita (n x n)
%   b - vectorul termenilor liberi (n x 1)
%
% OUTPUT:
%   x - solutia sistemului Ax = b
%
% Metoda: Se face Cholesky A = R' * R, apoi:
%   1. Se rezolva R' * y = b (substitutie directa)
%   2. Se rezolva R * x = y  (substitutie inversa)

    R = cholesky_decomp(A);  % Descompunerea A = R' * R
    n = length(b);
    y = zeros(n, 1);
    x = zeros(n, 1);

    % Substitutie directa: R' * y = b
    for i = 1:n
        y(i) = (b(i) - R(1:i-1, i)' * y(1:i-1)) / R(i, i);
    end

    % Substitutie inversa: R * x = y
    for i = n:-1:1
        x(i) = (y(i) - R(i, i+1:n) * x(i+1:n)) / R(i, i);
    end
end