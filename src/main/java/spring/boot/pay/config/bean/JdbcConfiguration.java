package spring.boot.pay.config.bean;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

import spring.boot.pay.config.properties.JdbcProperties;

@Configuration
public class JdbcConfiguration {

    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(JdbcProperties.driverClass);
        dataSource.setUrl(JdbcProperties.jdbcUrl);
        dataSource.setUsername(JdbcProperties.userName);
        dataSource.setPassword(JdbcProperties.password);
        dataSource.setMaxActive(JdbcProperties.maxActive);

        return dataSource;
    }

    @Bean(name="pay")
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }


    @Bean(name="namePay")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource());
        return namedParameterJdbcTemplate;
    }

    @Bean
    public DataSource dataSourceOrder(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(JdbcProperties.driverClass);
        dataSource.setUrl(JdbcProperties.jdbcUrl_order);
        dataSource.setUsername(JdbcProperties.userName_order);
        dataSource.setPassword(JdbcProperties.password_order);
        dataSource.setMaxActive(JdbcProperties.maxActive);

        return dataSource;
    }

    @Bean(name="order")
    public JdbcTemplate jdbcTemplateOrder(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSourceOrder());
        return jdbcTemplate;
    }

    @Bean(name="nameOrder")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplateOrder(){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceOrder());
        return namedParameterJdbcTemplate;
    }

}
