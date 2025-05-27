function w_opt = calculeaza_omega_opt(A)
    % INPUT:
    %   A - matricea patratica a sistemului Ax = b
    %
    % OUTPUT:
    %   w_opt - valoarea optima estimata pentru parametrul w (relaxare)
    %           Daca metoda Jacobi nu converge (raza spectrala >= 1), se returneaza NaN

    % Extragem matricea diagonala D, partea inferioara L si partea superioara U din A
    D = diag(diag(A));      % D - matricea diagonala
    L = tril(A, -1);        % L - partea strict inferioara (sub diagonala)
    U = triu(A, 1);         % U - partea strict superioara (deasupra diagonalei)

    % Construim matricea de iteratie pentru metoda Jacobi: T = -D^(-1) * (L + U)
    T = -inv(D) * (L + U);

    % Calculam raza spectrala (modulul maxim al valorilor proprii ale lui T)
    p = max(abs(eig(T)));

    % Verificam daca metoda Jacobi converge (conditia este ca p < 1)
    if p >= 1
        % Daca raza spectrala este >= 1, metoda Jacobi nu converge
        warning("Jacobi nu converge, ω optim nu poate fi estimat.");
        w_opt = NaN;  % Returnam NaN (valoare nedefinita) ca semnal ca nu putem estima w
    else
        % Daca metoda Jacobi converge, putem folosi formula teoretica pentru w optim
        % Formula: w_opt = 2 / (1 + sqrt(1 - p^2))
        w_opt = 2 / (1 + sqrt(1 - p^2));
    end
end
