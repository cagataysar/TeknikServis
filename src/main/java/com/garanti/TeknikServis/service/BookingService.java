package com.garanti.TeknikServis.service;
import com.garanti.TeknikServis.model.Booking;
import com.garanti.TeknikServis.model.BookingDTO;
import com.garanti.TeknikServis.model.Service;
import com.garanti.TeknikServis.repo.BookingRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BookingService {
    @NonNull
    private BookingRepo appointmentRepo;


    public boolean deleteById(int id)
    {
        return appointmentRepo.deleteById(id);
    }

    public BookingDTO getById(int id)
    {
        return appointmentRepo.getById(id);
    }

    public boolean save (Booking booking)
    {
        try
        {
            Service service = appointmentRepo.service_hour_control(booking);
            Date date=createBookingDate(service.getDURATION());
            return appointmentRepo.save(booking,date);
        }
        catch (Exception e)
        {
            return false;
        }
    }

private Date createBookingDate(Integer serviceDuration)
{
    LocalDate bookingDate = LocalDate.now();
    Date sqlDate = Date.valueOf(bookingDate);
    bookingDate = bookingDate.plusDays(-1L);
    int duration = 0;
    do
    {
        duration = serviceDuration;
        bookingDate = bookingDate.plusDays(1L);
        sqlDate = Date.valueOf(bookingDate);
        //List<Booking> bookings = appointmentRepo.dateControl(sqlDate);
        List<Service> services=appointmentRepo.getDurationList(sqlDate);
        for (Service services1 : services)
        {
            duration += services1.getDURATION();
        }
    }
    while (duration > 10);
    return sqlDate;
}

}
