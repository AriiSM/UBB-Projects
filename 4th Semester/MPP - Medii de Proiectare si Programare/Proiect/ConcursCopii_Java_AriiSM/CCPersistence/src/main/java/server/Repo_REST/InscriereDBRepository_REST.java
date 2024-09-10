package server.Repo_REST;


import client.Domain_REST.Concurs;
import client.Domain_REST.Inscriere;
import client.Domain_REST.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import server.Repo_REST.ConcursDBRepository_REST;
import server.Repo_REST.Interface.IInscriereRepository;
import server.Repo_REST.JdbcUtils;
import server.Repo_REST.ParticipantDBRepository_REST;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
@Component
public class InscriereDBRepository_REST implements IInscriereRepository {
    private final JdbcUtils dbUtils;
    private final ParticipantDBRepository_REST participantDBRepository;
    private final ConcursDBRepository_REST concursDBRepository;


    //private final Logger logger = LogManager.getLogger(ConcursDBRepository.class);
    @Autowired
    public InscriereDBRepository_REST(@Qualifier("props")Properties props) {
        //logger.info("Initializing InscriereDBRepository with proprties: {}", props);
        dbUtils = new JdbcUtils(props);
        participantDBRepository = new ParticipantDBRepository_REST(props);
        concursDBRepository = new ConcursDBRepository_REST(props);
    }

    @Override
    public Optional<Inscriere> findOne(Integer id) {
        //logger.traceEntry("InscriereDBRepository findOne: {}", id);
        Connection conn = dbUtils.getConnection();
        Inscriere inscriere = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Inscriere WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int idi = resultSet.getInt("id");
                    int idp = resultSet.getInt("id_participant");
                    int idc = resultSet.getInt("id_concurs");

                    Participant participant = participantDBRepository.findOne(idp).get();
                    Concurs concurs = concursDBRepository.findOne(idc).get();

                    inscriere = new Inscriere(participant, concurs);
                    inscriere.setId(idi);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(inscriere);
        return Optional.ofNullable(inscriere);
    }

    @Override
    public Iterable<Inscriere> findAll() {
        //logger.traceEntry("InscriereDBRepository findAll!");
        Connection conn = dbUtils.getConnection();
        List<Inscriere> inscriereList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Inscriere")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int idi = resultSet.getInt("id");
                    int idp = resultSet.getInt("id_participant");
                    int idc = resultSet.getInt("id_concurs");

                    Participant participant = participantDBRepository.findOne(idp).get();
                    Concurs concurs = concursDBRepository.findOne(idc).get();

                    Inscriere inscriere = new Inscriere(participant, concurs);
                    inscriere.setId(idi);

                    inscriereList.add(inscriere);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(inscriereList);
        return inscriereList;
    }

    @Override
    public Optional<Inscriere> save(Inscriere entity) {
        Connection conn = dbUtils.getConnection();
        System.out.println("Salvare inscriere...");
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Inscriere(id_participant, id_concurs) VALUES (?,?) RETURNING id")) {
            stmt.setInt(1, entity.getParticipant().getId());
            stmt.setInt(2, entity.getConcurs().getId());

            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1)); // Setare ID-ul în obiectul Inscriere
                } else {
                    throw new SQLException("Creating inscriere failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.of(entity);
    }


    @Override
    public Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs) {
        Connection conn = dbUtils.getConnection();
        Inscriere inscriere = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Inscriere WHERE id_participant = ? AND id_concurs = ?")) {
            stmt.setInt(1, id_participant);
            stmt.setInt(2, id_concurs);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int idi = resultSet.getInt("id");
                    int idp = resultSet.getInt("id_participant");
                    int idc = resultSet.getInt("id_concurs");

                    Participant participant = participantDBRepository.findOne(idp).get();
                    Concurs concurs = concursDBRepository.findOne(idc).get();

                    inscriere = new Inscriere(participant, concurs);
                    inscriere.setId(idi);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.ofNullable(inscriere);
    }
}
