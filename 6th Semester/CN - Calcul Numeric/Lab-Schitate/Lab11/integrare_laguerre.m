function [I, I_ex, eroare] = integrare_laguerre(f, n, a, b, precizie)
% Input:
%   f        - functia f(x) fara pondere
%   n        - numarul de noduri
%   a, b     - capetele integralei (implicit: [0, ∞))
%   precizie - optional, precizia pentru comparatie numerica
%
% Output:
%   I        - valoare aproximata a integralei
%   I_ex     - valoare exacta calculata numeric
%   eroare   - eroare absoluta

    if nargin < 3, a = 0; end
    if nargin < 4, b = Inf; end
    if nargin < 5, precizie = NaN; end

    [x, A] = gauss_laguerre(n);

    if a == 0 && isinf(b)
        % Intervalul nativ pentru Gauss-Laguerre: [0, ∞)
        I = sum(A .* f(x));
    else
        % Daca intervalul nu este [0, ∞), aplicam schimbare de variabila:
        % x = (t - a) / (b - a), dar aici nu este cazul; putem avertiza
        warning("Gauss-Laguerre este destinat integrarii pe [0, ∞). Capetele [a, b] se ignora.");
        I = sum(A .* f(x));
    end

    % Integrare numerica pentru comparatie
    if isnan(precizie)
        I_ex = NaN;
        eroare = NaN;
    else
        integrand = @(x) f(x) .* exp(-x);
        I_ex = integral(integrand, a, b, 'RelTol', precizie);
        eroare = abs(I - I_ex);
    end
end

function [noduri, coeficienti] = gauss_laguerre(n)
%   Input:
%       n - numarul de noduri
%
%   Output:
%       nodes    - valorile nodurilor (radacinile polinomului Laguerre)
%       weights  - coeficientii de integrare

    alpha = zeros(n, 1);
    beta = sqrt((1:n-1)');  % coeficientii βk
    beta0 = 1;

    J = diag(alpha) + diag(beta, 1) + diag(beta, -1);

    % Valorile proprii = noduri, vectorii proprii pentru coeficienti
    [V, D] = eig(J);
    noduri = diag(D);
    [noduri, ord] = sort(noduri);
    V = V(:, ord);

    % Coeficientii A_k = beta0 * (v_1k)^2
    coeficienti = beta0 * V(1,:).^2;
end