function [p, n] = approx_succ(g, p0, epsilon, Nmax)
    % Metoda aproximatiilor succesive
    for n = 1:Nmax
        % P1: Calculam urmatoarea aproximatie p = g(p0)
        p = g(p0);

        % P2: Verificam criteriul de oprire |p - p0| < epsilon
        if abs(p - p0) < epsilon
            return;  % P3: Succes, conditia de oprire e indeplinita
        end

        % P4: Pregatim urmatoarea iteratie
        p0 = p;
    end

    % P5: Daca am ajuns aici, nu s-a atins precizia in Nmax pasi
    error('Precizia nu a fost atinsa in %d pasi', Nmax);
end 