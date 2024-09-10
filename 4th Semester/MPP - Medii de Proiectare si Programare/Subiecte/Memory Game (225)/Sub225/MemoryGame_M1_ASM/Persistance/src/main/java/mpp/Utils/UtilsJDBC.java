package mpp.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class UtilsJDBC {
    private Properties props;
    private Connection instance1 = null;

    private static final Logger logger= LogManager.getLogger();

    public UtilsJDBC(Properties props) {
        this.props = props;
    }

    private Connection getNewConnection(){
        logger.traceEntry();
        String url = props.getProperty("jdbc.url");
        logger.info("trying to connect to databse ... {}", url);
        Connection con=null;
        try{
            con = DriverManager.getConnection(url);
        }catch(SQLException e){
            logger.error(e);
            System.out.println("DataBase connection Error: " + e);
        }
        return con;
    }

    public Connection getConnection(){
        logger.traceEntry();
        try{
            if(instance1==null || instance1.isClosed())
                instance1 = getNewConnection();
        }catch(SQLException e){
            logger.error(e);
            System.out.println("Error DataBase: " + e);
        }
        return instance1;
    }
}
