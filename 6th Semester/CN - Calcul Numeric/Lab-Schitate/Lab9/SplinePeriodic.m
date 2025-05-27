function [spline_vals, spline_pieces, max_err] = SplinePeriodic(f, x_noduri, x_eval)
    [spline_vals, spline_pieces, max_err] = aplicaSplinePeriodic(f, x_noduri, x_eval);
end




function coef = spline_periodic(x, y)
% SPLINE_PERIODIC - Calculeaza coeficientii spline-ului cubic periodic
%
% INPUT:
%   x - vectorul nodurilor (strict crescator): [x0, x1, ..., xn]
%   y - valorile functiei f(x) in noduri:      [y0, y1, ..., yn]
%
% OUTPUT:
%   coef - matrice (n-1) x 4 cu coeficientii spline-ului pe fiecare interval [xi, xi+1]:
%          fiecare linie: [a_i, b_i, c_i, d_i] corespunzand:
%          S_i(x) = a + b*(x - x_i) + c*(x - x_i)^2 + d*(x - x_i)^3

    n = length(x);           % numarul de noduri
    h = diff(x);             % lungimea fiecarui interval [x(i+1) - x(i)]

    % Initializam matricea A (tridiagonala cu elemente ciclice) si vectorul termenilor liberi
    A = zeros(n);           
    rhs = zeros(n, 1);      

    % ----------- CONSTRUCTIA SISTEMULUI LINEAR ------------

    % 1. Prima ecuatie (conditia de periodicitate in capatul stang)
    A(1,1)   = 2*(h(1)+h(end));   % diagonala principala
    A(1,2)   = h(1);              % urmatorul element
    A(1,end) = h(end);            % legatura ciclica cu ultimul nod
    rhs(1)   = 3*((y(2)-y(1))/h(1) - (y(end)-y(end-1))/h(end));  % derivata egala la capete

    % 2. Ecuatiile obisnuite pentru nodurile interioare (i = 2,...,n-1)
    for i = 2:n-1
        A(i,i-1) = h(i-1);                            % subdiagonala
        A(i,i)   = 2*(h(i-1) + h(i));                 % diagonala principala
        A(i,i+1) = h(i);                              % supradiagonala
        rhs(i)   = 3*((y(i+1)-y(i))/h(i) - (y(i)-y(i-1))/h(i-1));  % termenul liber
    end

    % 3. Ultima ecuatie: impune c(1) = c(n) => periodicitate a derivatelor a doua
    A(end,1)   = 1;
    A(end,end) = -1;
    rhs(end)   = 0;

    % -------------- REZOLVARE SISTEM --------------

    % Rezolva sistemul liniar A * c = rhs
    c = A \ rhs;

    % -------------- CALCUL COEFICIENTI SPLINE --------------

    a = y(1:end-1);       % coeficientul constant (fiecare S_i incepe din y(i))
    b = zeros(n-1,1);     % coeficientul lui (x - x_i)
    d = zeros(n-1,1);     % coeficientul lui (x - x_i)^3

    % Parcurgem fiecare interval [x(i), x(i+1)] pentru a calcula coeficientii
    for i = 1:n-1
        % coef. b = panta ajustata cu derivata a doua
        b(i) = (y(i+1)-y(i))/h(i) - h(i)*(2*c(i)+c(i+1))/3;

        % coef. d = diferenta derivatelor a doua, scalata
        d(i) = (c(i+1)-c(i))/(3*h(i));
    end

    % Rezultatul: coef = [a_i, b_i, c_i, d_i] pentru fiecare polinom
    coef = [a(:), b, c(1:end-1), d(:)];
end







function este = estePeriodica(f, a, b, tol)
% INPUT:
%   f   - functie anonima f(x)
%   a,b - capetele intervalului [a, b]
%   tol - toleranta numerica (optional, default = 1e-4)
%
% OUTPUT:
%   este - boolean: true daca f si f' sunt egale in capete (cu eroare < tol)

    if nargin < 4
        tol = 1e-4;
    end

    h = 1e-5;  % pas mic pentru derivare numerica

    % Derivata prima la capete (formula centrata)
    fpa = (f(a + h) - f(a - h)) / (2*h);
    fpb = (f(b + h) - f(b - h)) / (2*h);

    % Verificam daca valorile si derivarile sunt apropiate
    este = abs(f(a) - f(b)) < tol && abs(fpa - fpb) < tol;
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





function [spline_vals, spline_pieces, err_max] = aplicaSplinePeriodic(f, x_noduri, x_eval)
% INPUT:
%   f         - functie anonima f(x)
%   x_noduri  - vector de noduri (x0 < x1 < ... < xn)
%   x_eval    - puncte unde vrem sa evaluam spline-ul
%
% OUTPUT:
%   spline_vals    - valorile spline-ului in x_eval
%   spline_pieces  - celula cu polinoamele simbolice pe intervale
%   err_max        - eroarea maxima de interpolare f - spline

    % Verificare periodicității funcției la capete
    if ~estePeriodica(f, x_noduri(1), x_noduri(end))
        warning('Functia NU este aproximativ periodica in capetele intervalului!');
    end

    % Construim vectorul y
    y = f(x_noduri);

    % Apelam functia spline_periodic pentru coeficienti
    coef = spline_periodic(x_noduri, y);

    % Evaluam spline-ul in punctele dorite
    spline_vals = eval_spline_C(x_noduri, coef, x_eval);

    % Generam formule simbolice ale spline-ului pe fiecare interval
    syms x
    spline_pieces = cell(1, length(x_noduri)-1);
    for i = 1:length(spline_pieces)
        dx = x - x_noduri(i);
        S = coef(i,1) + coef(i,2)*dx + coef(i,3)*dx^2 + coef(i,4)*dx^3;
        spline_pieces{i} = simplify(S);
    end

    % Calculam eroarea maxima comparativ cu f
    err_max = max(abs(f(x_eval) - spline_vals));
end
