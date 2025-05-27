function I = FormulaDreptunghiAdaptiv(f, a, b, tol)
    I = dreptunghi_adaptiv_general(f, a, b, tol);
end

function I = dreptunghi_adaptiv_general(f, a, b, tol)
% INPUT:
%   f    - funcția de integrat
%   a,b  - capetele intervalului
%   tol  - toleranta pentru eroare
% OUTPUT:
%   I    - valoare aproximata a integralei

    I = subdivide_dreptunghi(f, a, b, tol);
end

function I = subdivide_dreptunghi(f, a, b, tol)
    % Pasul 1: puncte de mijloc si submijloc
    m = (a + b) / 2;
    h = b - a;

    % Pasul 2: 2 aproximari — 1 dreptunghi, 2 dreptunghiuri
    M1 = h * f(m);  % 1 dreptunghi pe [a,b]
    M2 = (h / 2) * (f((a + m)/2) + f((m + b)/2));  % 2 dreptunghiuri

    % Pasul 3: verificare eroare
    if abs(M2 - M1) < 3 * tol
        I = M2 + (M2 - M1) / 3;
    else
        I = subdivide_dreptunghi(f, a, m, tol/2) + ...
            subdivide_dreptunghi(f, m, b, tol/2);
    end
end