function [solutie, nr_iteratii] = metoda_gauss_seidel_sor(A, b, w, tol, iter_max)
% INPUT:
%   A        - matricea coeficientilor (n x n), se presupune diagonala dominanta
%   b        - vectorul termenilor liberi (n x 1)
%   w        - factorul de relaxare (0 < w <= 2), w = 1 inseamna Gauss-Seidel simplu
%   tol      - toleranta (criteriul de oprire)
%   iter_max - numarul maxim de iteratii permise
%
% OUTPUT:
%   solutie      - vectorul aproximativ x care satisface Ax ≈ b
%   nr_iteratii  - numarul de iteratii efectuate

    n = length(b);         % dimensiunea sistemului
    x = zeros(n, 1);       % vectorul x curent (initial: toti zero)
    nr_iteratii = 0;       % contor iteratii

    while nr_iteratii < iter_max
        x_vechi = x;       % salvam vechea aproximare

        % pentru fiecare necunoscuta x_i
        for i = 1:n
            % calculam suma A(i,:) * x curent, dar eliminam A(i,i)*x(i)
            suma = A(i,:) * x - A(i,i) * x(i);

            % actualizam x(i) folosind formula SOR (relaxare suprapusa)
            x(i) = (1 - w) * x_vechi(i) + w * (b(i) - suma) / A(i,i);
        end

        nr_iteratii = nr_iteratii + 1;

        % verificam daca modificarile sunt suficient de mici (oprire)
        if norm(x - x_vechi, inf) < tol
            break;
        end
    end

    solutie = x;  % returnam rezultatul final
end
