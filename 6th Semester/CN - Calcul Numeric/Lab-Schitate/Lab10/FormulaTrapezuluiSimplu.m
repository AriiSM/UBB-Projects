
function integral = FormulaTrapezuluiSimplu(f, a, b, divizari)
    integral = integrare_trapeze(f, a, b, divizari);
end


function integral = integrare_trapeze(f, a, b, divizari)

    pas = (b - a) / divizari; % pasul intre noduri
    puncte = linspace(a, b, divizari + 1); % genereaza nodurile
    valori = arrayfun(f, puncte); % evalueaza functia in fiecare nod

    % Aplica formula trapezului:
    integral = pas * (sum(valori) - 0.5 * (valori(1) + valori(end)));
end