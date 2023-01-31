package com.garanti.TeknikServis.repo;

import com.garanti.TeknikServis.model.Sale;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class SaleRepository {

    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List< Sale > getAll () {
        String sql = "select * from GARANTI.SALE ORDER BY SALE_ID DESC";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Sale.class));
    }

    public boolean deleteById (Integer id) {
        String sql = "delete from GARANTI.SALE where SALE_ID = :ID";
        Map< String, Object > parameter = new HashMap<>();
        parameter.put("ID", id);

        return namedParameterJdbcTemplate.update(sql, parameter) == 1;
    }

    public boolean save (Sale sale) {
        String sql = "Insert into GARANTI.SALE (PRODUCT_ID,NOTE,PRICE, IS_SOLD) values (:PRODUCT, :NOTE, :PRICE, :SOLD)";
        Map< String, Object > paramMap = new HashMap<>();
        paramMap.put("PRODUCT", sale.getPRODUCT_ID());
        paramMap.put("NOTE", sale.getNOTE());
        paramMap.put("PRICE", sale.getPRICE());
        paramMap.put("SOLD", sale.isIS_SOLD());

        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }
}
