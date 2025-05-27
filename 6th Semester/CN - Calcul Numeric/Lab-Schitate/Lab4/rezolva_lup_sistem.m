function x = rezolva_lup_sistem(L, U, b, p)
% INPUT:
%   L - matrice triunghiulara inferior (cu 1 pe diagonala)
%   U - matrice triunghiulara superior
%   b - vectorul termenilor liberi (n x 1)
%   p - vectorul de permutare obtinut in descompunere
%
% OUTPUT:
%   x - vectorul solutie al sistemului Ax = b
%
% Metoda: rezolvare sistem cu LUP:
%   1. rezolva Ly = Pb (descendent)
%   2. rezolva Ux = y  (ascendent)

    n = length(b);
    y = zeros(n, 1);
    x = zeros(n, 1);

    % Aplicam permutarea la b
    b_perm = b(p);

    % Substitutie directa (Ly = b_perm)
    for i = 1:n
        y(i) = b_perm(i) - L(i, 1:i-1) * y(1:i-1);
    end

    % Substitutie inversa (Ux = y)
    for i = n:-1:1
        x(i) = (y(i) - U(i, i+1:n) * x(i+1:n)) / U(i, i);
    end
end