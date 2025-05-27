function [a, phi] = mmp_discret(x, y, baza)
% INPUT:
%   x, y  – vectori de date (m puncte)
%   baza  – celula de functii (ex: {@(x) 1, @(x) x, @(x) x.^2})
%
% OUTPUT:
%   a     – vectorul coeficientilor (solutia sistemului normal)
%   phi   – functia aproximanta (phi(t) ≈ f(t))

    n = length(baza);  % numarul de functii de baza utilizate in aproximare

    % Initializam matricea Gram G si vectorul b din sistemul normal
    G = zeros(n);         % matrice patratica de dimensiune n x n
    b_vec = zeros(n, 1);  % vector coloana de dimensiune n x 1

    % Construim sistemul normal: G * a = b
    for i = 1:n
        for j = 1:n
            % Calculam produsul scalar discret <phi_i, phi_j>
            % adica suma f_i(x_k) * f_j(x_k) pentru toate punctele x_k
            G(i, j) = sum(baza{i}(x) .* baza{j}(x));
        end
        % Calculam termenul din dreapta <y, phi_i> = suma y_k * f_i(x_k)
        b_vec(i) = sum(y .* baza{i}(x));
    end

    % Rezolvam sistemul liniar G * a = b pentru a obtine coeficientii
    a = G \ b_vec;

    % Definim functia aproximanta phi(t) = a1*phi1(t) + a2*phi2(t) + ...
    phi = @(t) sum( ...
        cell2mat(arrayfun(@(k) a(k) * baza{k}(t), 1:n, 'UniformOutput', false)) ...
    );
end
