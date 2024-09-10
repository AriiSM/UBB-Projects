package mpp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


@SpringBootApplication
public class StartRestServices {

    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }

    @Bean(name="props")
    public Properties getBdProperties(){
        Properties props = new Properties();
        try {
            props.load(new FileReader("D:\\FACULTATE\\SEM4\\MPP\\PracticeForExam\\Example9\\ScrabbleGame_ASM\\RestServices\\src\\main\\resources\\prop.properties"));
        } catch (IOException e) {
            System.err.println("Configuration file bd.config not found" + e);
        }
        return props;
    }

    @Bean
    public DataSource dataSource(Properties props) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(props.getProperty("jdbc.driver"));
        dataSource.setUrl(props.getProperty("jdbc.url"));
        return dataSource;
    }
}
