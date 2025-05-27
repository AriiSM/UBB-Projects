function [L, U] = lu_fara_pivotare(A)
% INPUT:
%   A - matricea coeficientilor (n x n), presupusa nesingulara
%
% OUTPUT:
%   L - matrice triunghiulara inferiora cu 1 pe diagonala
%   U - matrice triunghiulara superiora
%
% Metoda: Descompunere LU fara pivotare
% A = L * U

    n = size(A, 1);     % dimensiunea matricei
    L = eye(n);         % initializare L cu identitate
    U = A;              % initial, U este A

    for k = 1:n-1
        for i = k+1:n
            if U(k, k) == 0
                error('Pivot nul pe pozitia (%d,%d). Algoritmul necesita pivotare.', k, k);
            end

            % Calculam factorul de eliminare
            L(i, k) = U(i, k) / U(k, k);

            % Actualizam linia i din U
            U(i, :) = U(i, :) - L(i, k) * U(k, :);
        end
    end
end