function [spline_vals, spline_pieces, err_max] = SplineCubicDeriv2(f, x_noduri, x_eval)
    [spline_vals, spline_pieces, err_max] = aplicaSplineDeriv2(f, x_noduri, x_eval);
end




function coef = spline_deriv2(x, y, m1, mn)
% INPUT:
%   x  - vectorul nodurilor (ordonate strict crescator)
%   y  - valorile functiei f(x) in noduri
%   m1 - derivata a doua in capatul stang (f''(x_0))
%   mn - derivata a doua in capatul drept (f''(x_n))
%
% OUTPUT:
%   coef - matrice cu coeficientii [a, b, c, d] pentru fiecare polinom cubic

    n = length(x);         % numar de noduri
    h = diff(x);           % lungimile subintervalelor

    % Initializam sistemul liniar A * c = rhs
    A = zeros(n);          % matrice tridiagonala (n x n)
    rhs = zeros(n,1);      % termenii liberi

    % Impunem derivata a doua la capete
    A(1,1) = 1; rhs(1) = m1;
    A(n,n) = 1; rhs(n) = mn;

    % Completam ecuatiile din interior (relatia standard pentru spline cubic)
    for i = 2:n-1
        A(i,i-1) = h(i-1);
        A(i,i)   = 2*(h(i-1) + h(i));
        A(i,i+1) = h(i);
        rhs(i) = 3*((y(i+1)-y(i))/h(i) - (y(i)-y(i-1))/h(i-1));
    end

    % Rezolvam sistemul tridiagonal => obtinem vectorul c (f''/2)
    c = A \ rhs;

    % Calculam coeficientii b si d pentru fiecare interval
    a = y(1:end-1);
    b = zeros(n-1,1);
    d = zeros(n-1,1);
    for i = 1:n-1
        b(i) = (y(i+1)-y(i))/h(i) - h(i)*(2*c(i)+c(i+1))/3;
        d(i) = (c(i+1)-c(i))/(3*h(i));
    end

    % Grupam coeficientii pe coloane: [a b c d]
    coef = [a(:), b, c(1:end-1), d];
end






function ok = esteDerivataA2LaCapete(f, x, tol)
% INPUT:
%   f   - functie anonima f(x)
%   x   - vectorul de noduri (folosim x(1) si x(end))
%   tol - toleranta relativa (default: 1e-2)
%
% OUTPUT:
%   ok  - true daca f'' este stabil numeric in capete

    if nargin < 3
        tol = 1e-1;
    end

    h = 1e-5;

    % Aproximam f''(x) la capete folosind formula centrata
    fpp_start = (f(x(1)+h) - 2*f(x(1)) + f(x(1)-h)) / h^2;
    fpp_end   = (f(x(end)+h) - 2*f(x(end)) + f(x(end)-h)) / h^2;

    % Verificam ca valorile sa fie finite
    if ~isfinite(fpp_start) || ~isfinite(fpp_end)
        ok = false;
        return;
    end

    %Calculam eroare relativa intre derivarile din capete
    medie = 0.5 * (abs(fpp_start) + abs(fpp_end));
    if medie < 1e-8
        diff_rel = abs(fpp_start - fpp_end);  % evitare instabilitate
    else
        diff_rel = abs(fpp_start - fpp_end) / medie;  % eroare relativa
    end

    % Returnam true doar daca diferenta este sub toleranta
    if diff_rel < tol
        ok = true;
    else
        ok = false;
    end
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






function [spline_vals, spline_pieces, err_max] = aplicaSplineDeriv2(f, x_noduri, x_eval)
% APLICASPLINEDERIV2 - Calculeaza si evalueaza spline-ul cu derivata a doua impusa
%
% INPUT:
%   f         - functie de interpolat (anonima)
%   x_noduri  - noduri unde construim spline-ul
%   x_eval    - puncte in care evaluam spline-ul
%
% OUTPUT:
%   spline_vals   - valorile spline-ului in punctele x_eval
%   spline_pieces - celula cu expresii simbolice ale polinoamelor
%   err_max       - eroarea maxima fata de functia f(x)

    % 1. Verificam daca derivata a doua este stabila numeric la capete
    if ~esteDerivataA2LaCapete(f, x_noduri)
        warning('Derivata a doua in capete nu pare stabila numeric.');
    end

    % 2. Aproximam derivata a doua in capete
    h_ = 1e-6;
    m1 = (f(x_noduri(1)+h_) - 2*f(x_noduri(1)) + f(x_noduri(1)-h_)) / h_^2;
    mn = (f(x_noduri(end)+h_) - 2*f(x_noduri(end)) + f(x_noduri(end)-h_)) / h_^2;

    % 3. Evaluam functia in noduri
    y = f(x_noduri);

    % 4. Construim spline-ul
    coef = spline_deriv2(x_noduri, y, m1, mn);

    % 5. Evaluam spline-ul in punctele dorite
    spline_vals = eval_spline_C(x_noduri, coef, x_eval);

    % 6. Generam polinoamele simbolice pe fiecare interval
    syms x
    spline_pieces = cell(1, length(x_noduri)-1);
    for i = 1:length(spline_pieces)
        dx = x - x_noduri(i);
        S = coef(i,1) + coef(i,2)*dx + coef(i,3)*dx^2 + coef(i,4)*dx^3;
        spline_pieces{i} = simplify(S);
    end

    % 7. Calculam eroarea maxima de aproximare
    err_max = max(abs(f(x_eval) - spline_vals));
end