function [aprox, num_iteratii] = metoda_jacobi(A, b, tol, iter_max)
% INPUT:
%   A         - matricea coeficientilor (n x n), presupusa diagonal dominanta
%   b         - vectorul termenilor liberi (n x 1)
%   tol       - toleranta dorita (precizia la care ne oprim)
%   iter_max  - numarul maxim de iteratii permise
%
% OUTPUT:
%   aprox         - vectorul solutie aproximativa (x)
%   num_iteratii  - numarul de iteratii efectuate pana la convergenta

    n = size(A, 1);                  % Dimensiunea sistemului (numar de necunoscute)
    x_vechi = zeros(n, 1);           % Initializare solutie precedenta (x^(k))
    aprox = zeros(n, 1);             % Initializare solutie curenta (x^(k+1))
    num_iteratii = 0;                % Initializare contor de iteratii

    % Pornim procesul iterativ
    while num_iteratii < iter_max
        % Pentru fiecare necunoscuta x_i
        for i = 1:n
            suma = 0;
            % Suma a_ij * x_j pentru j ≠ i
            for j = 1:n
                if j ~= i
                    suma = suma + A(i,j) * x_vechi(j);
                end
            end
            % Calculul noii valori pentru x_i
            aprox(i) = (b(i) - suma) / A(i,i);
        end

        % Verificăm daca solutia s-a stabilizat (criteriu de oprire)
        if norm(aprox - x_vechi, inf) < tol
            break;  % Iesim din bucla dacă am atins toleranta
        end

        % Pregatim pentru urmatoarea iteratie
        x_vechi = aprox;
        num_iteratii = num_iteratii + 1;
    end
end
