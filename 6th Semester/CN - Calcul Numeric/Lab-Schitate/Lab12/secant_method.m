function [p, i] = secant_method(f, p0, p1, epsilon, Nmax)
    % Metoda secantei conform pasilor din pseudo-cod
    %inspirata din Lab12 Metoda secantei (pg3)

    i = 2;                    % P1: initializare contor
    q0 = f(p0); 
    q1 = f(p1);

    while i <= Nmax          % P2: bucla principala
        % P3: calcul noua aproximare
        p = p1 - q1 * (p1 - p0) / (q1 - q0);

        % P4: criteriul de oprire
        if abs(p - p1) < epsilon
            return;  % Succes
        end

        i = i + 1;           % P5: incrementare

        % P6: pregatim urmatoarea iteratie
        p0 = p1;
        q0 = q1;
        p1 = p;
        q1 = f(p);
    end

    % P7: daca nu s-a atins precizia in Nmax pasi
    error('Precizia nu poate fi atinsă cu numărul dat de iterații.');
end