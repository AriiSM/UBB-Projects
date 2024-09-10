package server.Repo_Jdbc;


import client.Domain_Simplu.Participant;
import server.Repo_Jdbc.Interface.IParticipantRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipantDBRepository implements IParticipantRepository {
    private final JdbcUtils dbUtils;
    //private final Logger logger = LogManager.getLogger(ParticipantDBRepository.class);


    public ParticipantDBRepository(Properties props) {
        //logger.info("Initializing ParticipantDBRepository with proprties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Participant> findOne(Integer id) {
        //logger.traceEntry("ParticipantDBRepository findOne: {}", id);
        Connection conn = dbUtils.getConnection();
        Participant participant = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Participant WHERE id = ?")) {
            stms.setInt(1, id);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    Integer idp = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    Integer varsta = resultSet.getInt("varsta");

                    participant = new Participant(nume, prenume, varsta);
                    participant.setId(idp);
                    // logger.trace("findOne{}", participant);
                }
            }
        } catch (SQLException e) {
            // logger.error(e);
            System.err.println("Error DB" + e);
        }
        // logger.traceExit(participant);
        return Optional.ofNullable(participant);
    }

    @Override
    public Iterable<Participant> findAll() {
        //logger.traceEntry("ParticipantDBRepository findAll!");
        Connection conn = dbUtils.getConnection();
        List<Participant> participantList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Participant")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    int varsta = resultSet.getInt("varsta");

                    Participant participant = new Participant(nume, prenume, varsta);
                    participant.setId(id);
                    participantList.add(participant);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(participantList);
        return participantList;
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        Participant nou = null;
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Participant(nume, prenume, varsta) VALUES (?, ?, ?) RETURNING id")) {
            stmt.setString(1, entity.getLastName());
            stmt.setString(2, entity.getFirstName());
            stmt.setInt(3, entity.getAge());

            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1)); // Setare ID-ul în obiectul Inscriere
                } else {
                    throw new SQLException("Creating Participant failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(entity);
    }

    @Override
    public List<Participant> filterProbaCategorie(String proba, String categorie) {
        //logger.traceEntry("ParticipantDBRepository filterProbaCategorie: proba={}, categorie={}", proba, categorie);
        Connection conn = dbUtils.getConnection();
        List<Participant> participantList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT P.id, P.nume, P.prenume, P.varsta FROM Participant P INNER JOIN Inscriere I ON P.id=I.id_participant INNER JOIN Concurs C on C.id = I.id_concurs WHERE C.categorie = ? and C.proba = ?")) {
            stmt.setString(1, categorie);
            stmt.setString(2, proba);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    int varsta = resultSet.getInt("varsta");

                    Participant participant = new Participant(nume, prenume, varsta);
                    participant.setId(id);
                    participantList.add(participant);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        // logger.traceExit(participantList);
        return participantList;
    }

    @Override
    public Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta) {
        //logger.traceEntry("ParticipantDBRepository findOne: {},{},{}", nume,prenume,varsta);
        Connection conn = dbUtils.getConnection();
        Participant participant = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Participant WHERE nume = ? and prenume = ? and varsta = ?")) {
            stms.setString(1, nume);
            stms.setString(2, prenume);
            stms.setInt(3, varsta);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    Integer idp = resultSet.getInt("id");
                    String numep = resultSet.getString("nume");
                    String prenumep = resultSet.getString("prenume");
                    Integer varstap = resultSet.getInt("varsta");

                    participant = new Participant(numep, prenumep, varstap);
                    participant.setId(idp);
                    //logger.trace("findOne{}", participant);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(participant);
        return Optional.ofNullable(participant);
    }

    @Override
    public Integer numarProbePentruParticipant(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        //logger.traceEntry("ParticipantDBRepository numarProbePentruParticipant: {}", id);
        Connection conn = dbUtils.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Inscriere WHERE id_participant = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int numarProbe = resultSet.getInt(1);
                    return numarProbe;
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        // logger.traceExit(numarProbe);
        return -1;
    }
}