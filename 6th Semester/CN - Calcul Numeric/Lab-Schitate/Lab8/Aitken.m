function [p_val, p_exact, err_abs, xx, yy] = Aitken(f, x, x_eval)
    [p_val, p_exact, err_abs, xx, yy] = runAitkenInterpolation(f, x, x_eval);
end


function val = evalAitken(x, y, x_eval)
% IMPLEMENTARE AITKEN-NEVILLE – calcul iterativ al valorii polinomului
%
% INPUT:
%   x       - vectorul nodurilor [x0, x1, ..., xn]
%   y       - valorile f(xi) în aceste noduri
%   x_eval  - punctul în care vrem să evaluăm interpolarea
%
% OUTPUT:
%   val     - valoarea aproximată a polinomului interpolator în x_eval

    n = length(x);                % numărul de noduri
    P = zeros(n,n);               % tabel triunghiular pentru recurență
    P(:,1) = y(:);                % prima coloană: valorile funcției
    
    % Recursie pe coloane (j), pe măsură ce construim niveluri mai adânci
    for j = 2:n
        for i = j:n
            % Formula Aitken-Neville:
            P(i,j) = ((x_eval - x(i-j+1)) * P(i,j-1) - ...
                      (x_eval - x(i))     * P(i-1,j-1)) / ...
                      (x(i) - x(i-j+1));
        end
    end
    
    val = P(n,n);  % valoarea interpolată finală se află în colțul din dreapta-sus
end



function [p_val, p_exact, err_abs, xx, yy] = runAitkenInterpolation(f, x, x_eval)
% INTERPOLARE AITKEN - calculeaza f(x_eval) prin schema Aitken-Neville
% INPUT:
%   f      - functie anonima (e.g., @(x) sin(x))
%   x      - vector cu noduri
%   x_eval - punct (sau vector) de evaluat
% OUTPUT:
%   p_val     - valoare interpolata in x_eval
%   p_exact   - valoarea reala f(x_eval)
%   err_abs   - eroarea absoluta
%   xx, yy    - vectori pentru grafic (optional)

    y = f(x);

    % 1. Evaluare în punct(e)
    p_val = evalAitken(x, y, x_eval);

    % 2. Evaluare exactă și eroare
    p_exact = f(x_eval);
    err_abs = abs(p_val - p_exact);

    % 3. Pentru grafic
    xx = linspace(min(x), max(x), 400);
    yy = arrayfun(@(xe) evalAitken(x, y, xe), xx);  % <-- FIXED aici
end

