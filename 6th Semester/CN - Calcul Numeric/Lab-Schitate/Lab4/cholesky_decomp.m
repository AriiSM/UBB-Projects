function R = cholesky_decomp(A)
% INPUT:
%   A - matricea simetrica si pozitiv definita (n x n)
%
% OUTPUT:
%   R - matricea triunghiulara superiora astfel incat A = R' * R
%
% Metoda: Descompunere Cholesky (Algoritmul 4 din curs)
% Transforma A in R, lucrand direct pe submatricea de sus

    R = A;              % R := A
    m = size(A, 1);     % dimensiunea matricei

    for k = 1:m
        % -------------------------
        % R(k, k:m) := R(k, k:m) / sqrt(R(k, k))
        R(k, k:m) = R(k, k:m) / sqrt(R(k, k));  % normalizare pe linia k

        % -------------------------
        % pentru fiecare j = k+1 .. m
        for j = k+1:m
            % R(j, j:m) := R(j, j:m) - R(k, j:m) * R(k, j) / R(k, k)
            % echivalent cu eliminare a contributiei liniei k asupra liniei j
            R(j, j:m) = R(j, j:m) - (R(k, j:m) * R(k, j)) / R(k, k);
        end
    end
end