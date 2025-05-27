function [x_noduri, y_noduri, w, x_eval, p_eval, p_expr] = BaricentricaChebyshev(f, a, b, n, tip)
    [x_noduri, y_noduri, w, x_eval, p_eval, p_expr] = runInterpolareBaricentrica(f, a, b, n, tip);
end



function x = genereazaNoduriChebyshev(n, a, b, tip)
% INPUT:
%   n   - numarul de noduri
%   a,b - capetele intervalului
%   tip - 'I' pentru Chebyshev de tip I, 'II' pentru tip II
%
% OUTPUT:
%   x   - vector cu nodurile generate

    switch tip
        case 'I'
            % Noduri Chebyshev de ordinul I (nu includ capetele intervalului)
            j = 0:n-1;
            x = cos((2*j + 1)*pi/(2*n));
        case 'II'
            % Noduri Chebyshev de ordinul II (includ capetele intervalului)
            j = 0:n-1;
            x = cos(j*pi/(n-1));
        otherwise
            error('Tip invalid. Foloseste ''I'' sau ''II''.');
    end

    % Reproiectare a nodurilor din intervalul [-1, 1] in [a, b]
    x = ((b - a)/2) * x + (a + b)/2;
end


function w = calculeazaPonderiBaricentrice(x_noduri)
% CALCULEAZAPONDERIBARICENTRICE - Calculeaza ponderile baricentrice pentru interpolare
%
% INPUT:
%   x_noduri - vector cu nodurile de interpolare
%
% OUTPUT:
%   w        - vector cu ponderile baricentrice

    n = length(x_noduri);  % numarul de noduri
    w = ones(1, n);         % initializare ponderi cu 1

    % Calculul fiecarei ponderi w(j)
    for j = 1:n
        for k = [1:j-1, j+1:n]  % evitam cazul k = j
            w(j) = w(j) / (x_noduri(j) - x_noduri(k));  % produsul inverselor distantelor
        end
    end
end

function [p, p_expr] = interpolareBaricentricaExtinsa(x_noduri, y_noduri, w, x_eval)
% INTERPOLAREBARICENTRICAEXTINSA - Interpolare baricentrica + forma simbolica + coeficienti
%
% INPUT:
%   x_noduri  - nodurile de interpolare (vector)
%   y_noduri  - valorile functiei in noduri
%   w         - ponderile baricentrice calculate anterior
%   x_eval    - punctele in care vrem sa evaluam interpolantul
%
% OUTPUT:
%   p         - vectorul valorilor interpolate
%   p_expr    - polinomul exprimat simbolic (obtinere cu Lagrange clasic)

    % Initializam vectorul de rezultate p cu zerouri
    p = zeros(size(x_eval));

    % Evaluam interpolarea baricentrica in fiecare punct din x_eval
    for k = 1:length(x_eval)
        x = x_eval(k);  % punctul curent

        % Daca x coincide exact cu un nod de interpolare, returnam valoarea functiei
        idx = find(abs(x - x_noduri) < 1e-14, 1);
        if ~isempty(idx)
            p(k) = y_noduri(idx);
            continue;
        end

        % Calculam termenii baricentrici
        termen = w ./ (x - x_noduri);
        % Evaluam valoarea interpolata ca raport de sume
        p(k) = sum(termen .* y_noduri) / sum(termen);
    end

    % ==== CONSTRUCTIA POLINOMULUI SIMBOLIC (LAGRANGE CLASIC) ====

    syms x;             % definim simbolul x
    n = length(x_noduri); 
    p_expr = 0;         % initializam expresia polinomului cu 0

    % Construim fiecare baza de interpolare L_j(x)
    for j = 1:n
        Lj = 1;  % baza de interpolare pentru nodul j

        % Produsul celorlalti termeni pentru L_j(x)
        for k = [1:j-1, j+1:n]
            Lj = Lj * (x - x_noduri(k)) / (x_noduri(j) - x_noduri(k));
        end

        % Adaugam termenul y_j * L_j(x) in polinomul final
        p_expr = p_expr + y_noduri(j) * Lj;
    end

    % Expandam expresia simbolica pentru a obtine forma dezvoltata
    p_expr = expand(p_expr);
end



function [x_noduri, y_noduri, w, x_eval, p_eval, p_expr] = runInterpolareBaricentrica(f, a, b, n, tip)
% RUNINTERPOLAREBARICENTRICA - Automatizeaza interpolarea baricentrica cu Chebyshev
%
% INPUT:
%   f    - functie anonima (ex: @(x) sin(x))
%   a,b  - capetele intervalului
%   n    - numarul de noduri Chebyshev
%   tip  - 'I' sau 'II' (tipul nodurilor Chebyshev)
%
% OUTPUT:
%   x_noduri - nodurile Chebyshev de tipul ales
%   y_noduri - valorile functiei f in noduri
%   w        - ponderile baricentrice
%   x_eval   - puncte dense pentru evaluare (plot)
%   p_eval   - valori interpolate f(x_eval)
%   p_expr   - forma simbolica a polinomului interpolator

    % 1. Noduri Chebyshev de tip specificat
    x_noduri = genereazaNoduriChebyshev(n, a, b, tip);

    % 2. Evaluari
    y_noduri = f(x_noduri);
    w = calculeazaPonderiBaricentrice(x_noduri);
    x_eval = linspace(a, b, 500);

    % 3. Interpolare
    [p_eval, p_expr] = interpolareBaricentricaExtinsa(x_noduri, y_noduri, w, x_eval);
end

