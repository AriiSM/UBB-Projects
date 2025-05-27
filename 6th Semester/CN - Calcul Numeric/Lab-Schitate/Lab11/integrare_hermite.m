function [I, eroare] = integrare_hermite(f, n, valoare_exacta)
% Input:
%   f              - functia f(x) (fara ponderea e^{-x^2})
%   n              - numarul de noduri
%   valoare_exacta - valoare reala de referinta pentru comparatie (optional)
%
% Output:
%   I      - valoare aproximata a integralei
%   eroare - eroare absoluta (daca se furnizeaza valoarea exacta)

    if nargin < 3
        valoare_exacta = NaN;
    end

    [x, A] = gauss_hermite(n);  % folosim generatorul standard

    I = sum(A .* f(x));

    if isnan(valoare_exacta)
        eroare = NaN;
    else
        eroare = abs(I - valoare_exacta);
    end
end

function [noduri, coeficienti] = gauss_hermite(n)
%   Input:
%       n - numarul de noduri
%
%   Output:
%       nodes    - radacinile polinomului Hermite H_n(x)
%       weights  - coeficientii asociati (pentru integrare)

    alpha = zeros(n, 1);
    beta = sqrt((1:n-1) / 2)';
    beta0 = sqrt(pi);

    J = diag(alpha) + diag(beta, 1) + diag(beta, -1);

    [V, D] = eig(J);
    noduri = diag(D);
    [noduri, ord] = sort(noduri);
    V = V(:, ord);
    
    coeficienti = beta0 * V(1,:).^2;
end