function I = FormulaSimpsonAdaptiva(f, a, b, tol)
    I = simpson_adaptiv_general(f, a, b, tol);
end

function I = simpson_adaptiv_general(f, a, b, tol)
% INPUT:
%   f    - funcția de integrat (fara pondere)
%   a,b  - capetele intervalului de integrare
%   tol  - toleranta dorita pentru eroarea absoluta
% OUTPUT:
%   I    - aproximarea integralei 

    fa = f(a); 
    fb = f(b); 
    fc = f((a + b) / 2);  % evaluare în mijloc
    I = subdivide_simpson(f, a, b, tol, fa, fc, fb);
end

function I = subdivide_simpson(f, a, b, tol, fa, fc, fb)
    % Pasul 1: puncte intermediare
    c = (a + b) / 2;
    d = (a + c) / 2; 
    e = (c + b) / 2;

    % Pasul 2: evaluare functie in puncte noi
    fd = f(d); fe = f(e);

    % Pasul 3: 2 aproximari - coarse (1 aplicare Simpson) si refined (doua aplicatii)
    S1 = (b - a) / 6 * (fa + 4 * fc + fb);
    S2 = (b - a) / 12 * (fa + 4 * fd + 2 * fc + 4 * fe + fb);

    % Pasul 4: daca diferenta e mica => acceptam, altfel impartim si recursivam
    %Practic daca 2 subdiviziuni au rezultatul fuziunii lor ne putem oprii
    if abs(S2 - S1) < 15 * tol
        I = S2 + (S2 - S1) / 15;  % corectie Richardson
    else
        I = subdivide_simpson(f, a, c, tol/2, fa, fd, fc) + ...
            subdivide_simpson(f, c, b, tol/2, fc, fe, fb);
    end
end