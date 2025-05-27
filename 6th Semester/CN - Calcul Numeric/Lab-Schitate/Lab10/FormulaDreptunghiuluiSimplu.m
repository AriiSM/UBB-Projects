function integral = FormulaDreptunghiuluiSimplu(f, a, b, divizari)
    integral = integrare_dreptunghiuri(f, a, b, divizari);
end



function integral = integrare_dreptunghiuri(f, a, b, divizari)
    pas = (b - a) / divizari; % lungimea fiecarui dreptunghi
    mijloace = a + pas * (0.5 : 1 : divizari - 0.5); % vector cu puncte de mijloc

    evaluari = arrayfun(f, mijloace); % evalueaza functia in punctele de mijloc

    integral = pas * sum(evaluari); % suma ariilor dreptunghiurilor
end