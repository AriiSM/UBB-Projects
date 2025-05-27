function [noduri, coeficienti] = gauss_legendre(n)
%   Input:
%       n - numarul de noduri (puncte de evaluare)
%
%   Output:
%       nodes     - pozitiile nodurilor (radacinile polinomului Legendre)
%       weights   - coeficientii de integrare (ponderele asociate)

    % Initializam coeficientii pentru matricea tridiagonala (Jacobi)
    alpha = zeros(n, 1);
    beta = zeros(n-1, 1);

    % Calculam coeficientii beta conform formulei standard pentru Legendre
    for k = 1:n-1
        beta(k) = 1 / sqrt(4 - 1/(k^2));
    end

    % Construim matricea Jacobi tridiagonala simetrica
    J = diag(alpha) + diag(beta, 1) + diag(beta, -1);

    % Calculam valorile proprii (nodes) si vectorii proprii
    [V, D] = eig(J);
    noduri = diag(D);       

    % Sortam nodurile si ajustam vectorii proprii corespunzatori
    [noduri, ord] = sort(noduri);
    V = V(:, ord);

    % Formula coeficientilor (pondere) pentru Gauss-Legendre:
    coeficienti = 2 * V(1,:).^2;  
end