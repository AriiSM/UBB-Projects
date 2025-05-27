function x = gauss_pivot_scalat(A, b)
% INPUT:
%   A - matricea coeficienților (n x n)
%   b - vectorul termenilor liberi (n x 1)
%
% OUTPUT:
%   x - solutia sistemului Ax = b
%
% Metoda: Eliminare Gauss cu pivotare scalata pe coloana
% (aleg pivot in functie de valoarea relativa la scara liniei)

    [n, ~] = size(A);
    Ab = [A, b]; % Matrice extinsă

    % Vector de scalare: maximul absolut pe fiecare linie
    s = max(abs(A), [], 2);

    for i = 1:n-1
        % Calcul pivot scalat: max |a_ij| / s_i
        ratios = abs(Ab(i:n, i)) ./ s(i:n);
        [~, p] = max(ratios);
        p = p + i - 1;

        if Ab(p, i) == 0
            error('Sistemul nu are soluție unică.');
        end

        % Schimbare de linii, dacă e cazul
        if p ~= i
            Ab([i, p], :) = Ab([p, i], :);
            s([i, p]) = s([p, i]); % swap și în vectorul de scalare
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

    % Substituție inversă
    x = zeros(n, 1);
    for i = n:-1:1
        x(i) = (Ab(i, end) - Ab(i, i+1:n) * x(i+1:n)) / Ab(i, i);
    end
end