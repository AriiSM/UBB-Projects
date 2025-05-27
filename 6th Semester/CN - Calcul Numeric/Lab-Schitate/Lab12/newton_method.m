function [p, n] = newton_method(f, df, p0, epsilon, Nmax)
    % Rezolvare conform pasilor din pseudo-cod
    % inspirata din Lab12 Metoda Newton-Raphson(pg2)

    i = 1;  % P1: initializare iterator
    while i <= Nmax  % P2: bucla principala
        p = p0 - f(p0)/df(p0);  % P3: pasul Newton
        
        if abs(p - p0) < epsilon  % P4: criteriul de oprire
            n = i;
            return;  % Succes: p este solutia aproximativa
        end

        i = i + 1;   % P5: incrementare
        p0 = p;      % P6: actualizare p0
    end

    % P7: daca nu s-a atins precizia in Nmax pasi
    error('Precizia nu poate fi atinsa cu numarul dat de iteratii.');
end