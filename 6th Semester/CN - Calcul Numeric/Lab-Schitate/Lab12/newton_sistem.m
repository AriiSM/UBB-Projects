function [x, n] = newton_sistem(f, J, x0, epsilon, Nmax)
    % inspirata din Lab12 Metoda lui Newton(pg5)
    x = x0;
    for n = 1:Nmax
        dx = -J(x)\f(x);     % rezolvam: J(x) * dx = -f(x)
        x = x + dx;          % x^{(n+1)} = x^{(n)} + dx
        if norm(dx) < epsilon
            return           % convergenta
        end
    end
    error('Precizia nu a fost atinsa in %d pasi', Nmax);
end