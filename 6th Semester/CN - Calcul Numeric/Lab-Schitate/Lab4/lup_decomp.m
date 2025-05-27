function [L, U, p] = lup_decomp(A)
% INPUT:
%   A - matricea coeficientilor (n x n), nesingulara
%
% OUTPUT:
%   L - matrice triunghiulara inferiora cu 1 pe diagonala
%   U - matrice triunghiulara superiora
%   p - vectorul de permutare al liniilor
%
% Algoritm: Descompunere LUP dupa schema din curs (Algoritmul 3)
% PA = LU, unde P este reprezentata prin vectorul p

    m = size(A,1);   % dimensiunea matricei
    p = 1:m;         % initializam vectorul de permutare: p = 1 : m
    U = A;           % initial, U este matricea A
    L = eye(m);      % initial, L este matricea identitate

    for k = 1:m-1
        % -------------------------
        % {Pivotare}
        % Cautam indexul i >= k care maximizeaza |U(i,k)|
        [~, idx] = max(abs(U(k:end, k)));
        i = idx + k - 1;

        % Interschimbam liniile k si i in U
        if i ~= k
            U([k i], :) = U([i k], :);             % A_k <-> A_i
            L([k i], 1:k-1) = L([i k], 1:k-1);     % Interschimbare in L doar pana la coloana k-1
            p([k i]) = p([i k]);                   % pk <-> pi in vectorul de permutare
        end

        % -------------------------
        % {Calculez complementul Schur}
        for lin = k+1:m
            % A_lin,k := A_lin,k / A_k,k   (salvam in L coeficientii de eliminare)
            L(lin, k) = U(lin, k) / U(k, k);

            % A_lin,lin := A_lin,lin − A_lin,k * A_k,lin
            U(lin, :) = U(lin, :) - L(lin, k) * U(k, :);
        end
    end
end
