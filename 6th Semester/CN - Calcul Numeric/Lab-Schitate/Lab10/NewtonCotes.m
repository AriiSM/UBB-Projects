function  [noduri, coeficienti] = NewtonCotes(grad, a, b)
    [noduri, coeficienti] = newton_cotes_inchis_custom(grad, a, b);
end


function [noduri, coeficienti] = newton_cotes_inchis_custom(grad, a, b)
% INPUT:
%   grad - gradul formulei (ex: 1 pentru trapez, 2 pentru Simpson etc.)
%   a, b - capetele intervalului de integrare
%
% OUTPUT:
%   noduri       - vector cu nodurile x_i ∈ [a, b]
%   coeficienti  - coeficienți w_i corespunzători fiecărui nod
%
% Metoda:
%   Se construiește baza Lagrange și se integrează simbolic fiecare L_i(x)

    noduri = linspace(a, b, grad + 1);          % generare noduri echidistante
    coeficienti = zeros(1, grad + 1);           % initializare vector de coeficienti

    for i = 1:(grad + 1)
        % Initializam polinomul L_i(x) = 1
        Li = 1;

        % Construim produsul L_i(x) = ∏_{j≠i} (x - xj)/(xi - xj)
        for j = 1:(grad + 1)
            if j ~= i
                Li = conv(Li, [1, -noduri(j)]) / (noduri(i) - noduri(j));
            end
        end

        % Integram L_i(x) pe [a, b]
        Li_int = polyint(Li);  % primitiva simbolica
        coeficienti(i) = polyval(Li_int, b) - polyval(Li_int, a);  % ∫_a^b L_i(x) dx
    end
end