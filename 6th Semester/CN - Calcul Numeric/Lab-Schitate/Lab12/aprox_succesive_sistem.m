function [x, n] = aprox_succesive_sistem(f, J, x0, epsilon, Nmax)
 % inspirata din Lab12 aproximatiilor succesive(pg5)

    Lambda = -inv(J(x0));     % Lambda fixa, calculata la inceput
    x = x0;
    for n = 1:Nmax
        x_nou = x + Lambda * f(x);   % x_{n+1} = x + Lambda*f(x)
        if norm(x_nou - x) < epsilon
            x = x_nou;
            return
        end
        x = x_nou;
    end
    error('Precizia nu a fost atinsa in %d pasi', Nmax);
end