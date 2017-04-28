package de.rwthaachen.mi.convis;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by mwright on 6/15/16.
 */

@EnableAutoConfiguration
@ComponentScan({"de.rwthaachen.mi.convis", "de.rwthaachen.mi.convis.config"})
@SpringBootApplication
public class EdfConvisApplication extends SpringBootServletInitializer {

    /* Database parameters are retrieved from application.properties */
    @Value("${spring.datasource.driver-class-name}") private String databaseDriverClassName;
    @Value("${spring.datasource.url}") private String datasourceUrl;
    @Value("${spring.datasource.username}") private String databaseUsername;
    @Value("${spring.datasource.password}") private String databasePassword;

    public static void main(String[] args) {
        SpringApplication.run(EdfConvisApplication.class, args);
    }

    /**
     * Bean for datasource setup.
     *
     * @return The datasource
     */
    @Bean
    public DataSource datasource() {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();

        ds.setDriverClassName(databaseDriverClassName);
        ds.setUrl(datasourceUrl);
        ds.setUsername(databaseUsername);
        ds.setPassword(databasePassword);
        return ds;
    }
    
}
