function [I, n] = integrare_legendre_adaptiv(f, a, b, eps)
% Input:
%   f   - functia de integrat
%   a   - capat stanga
%   b   - capat dreapta
%   eps - precizia dorita
%
% Output:
%   I - valoarea aproximata a integralei
%   n - numarul de noduri necesare

    n = 2;
    [x, A] = gauss_legendre(n);

    % Transformare noduri in intervalul [a, b]
    % Se face schimb de variabila deoarece ca sa folosim acest mod de
    % calcul integrala trebuie sa fie definita pe [-1,1] nu pe [a,b]
    t = ((b - a)/2) * x + (a + b)/2;
    I_prev = ((b - a)/2) * sum(A .* f(t(:))');

    while true
        n = n + 1;
        [x, A] = gauss_legendre(n);
        t = ((b - a)/2) * x + (a + b)/2;
        I_curr = ((b - a)/2) * sum(A .* f(t(:))');

        if abs(I_curr - I_prev) < eps
            break;
        end

        I_prev = I_curr;
    end

    I = I_curr;
end