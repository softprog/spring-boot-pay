package spring.boot.pay.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class BaseDao {

    @Autowired
    @Qualifier("pay") 
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("namePay") 
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    @Qualifier("order") 
    protected JdbcTemplate jdbcTemplateOrder;

    @Autowired
    @Qualifier("nameOrder") 
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplateOrder;
}
