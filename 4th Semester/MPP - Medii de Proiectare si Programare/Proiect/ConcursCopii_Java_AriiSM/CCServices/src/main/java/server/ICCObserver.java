package server;

import client.Domain_Simplu.Inscriere;

public interface ICCObserver {
    void inscriereReceives(Inscriere inscriere) throws CCException;
}
