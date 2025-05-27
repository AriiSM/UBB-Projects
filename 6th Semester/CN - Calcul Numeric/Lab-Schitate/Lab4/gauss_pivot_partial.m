function x = gauss_pivot_partial(A, b)
% INPUT:
%   A - matricea coeficienților (n x n)
%   b - vectorul termenilor liberi (n x 1)
%
% OUTPUT:
%   x - soluția sistemului Ax = b
%
% Metoda: Eliminare Gauss cu pivotare partiala (pe coloana)

    [n, ~] = size(A);
    Ab = [A, b]; % Matrice extinsă

    for i = 1:n-1
        % Gasire pivot (valoare maxima absoluta pe coloana i, de la linia i in jos)
        [~, p] = max(abs(Ab(i:n, i)));
        p = p + i - 1;

        if Ab(p, i) == 0
            error('Sistemul nu are soluție unică.');
        end

        % Schimbare de linii, dacă e cazul
        if p ~= i
            Ab([i, p], :) = Ab([p, i], :);
        end

        % Eliminare
        for j = i+1:n
            m = Ab(j, i) / Ab(i, i);
            Ab(j, i:end) = Ab(j, i:end) - m * Ab(i, i:end);
        end
    end

    if Ab(n, n) == 0
        error('Sistemul nu are soluție unică.');
    end

    % Substitutie inversa
    x = zeros(n, 1);
    for i = n:-1:1
        x(i) = (Ab(i, end) - Ab(i, i+1:n) * x(i+1:n)) / Ab(i, i);
    end
end