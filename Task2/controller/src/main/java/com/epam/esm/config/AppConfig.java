package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;

@EnableWebMvc
@ComponentScan(basePackages = "com.epam.esm")
//@PropertySource("classpath:../resources/application.properties")
@Configuration
public class AppConfig implements WebMvcConfigurer {
    //@Value("${db.url}")
    private String url;

    //@Value("${db.user}")
    private String user;

    //@Value("${db.password}")
    private String password;

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/gift_certificates?useUnicode=true&serverTimezone=UTC");
        dataSource.setUsername("user");
        dataSource.setPassword("password");

        return dataSource;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
