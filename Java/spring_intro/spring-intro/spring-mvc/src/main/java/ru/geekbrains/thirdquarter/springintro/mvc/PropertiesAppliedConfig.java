package ru.geekbrains.thirdquarter.springintro.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@PropertySource("classpath:AppProperties.properties")
public class PropertiesAppliedConfig {

    private Environment environment;

    @Autowired
    public PropertiesAppliedConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(this.environment.getProperty("datasource.driver")));
        dataSource.setUrl(this.environment.getProperty("datasource.url"));
        dataSource.setUsername(this.environment.getProperty("datasource.user"));
        dataSource.setPassword(this.environment.getProperty("datasource.password"));
        return dataSource;
    }
}
