import server.*;
import server.Repo_Jdbc.ConcursDBRepository;
import server.Repo_Jdbc.InscriereDBRepository;
import server.Repo_Jdbc.OrganizatorDBRepository;
import server.Repo_Jdbc.ParticipantDBRepository;
import server.utils.AbstractServer;
import server.utils.CCRpcConcurrentServer;
import server.utils.ServerException;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
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

        ParticipantDBRepository participantDBRepository = new ParticipantDBRepository(serverProps);
        OrganizatorDBRepository organizatorDBRepository = new OrganizatorDBRepository(serverProps);
        InscriereDBRepository inscriereDBRepository = new InscriereDBRepository(serverProps);
        ConcursDBRepository concursDBRepository = new ConcursDBRepository(serverProps);

        ICCServices chatServerImpl = new ServiceImplementation(participantDBRepository, organizatorDBRepository, inscriereDBRepository, concursDBRepository);
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
