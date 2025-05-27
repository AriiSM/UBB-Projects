function integral = FormulaSimpsonSimplu(f, a, b, divizari)
    integral = integrare_simpson(f, a, b, divizari);
end



function integral = integrare_simpson(f, a, b, divizari)
    if rem(divizari, 2) ~= 0
        error('divizari trebuie sa fie par pentru regula Simpson.');
    end

    pas = (b - a) / divizari; % pasul intre noduri
    puncte = a + (0:divizari) * pas; % nodurile x0, x1, ..., xn
    valori = f(puncte); % evaluarea functiei in fiecare nod

    % Coeficienții:
    coef4 = sum(valori(2:2:end-1));
    coef2 = sum(valori(3:2:end-2));

    % Formula Simpson compusa:
    integral = pas/3 * (valori(1) + 4 * coef4 + 2 * coef2 + valori(end));
end