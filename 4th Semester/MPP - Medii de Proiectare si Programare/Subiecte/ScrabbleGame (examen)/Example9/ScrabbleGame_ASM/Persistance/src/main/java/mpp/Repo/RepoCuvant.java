package mpp.Repo;

import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoCuvant;
import mpp.Utils.UtilsJDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

@Component
public class RepoCuvant implements IRepoCuvant {
    private UtilsJDBC utils;

    @Autowired
    public RepoCuvant(@Qualifier("props")Properties props) {
        utils = new UtilsJDBC(props);
    }

    private static final Logger logger = LogManager.getLogger();
    @Override
    public Optional<String> findOne(Integer integer) {
        Connection con = utils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Cuvant ORDER BY RANDOM() LIMIT 1")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                String cuvant = resultSet.getString("cuvant");
                return Optional.ofNullable(cuvant);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<String> findAll() {
        return null;
    }

    @Override
    public Optional<String> save(String entity) throws GenericException {
        return Optional.empty();
    }

    @Override
    public Optional<String> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<String> update(String entity) throws GenericException {
        return Optional.empty();
    }
}
