package com.garanti.TeknikServis.repo;

import com.garanti.TeknikServis.model.Booking;
import com.garanti.TeknikServis.model.BookingDTO;
import com.garanti.TeknikServis.model.Service;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class BookingRepo {


    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BookingDTO getById(int id)
    {
        BookingDTO bookingDTO = null;
        String sql = "SELECT b.B_ID,u.USERNAME,s.SERVICE_NAME,s.DURATION,s.DESKTOP,s.LAPTOP,s.MAC,b.NOTE,b.BOOKING_DATE from BOOKING b "
                +"INNER JOIN USERS u ON u.USER_ID=b.USER_ID "
                +"INNER JOIN SERVICE s ON s.SERVICE_ID=b.SERVICE_ID where b.B_ID = :ABUZIDDIN ";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ABUZIDDIN", id);
        bookingDTO = namedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(BookingDTO.class));
        return bookingDTO;
    }



    public boolean deleteById(int id)
    {
        String sql = "DELETE FROM BOOKING where B_ID = :B_ID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("B_ID", id);
        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }

    public boolean save (Booking booking,Date date)
    {
        String sql = "INSERT INTO BOOKING (NOTE,USER_ID,SERVICE_ID,BOOKING_DATE) values (:NOTE,:USER_ID,:SERVICE_ID,:BOOKING_DATE)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("NOTE", booking.getNOTE());
        paramMap.put("USER_ID", booking.getUSER_ID());
        paramMap.put("SERVICE_ID", booking.getSERVICE_ID());
        paramMap.put("BOOKING_DATE", date);
        return  namedParameterJdbcTemplate.update(sql,paramMap)==1;

    }
    public Integer getNextId()
    {
        //aynı anda çağrılırsa yanlış ıd döndürülebilir.
        String sql="SELECT MAX(B_ID) FROM BOOKING";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public Service service_hour_control(Booking booking) {
        Service service= null;
        String sql = "SELECT DURATION FROM SERVICE WHERE SERVICE_ID =:A";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("A", booking.getSERVICE_ID());
        service=namedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(Service.class));
         //x=namedParameterJdbcTemplate.update(sql, paramMap);
        return service;
    }


    public List<Service> getDurationList(Date new_date) {
        Service services=null;
        String sql = "SELECT s.DURATION FROM SERVICE s "
        +"INNER JOIN BOOKING b ON b.SERVICE_ID=s.SERVICE_ID WHERE b.BOOKING_DATE=:NEW_DATE";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("NEW_DATE",new_date);
        return namedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Service.class));
    }
}
