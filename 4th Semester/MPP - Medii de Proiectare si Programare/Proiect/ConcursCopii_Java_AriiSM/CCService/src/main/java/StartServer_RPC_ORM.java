import server.ICCServices;
import server.Repo_Hibernate.ConcursDBRepository_Hibernate;
import server.Repo_Hibernate.InscriereDBRepository_Hibernate;
import server.Repo_Hibernate.OrganizatorDBRepository_Hibernate;
import server.Repo_Hibernate.ParticipantDBRepository_Hibernate;
import server.ServiceImplementationHibernate;
import server.utils.AbstractServer;
import server.utils.CCRpcConcurrentServer;
import server.utils.ServerException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public class StartServer_RPC_ORM {
    private static final int defaultPort = 55555;

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out));
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/chatserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }

        ParticipantDBRepository_Hibernate participantDBRepository = new ParticipantDBRepository_Hibernate();
        OrganizatorDBRepository_Hibernate organizatorDBRepository = new OrganizatorDBRepository_Hibernate();
        InscriereDBRepository_Hibernate inscriereDBRepository = new InscriereDBRepository_Hibernate();
        ConcursDBRepository_Hibernate concursDBRepository = new ConcursDBRepository_Hibernate();

        ICCServices chatServerImpl = new ServiceImplementationHibernate(participantDBRepository, organizatorDBRepository, inscriereDBRepository, concursDBRepository);

//        try{
//            organizatorDBRepository.save(new client.Domain_Hibernate.Organizator("Stan", "Ariana", "123"));
//            organizatorDBRepository.save(new client.Domain_Hibernate.Organizator("Ruse", "Teodor", "123"));
//
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_6_8, Proba.DESEN));
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_9_11, Proba.DESEN));
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_12_15, Proba.DESEN));
//
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_6_8, Proba.POEZIE));
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_9_11, Proba.POEZIE));
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_12_15, Proba.POEZIE));
//
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_6_8, Proba.CAUTAREA_UNEI_COMORI));
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_9_11, Proba.CAUTAREA_UNEI_COMORI));
//            concursDBRepository.save(new Concurs(Categorie.VARSTA_12_15, Proba.CAUTAREA_UNEI_COMORI));
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("RPC");
        System.out.println("Starting server on port: " + chatServerPort);
        AbstractServer server = new CCRpcConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
