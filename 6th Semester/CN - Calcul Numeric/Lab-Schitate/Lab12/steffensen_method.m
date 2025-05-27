function [p, i] = steffensen_method(g, p0, epsilon, Nmax)
    % Metoda Steffensen conform pasilor din pseudo-cod
    %inspirata din Lab12 Metoda lui Steffensen (pg3)

    i = 1;  % P1: initializare contor

    while i <= Nmax  % P2: bucla principala
        % P3: calculam pasii iterativi g(g(...))
        p1 = g(p0);               % p1 := g(p0)
        p2 = g(p1);               % p2 := g(p1)

        % Calculam noua aproximare folosind formula Steffensen
        denom = p2 - 2*p1 + p0;
        if denom == 0
            error('Împărțire la zero.');
        end

        p = p0 - ((p1 - p0)^2) / denom;

        % P4: verificam criteriul de oprire
        if abs(p - p0) < epsilon
            return;  % Succes
        end

        i = i + 1;    % P5: incrementare
        p0 = p;       % P6: actualizare p0
    end

    % P7: nu s-a atins precizia
    error('Precizia nu poate fi atinsa cu numarul dat de iteratii.');
end