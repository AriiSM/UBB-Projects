function [I, I_ex, eroare] = integrare_chebyshev2(f, n, a, b, precizie)
% Input:
%   f        - funcția f(x) fără pondere
%   n        - numărul de noduri
%   a, b     - capetele integralei (ideal [-1, 1])
%   precizie - (opțional) precizie numerică pentru calculul valorii exacte
%
% Output:
%   I        - valoarea aproximată a integralei
%   I_ex     - valoarea exactă (numerică)
%   eroare   - eroare absolută

   if nargin < 5
        precizie = NaN;
    end

    % Obtinem nodurile si coeficientii pentru [-1, 1]
    [t, A] = gauss_chebyshev2(n);

    % Daca intervalul nu este [-1, 1], aplicam schimbare de variabila
    if ~(a == -1 && b == 1)
        % x = ((b - a)/2) * t + (a + b)/2
        x = ((b - a)/2) * t + (a + b)/2;
        fx = f(x);  % evaluam functia in punctele transformate
        I = ((b - a)/2) * sum(A .* fx);
    else
        x = t;
        fx = f(x);
        I = sum(A .* fx);
    end

    % Valoare exactă (numerica) pentru comparatie
    if ~isnan(precizie)
        g = @(x) sqrt(1 - ((2*x - (b + a)) / (b - a)).^2) .* f(x); % schimbare inversă în densitate
        I_ex = integral(g, a, b, 'RelTol', precizie);
        eroare = abs(I - I_ex);
    else
        I_ex = NaN;
        eroare = NaN;
    end
end

function [noduri, coeficienti] = gauss_chebyshev2(n)
%   Input:
%       n - numarul de noduri
%
%   Output:
%       nodes    - valorile nodurilor
%       weights  - coeficientii de integrare

    k = 1:n;% indexul nodurilor

    % Nodurile sunt date de cos(pi * k / (n + 1))
    noduri = cos(pi * k / (n + 1));

    % Coeficientii depend de sin^2(k * pi / (n + 1))
    coeficienti = pi / (n + 1) * sin(pi * k / (n + 1)).^2;
end