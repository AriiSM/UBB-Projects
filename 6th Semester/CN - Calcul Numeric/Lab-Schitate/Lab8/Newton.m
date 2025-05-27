function [p_val, p_exact, err_abs, xx, yy, coef] = Newton(f, x, x_eval)
    [p_val, p_exact, err_abs, xx, yy, coef] = runNewtonInterpolation(f, x, x_eval);
end

function val = evalNewton(x, coef, xe)
% EVALUAREA unui polinom Newton in forma eficienta (Horner)
%
% INPUT:
%   x     - vectorul nodurilor
%   coef  - coeficientii Newton (deja calculati)
%   xe    - punctul unde vrem sa evaluam polinomul
%
% OUTPUT:
%   val   - valoarea aproximata a polinomului în xe

    n = length(x);
    val = coef(n);  % pornim cu ultimul coeficient
    for j = n-1:-1:1
        val = val * (xe - x(j)) + coef(j);  % Horner adaptat la forma Newton
    end
end

function [p_val, p_exact, err_abs, xx, yy, coef] = runNewtonInterpolation(f, x, x_eval)
% INPUT:
%   f       - functie anonima (ex: @(x) sin(x))
%   x       - vector cu noduri de interpolare (distincte)
%   x_eval  - punct sau vector de puncte unde evaluam polinomul
%
% OUTPUT:
%   p_val    - valoarea polinomului Newton în x_eval
%   p_exact  - valoarea reala f(x_eval)
%   err_abs  - eroarea absoluta |f(x_eval) - p_val|
%   xx       - vector de puncte pentru plot (400 valori)
%   yy       - valorile polinomului în punctele xx
%   coef     - coeficientii polinomului în forma Newton    

  % 1. Evaluam functia în noduri
    y = f(x);
    n = length(x);
    coef = y;  % inițializare coeficienți Newton
    
    % 2. Construim tabelul diferențelor divizate (triunghi superior)
    for j = 2:n
        coef(j:n) = (coef(j:n) - coef(j-1:n-1)) ./ (x(j:n) - x(1:n-j+1));
    end
    
    % 3. Evaluam polinomul în x_eval folosind Horner pentru forma Newton
    p_val = arrayfun(@(xe) evalNewton(x, coef, xe), x_eval);
    
    % 4. Comparam cu f(x_eval)
    p_exact = f(x_eval);
    err_abs = abs(p_val - p_exact);
    
    % 5. Generam date pentru grafic
    xx = linspace(min(x), max(x), 400);
    yy = arrayfun(@(xe) evalNewton(x, coef, xe), xx);
end