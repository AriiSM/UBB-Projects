function [a, phi] = mmp_continuu(f, baza, a_int, b_int)
% INPUT:
%   f       – functia de aproximat (ex: f(x) = sin(x))
%   baza    – celula cu functii de baza (ex: {@(x) 1, @(x) x, @(x) x.^2})
%   a_int   – capatul stang al intervalului de aproximare
%   b_int   – capatul drept al intervalului de aproximare
%
% OUTPUT:
%   a       – vectorul coeficientilor (solutia sistemului normal)
%   phi     – functia aproximanta, definita pe [a_int, b_int]

    n = length(baza);      % numarul de functii baza utilizate (grad + 1)
    G = zeros(n);          % matricea Gram (G(i,j) = <phi_i, phi_j>)
    b_vec = zeros(n, 1);   % vectorul din dreapta sistemului normal

    % Construim sistemul normal G * a = b
    for i = 1:n
        for j = 1:n
            % Produs scalar continuu <phi_i, phi_j> = integral pe [a,b]
            G(i, j) = integral(@(x) baza{i}(x) .* baza{j}(x), a_int, b_int);
        end
        % Calculam <f, phi_i> = integral(f(x) * phi_i(x)) pe [a,b]
        b_vec(i) = integral(@(x) f(x) .* baza{i}(x), a_int, b_int);
    end

    % Rezolvam sistemul normal G * a = b pentru coeficientii a
    a = G \ b_vec;

    % Construim functia aproximanta phi(x) = a1*phi1(x) + a2*phi2(x) + ...
    phi = @(x) sum( ...
        cell2mat(arrayfun(@(k) a(k) * baza{k}(x), 1:n, 'UniformOutput', false)) ...
    );
end
