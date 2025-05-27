function I = FormulaTrapezAdaptiv(f, a, b, tol)
    I = trapez_adaptiv_general(f, a, b, tol);
end



function I = trapez_adaptiv_general(f, a, b, tol)
% INPUT:
%   f    - funcția de integrat
%   a,b  - capetele intervalului
%   tol  - toleranta erorii
% OUTPUT:
%   I    - valoarea aproximativa a integralei

    fa = f(a); fb = f(b);
    I = subdivide_trapez(f, a, b, tol, fa, fb);
end

function I = subdivide_trapez(f, a, b, tol, fa, fb)
    % Pasul 1: punct intermediar
    c = (a + b) / 2;
    fc = f(c);

    % Pasul 2: aproximare cu un trapez si apoi doua
    T1 = (b - a)/2 * (fa + fb);
    T2 = (b - a)/4 * (fa + 2 * fc + fb);

    % Pasul 3: verificare eroare si recursie
    if abs(T2 - T1) < 3 * tol
        I = T2 + (T2 - T1) / 3;
    else
        I = subdivide_trapez(f, a, c, tol/2, fa, fc) + ...
            subdivide_trapez(f, c, b, tol/2, fc, fb);
    end
end