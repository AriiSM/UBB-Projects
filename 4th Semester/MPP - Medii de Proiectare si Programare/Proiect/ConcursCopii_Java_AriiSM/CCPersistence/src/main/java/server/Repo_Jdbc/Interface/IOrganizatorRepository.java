package server.Repo_Jdbc.Interface;

import client.Domain_Simplu.Organizator;
import server.ICrudRepository;

public interface IOrganizatorRepository extends ICrudRepository<Integer, Organizator> {
    Organizator findAccount(String parola, String nume, String prenume);
}
