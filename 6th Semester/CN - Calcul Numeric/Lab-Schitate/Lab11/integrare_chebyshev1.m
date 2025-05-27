function [I, I_ex, eroare] = integrare_chebyshev1(f, n, a, b, precizie)
% Input:
%   f         - functia f(x), fara pondere
%   n         - numarul de noduri
%   a, b      - capetele integralei (default [-1, 1])
%   precizie  - precizia dorita (pentru calculul erorii)
%
% Output:
%   I         - valoare aproximata
%   I_ex      - valoare numerica exacta (pentru verificare)
%   eroare    - diferenta fata de integrarea numerica

    if nargin < 3, a = -1; end
    if nargin < 4, b = 1; end
    if nargin < 5, precizie = NaN; end

    [x, A] = gauss_chebyshev1(n);

    % Schimbare de variabila: x ∈ [-1,1] → t ∈ [a,b]
    % x = (2*t - (b + a)) / (b - a)  ⇔  t = ((b - a)/2)*x + (a + b)/2
    t = ((b - a)/2) * x + (a + b)/2;

    % f compusa si ponderata (in loc de w(x), care e absorbit)
    I = (b - a)/2 * sum(A .* f(t));

    % Integrare exacta (numerica), daca se doreste
    if isnan(precizie)
        I_ex = NaN;
        eroare = NaN;
    else
        g = @(x) f(x) ./ sqrt(1 - ((2*x - (b + a))/(b - a)).^2);
        I_ex = integral(g, a, b, 'RelTol', precizie);
        eroare = abs(I - I_ex);
    end
end

function [noduri, coeficienti] = gauss_chebyshev1(n)
%   Input:
%       n - numarul de noduri
%
%   Output:
%       nodes    - valorile nodurilor (cosinusuri echidistante)
%       weights  - coeficientii de integrare, egali (pi/n)

    k = 1:n; % indexul nodurilor

    % Nodurile sunt cosinusuri echidistante in [0, pi]
    noduri = cos((2*k - 1) * pi / (2 * n));

    % Toti coeficientii sunt egali cu pi/n
    coeficienti = (pi / n) * ones(1, n);
end