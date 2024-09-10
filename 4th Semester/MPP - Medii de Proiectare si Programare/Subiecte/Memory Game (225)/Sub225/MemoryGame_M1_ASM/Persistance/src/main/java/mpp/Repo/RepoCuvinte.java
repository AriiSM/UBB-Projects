package mpp.Repo;

import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoCuvinte;
import mpp.Utils.UtilsJDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class RepoCuvinte implements IRepoCuvinte {

    private UtilsJDBC utils;

    @Autowired
    public RepoCuvinte(@Qualifier("props") Properties props) {
        utils = new UtilsJDBC(props);
    }

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String[] find5Pairs() {
        Connection con = utils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM CuvintePentruJoc ORDER BY RANDOM() LIMIT 5")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                String[] cuvinte = new String[5];
                int i = 0;
                while (resultSet.next()) {
                    cuvinte[i] = resultSet.getString("cuvand");
                    i++;
                }
                return cuvinte;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<String> findOne(Integer integer) {
        return Optional.empty();
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
