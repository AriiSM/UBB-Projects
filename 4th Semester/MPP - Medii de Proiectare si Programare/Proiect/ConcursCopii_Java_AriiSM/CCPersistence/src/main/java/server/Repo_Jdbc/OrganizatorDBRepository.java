package server.Repo_Jdbc;


import client.Domain_Simplu.Organizator;
import server.AESUtil;
import server.Repo_Jdbc.Interface.IOrganizatorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class OrganizatorDBRepository implements IOrganizatorRepository {
    private final JdbcUtils dbUtils;
    //private final Logger logger = LogManager.getLogger(OrganizatorDBRepository.class);


    public OrganizatorDBRepository(Properties props) {
        //logger.info("Initializing OrganizatorDBRepository with proprties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Organizator> findOne(Integer id) {
        //logger.traceEntry("OrganizatorDBRepository findOne: {}", id);
        Connection conn = dbUtils.getConnection();
        Organizator organizator = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Organizator WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int ido = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String parolaCriptata = resultSet.getString("parola");
                    organizator = new Organizator(nume, prenume, parolaCriptata);
                    organizator.setId(ido);
                }
            } catch (Exception e) {
                //logger.error(e);
                System.err.println("Error decrypt" + e);
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(organizator);
        return Optional.ofNullable(organizator);
    }

    @Override
    public Iterable<Organizator> findAll() {
        //logger.traceEntry("OrganizatorDBRepository findAll!");
        Connection conn = dbUtils.getConnection();
        List<Organizator> organizatoriList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Organizator")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String parolaCriptata = resultSet.getString("parola");

                    Organizator organizator = new Organizator(nume, prenume, parolaCriptata);
                    organizator.setId(id);
                    organizatoriList.add(organizator);
                }
            } catch (Exception e) {
                //logger.error(e);
                System.err.println("Error decrypt" + e);
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(organizatoriList);
        return organizatoriList;
    }

    @Override
    public Optional<Organizator> save(Organizator entity) {
        //logger.traceEntry("OrganizatorDBRepository save: {}", entity);
        //organizatorValidator.validate(entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Organizator(nume,prenume,parola) VALUES (?,?,?)")) {
            stmt.setString(1, entity.getLastName());
            stmt.setString(2, entity.getFirstName());


            String parolaCriptata = AESUtil.encrypt(entity.getParola());
            stmt.setString(3,parolaCriptata);

            //int result = stmt.executeUpdate();
            //logger.trace("Saved{} instances", result);
        } catch (SQLException e) {
           //logger.error(e);
            System.err.println("Error DB" + e);
        } catch (Exception e) {
            //logger.error(e);
            System.err.println("Error encrypt" + e);
        }
        //logger.traceExit(entity);
        return Optional.of(entity);
    }

    @Override
    public Organizator findAccount(String parolaO, String numeO, String prenumeO) {
        //logger.traceEntry("OrganizatorDBRepository gasesteUserDupaParola: {}", parolaO);
        Connection conn = dbUtils.getConnection();
        Organizator organizator = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Organizator WHERE parola = ? AND nume = ? AND prenume = ? ")) {
            String parolaCriptata = AESUtil.encrypt(parolaO);
            stmt.setString(1, parolaCriptata);
            stmt.setString(2, numeO);
            stmt.setString(3, prenumeO);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");


                    organizator = new Organizator(nume, prenume, parolaO);
                    organizator.setId(id);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        } catch (Exception e) {
           // logger.error(e);
            System.err.println("Error encrypt" + e);
        }
        //logger.traceExit(organizator);
        return organizator;
    }
}
