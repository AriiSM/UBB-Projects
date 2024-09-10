package server.Repo_Jdbc;


import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Inscriere;
import client.Domain_Simplu.Participant;
import server.Repo_Jdbc.Interface.IInscriereRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class InscriereDBRepository implements IInscriereRepository {
    private final JdbcUtils dbUtils;
    private final ParticipantDBRepository participantDBRepository;
    private final ConcursDBRepository concursDBRepository;


    //private final Logger logger = LogManager.getLogger(ConcursDBRepository.class);

    public InscriereDBRepository(Properties props) {
        //logger.info("Initializing InscriereDBRepository with proprties: {}", props);
        dbUtils = new JdbcUtils(props);
        participantDBRepository = new ParticipantDBRepository(props);
        concursDBRepository = new ConcursDBRepository(props);
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
