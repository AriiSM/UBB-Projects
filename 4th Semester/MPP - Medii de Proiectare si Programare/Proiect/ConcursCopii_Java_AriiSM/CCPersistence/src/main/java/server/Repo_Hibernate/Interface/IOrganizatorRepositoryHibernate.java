package server.Repo_Hibernate.Interface;


import server.ICrudRepository;

public interface IOrganizatorRepositoryHibernate extends ICrudRepository<Integer, client.Domain_Hibernate.Organizator> {
    client.Domain_Hibernate.Organizator findAccount(String parola, String nume, String prenume);
}
