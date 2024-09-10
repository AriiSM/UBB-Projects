package server.Repo_REST.Interface;

import client.Domain_REST.Organizator;
import server.ICrudRepository;

public interface IOrganizatorRepository extends ICrudRepository<Integer, Organizator> {
    Organizator findAccount(String parola, String nume, String prenume);
}
