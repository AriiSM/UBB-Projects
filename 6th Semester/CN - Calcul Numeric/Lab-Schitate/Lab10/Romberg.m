function [rezultat, matrice_romberg] = Romberg(f, a, b, tol, max_iter)
    if nargin < 5
        max_iter = 10;  % daca nu se specifica, folosim 10 iteratii maxim
    end
    [rezultat, matrice_romberg] = romberg_custom(f, a, b, tol, max_iter);
end


function [rezultat, matrice_romberg] = romberg_custom(f, a, b, tol, max_iter)
% INPUT:
%   f         - functia de integrat (fara pondere)
%   a, b      - capetele intervalului de integrare
%   tol       - toleranta maxima admisa pentru eroare absoluta
%   max_iter  - (optional) numar maxim de iteratii (default: 10)
%
% OUTPUT:
%   rezultat         - valoarea aproximata a integralei
%   matrice_romberg  - matricea completa R(i, j) cu aproximari intermediare

    if nargin < 5
        max_iter = 10;  % daca nu se specifica, folosim 10 iteratii maxim
    end

    matrice_romberg = zeros(max_iter);  % initializare matrice patratica
    h = b - a;  % lungimea initiala a intervalului

    % R(1,1): regula trapezului de baza (1 singur trapez)
    matrice_romberg(1,1) = h / 2 * (f(a) + f(b));

    for k = 2:max_iter
        h = h / 2;  % la fiecare pas, injumatatim pasul

        % calculam suma punctelor noi dintre cele deja folosite
        suma = 0;
        for i = 1:2^(k-2)
            x_i = a + (2*i - 1) * h;
            suma = suma + f(x_i);
        end

        % R(k,1): aproximare cu regula trapezului compus
        matrice_romberg(k,1) = 0.5 * matrice_romberg(k-1,1) + h * suma;

        % extrapolare Richardson pentru R(k,j)
        for j = 2:k
            matrice_romberg(k,j) = (4^(j-1) * matrice_romberg(k,j-1) - matrice_romberg(k-1,j-1)) / ...
                                   (4^(j-1) - 1);
        end

        % criteriu de oprire: diagonala principala converge
        if abs(matrice_romberg(k,k) - matrice_romberg(k-1,k-1)) < tol
            rezultat = matrice_romberg(k,k);
            return;
        end
    end

    % daca nu s-a atins toleranta dupa max_iter iteratii
    warning('Romberg: nu s-a atins toleranta dupa %d iteratii.', max_iter);
    rezultat = matrice_romberg(max_iter, max_iter);
end