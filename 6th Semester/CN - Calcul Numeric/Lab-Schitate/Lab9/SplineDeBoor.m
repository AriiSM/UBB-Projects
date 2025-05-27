
function [xx, yy, spline_x, spline_y, err_max] = SplineDeBoor(f, a, b, n_noduri, n_eval)
    [xx, yy, spline_x, spline_y, err_max] = aplicaSplineBoor(f, a, b, n_noduri, n_eval);
end


function [pp_x, pp_y] = calculeaza_coeficienti_spline(t, x, y)
% CALCULEAZA_COEFICIENTI_SPLINE - Calculeaza spline cubice x(t), y(t)
%
% INPUT:
%   t - parametrizare (de obicei uniforma)
%   x - valori x corespunzatoare
%   y - valori y corespunzatoare
%
% OUTPUT:
%   pp_x - spline cubica pentru x(t)
%   pp_y - spline cubica pentru y(t)

    pp_x = spapi(4, t, x);  % spline cubic de ordin 4 (cubic spline)
    pp_y = spapi(4, t, y);
end






function [xx, yy] = evalueaza_spline(pp_x, pp_y, t_start, t_end, n_puncte)
% EVALUEAZA_SPLINE - Evalueaza spline-urile x(t), y(t) pentru valori de t
%
% INPUT:
%   pp_x, pp_y  - spline-uri x(t), y(t)
%   t_start/end - capetele parametrizarii (de regula 0 si 1)
%   n_puncte    - cate puncte sa generam
%
% OUTPUT:
%   xx, yy - valorile evaluate x(t), y(t)

    tt = linspace(t_start, t_end, n_puncte);
    xx = fnval(pp_x, tt);
    yy = fnval(pp_y, tt);
end





function [xx, yy, spline_x, spline_y, err_max] = aplicaSplineBoor(f, a, b, n_noduri, n_eval)
% APLICASPLINEBOOR - Aplica spline parametric de Boor pentru o functie f(x)
%
% INPUT:
%   f         - functie anonima f(x)
%   a, b      - capetele intervalului
%   n_noduri  - cate puncte de interpolare
%   n_eval    - cate puncte vrem sa evaluam pe spline
%
% OUTPUT:
%   xx, yy     - valori x si y ale spline-ului in punctele evaluate
%   spline_x/y - obiecte spline pentru x(t) si y(t)
%   err_max    - eroare maxima fata de functia f(x)

    % Noduri si valori
    x_noduri = linspace(a, b, n_noduri);       % valori x
    y_noduri = f(x_noduri);                    % f(x) -> y

    % Parametrizare uniforma
    t = linspace(0, 1, n_noduri);

    % Construim spline-urile pentru x(t) si y(t)
    [spline_x, spline_y] = calculeaza_coeficienti_spline(t, x_noduri, y_noduri);

    % Evaluam spline-urile in n puncte
    [xx, yy] = evalueaza_spline(spline_x, spline_y, 0, 1, n_eval);

    % Calculam eroarea fata de y = f(x)
    yy_true = f(xx);
    err_max = max(abs(yy - yy_true));
end