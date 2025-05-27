function [I, eroare] = integrare_jacobi_custom(f, n, alpha, beta, a, b, pondere, precizie)
% Input:
%   f         - functia f(x) fara pondere
%   n         - numărul de noduri
%   alpha, beta - parametrii pentru Gauss-Jacobi (alpha, beta > -1)
%   a, b      - capetele intervalului de integrare
%   pondere   - functia de pondere (w(x)) (ex: @(x) 1./sqrt(sin(x)))
%   precizie  - valoare exactă pentru calculul erorii (opțional)
%
% Output:
%   I         - aproximarea integralei
%   eroare    - eroarea absoluta fata de valoarea exacta (daca este data)

    % Pas 1: Noduri si coeficienți standard pe [-1, 1]
    [t, A] = gauss_jacobi(n, alpha, beta);

    % Pas 2: Schimbare de variabila: t ∈ [-1, 1] → x ∈ [a, b]
    x = ((b - a) / 2) * t + (a + b) / 2;

    % Pas 3: Evaluam functia in x si inmultim cu ponderea
    fx = f(x) .* pondere(x);

    % Pas 4: Aplicam formula
    I = ((b - a) / 2) * sum(A .* fx);

    % Pas 5: Eroare (daca se dă valoare exacta)
    if nargin < 8 || isempty(precizie)
        eroare = NaN;
    else
        eroare = abs(I - precizie);
    end
end

function [noduri, coeficienti] = gauss_jacobi(n, alpha, beta)
%   Input:
%       n     - numarul de noduri
%       alpha - parametru al ponderei (> -1)
%       beta  - parametru al ponderei (> -1)
%
%   Output:
%       nodes    - valorile nodurilor (radacini polinom Jacobi)
%       weights  - coeficientii asociati nodurilor

    if alpha <= -1 || beta <= -1
        error('alpha si beta trebuie sa fie > -1');
    end

    a = zeros(n, 1);      % coeficientii diagonali α_k
    b = zeros(n-1, 1);    % coeficientii sub/supradiagonali β_k

    for k = 0:n-1
        if k > 0
            num = 4*k*(k + alpha)*(k + beta)*(k + alpha + beta);
            den = (2*k + alpha + beta)^2 * (2*k + alpha + beta + 1) * (2*k + alpha + beta - 1);
            b(k) = sqrt(num / den);
        end
        a(k+1) = (beta^2 - alpha^2) / ((2*k + alpha + beta) * (2*k + alpha + beta + 2));
    end

    % Matricea Jacobi
    J = diag(a) + diag(b, 1) + diag(b, -1);

    % Calculam valorile si vectorii proprii
    [V, D] = eig(J);
    noduri = diag(D);
    [noduri, ord] = sort(noduri);
    V = V(:, ord);

    % Coeficientii: formula generalizata (beta0 este constanta de normalizare)
    beta0 = 2^(alpha + beta + 1) * gamma(alpha + 1) * gamma(beta + 1) / gamma(alpha + beta + 2);
    coeficienti = beta0 * V(1,:).^2;
end