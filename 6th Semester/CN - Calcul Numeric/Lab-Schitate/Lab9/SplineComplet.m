function [spline_vals, spline_pieces, err_max] = SplineComplet(f, x_noduri, x_eval)
    [spline_vals, spline_pieces, err_max] = aplicaSplineComplet(f, x_noduri, x_eval);
end

function coef = spline_complet(x, y, fp0, fpn)
% INPUT:
%   x    - noduri (vector strict crescator)
%   y    - valori f(x) in noduri
%   fp0  - derivata f'(x_0)
%   fpn  - derivata f'(x_n)
%
% OUTPUT:
%   coef - matrice (n-1)x4 cu coeficientii [a, b, c, d] pentru fiecare interval

    n = length(x);
    h = diff(x);  % lungimile intervalelor

    % Construim sistemul tridiagonal pentru c
    A = zeros(n);      
    rhs = zeros(n,1);  

    % Capatul stang: derivata cunoscuta f'(x0) = fp0
    A(1,1) = 2*h(1); 
    A(1,2) = h(1);
    rhs(1) = 3*((y(2)-y(1))/h(1) - fp0);

    % Capatul drept: derivata cunoscuta f'(xn) = fpn
    A(n,n-1) = h(end);
    A(n,n) = 2*h(end);
    rhs(n) = 3*(fpn - (y(end)-y(end-1))/h(end));

    % Ecuatii interioare
    for i = 2:n-1
        A(i,i-1) = h(i-1);
        A(i,i)   = 2*(h(i-1) + h(i));
        A(i,i+1) = h(i);
        rhs(i) = 3*((y(i+1)-y(i))/h(i) - (y(i)-y(i-1))/h(i-1));
    end

    % Rezolvam sistemul: obtinem c_i = S''(x_i)/2
    c = A \ rhs;

    % Calculam coeficientii spline-ului pe fiecare interval
    a = y(1:end-1);
    b = zeros(n-1,1);
    d = zeros(n-1,1);

    for i = 1:n-1
        b(i) = (y(i+1)-y(i))/h(i) - h(i)*(2*c(i)+c(i+1))/3;
        d(i) = (c(i+1)-c(i))/(3*h(i));
    end

    % Returnam coeficientii: coloane [a b c d]
    coef = [a(:), b(:), c(1:end-1), d(:)];
end




function values = eval_spline_C(x, coef, x_eval)
% INPUT:
%   x       - nodurile [x0, x1, ..., xn]
%   coef    - coeficientii pe intervale (a, b, c, d)
%   x_eval  - punctele unde evaluam spline-ul
%
% OUTPUT:
%   values  - valori spline evaluate in punctele x_eval

    n = length(x);
    values = zeros(size(x_eval));

    for j = 1:length(x_eval)
        % Gasim in ce interval se afla x_eval(j)
        i = find(x_eval(j) >= x(1:end-1) & x_eval(j) <= x(2:end), 1, 'first');

        % Daca e in afara domeniului, alegem cel mai apropiat interval
        if isempty(i)
            i = (x_eval(j) < x(1)) * 1 + (x_eval(j) > x(end)) * (n - 1);
        end

        dx = x_eval(j) - x(i);
        a = coef(i,1); b = coef(i,2);
        c = coef(i,3); d = coef(i,4);

        values(j) = a + b*dx + c*dx^2 + d*dx^3;
    end
end





function este_derivabila = esteDerivabilaLaCapete(f, x, tol)
    % Verifica daca derivata la capete poate fi aproximata stabil
    h = 1e-5;
    d_st0 = (f(x(1)) - f(x(1) - h)) / h;
    d_dr0 = (f(x(1) + h) - f(x(1))) / h;

    d_stn = (f(x(end)) - f(x(end) - h)) / h;
    d_drn = (f(x(end) + h) - f(x(end))) / h;

    este_derivabila = abs(d_st0 - d_dr0) < tol && abs(d_stn - d_drn) < tol;
end







function [spline_vals, spline_pieces, err_max] = aplicaSplineComplet(f, x_noduri, x_eval)
% APLICASPLINECOMPLET - Aplica spline complet cu derivata aproximata numeric
%
% INPUT:
%   f         - functie anonima (f(x))
%   x_noduri  - noduri de interpolare (crescator)
%   x_eval    - puncte pentru evaluare
%
% OUTPUT:
%   spline_vals   - valori spline evaluate in x_eval
%   spline_pieces - celula cu expresii simbolice pe fiecare interval
%   err_max       - eroare maxima fata de f(x)

    if ~esteDerivabilaLaCapete(f, x_noduri, 1e-3)
        warning(['Functia nu este derivabila numeric la capete.\n' ...
                 'Spline complet NU este recomandat. Foloseste spline natural.']);
    end

    % Derivata aproximata la capete (formula centrata)
    h = 1e-5;
    fp0 = (f(x_noduri(1) + h) - f(x_noduri(1) - h)) / (2*h);
    fpn = (f(x_noduri(end) + h) - f(x_noduri(end) - h)) / (2*h);

    y = f(x_noduri);
    coef = spline_complet(x_noduri, y, fp0, fpn);
    spline_vals = eval_spline_C(x_noduri, coef, x_eval);

    % Polinoame simbolice
    syms x
    spline_pieces = cell(1, length(x_noduri)-1);
    for i = 1:length(spline_pieces)
        dx = x - x_noduri(i);
        S = coef(i,1) + coef(i,2)*dx + coef(i,3)*dx^2 + coef(i,4)*dx^3;
        spline_pieces{i} = simplify(S);
    end

    % Eroare
    err_max = max(abs(f(x_eval) - spline_vals));
end