function x = backsub(R, y)
% INPUT:
%   R - matrice triunghiulara superioara (n x n)
%   y - vector coloana (n x 1), rezultat din Q^T * b
%
% OUTPUT:
%   x - solutia sistemului R * x = y
%
% Metoda: Substitutie inapoi (Backward substitution)
% Se rezolva sistemul pornind de la ultima ecuatie catre prima

    n = length(y);      % dimensiunea sistemului
    x = zeros(n,1);     % initializare vector solutie

    % Parcurgere de la ultima linie la prima
    for i = n:-1:1
        % x(i) = ( y(i) - suma R(i,j) * x(j) pentru j = i+1..n ) / R(i,i)
        x(i) = (y(i) - R(i,i+1:end) * x(i+1:end)) / R(i,i);
    end
end