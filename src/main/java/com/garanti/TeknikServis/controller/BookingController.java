package com.garanti.TeknikServis.controller;
import com.garanti.TeknikServis.model.Booking;
import com.garanti.TeknikServis.model.BookingDTO;
import com.garanti.TeknikServis.model.Users;
import com.garanti.TeknikServis.repo.BookingRepo;
import com.garanti.TeknikServis.response.RestResponse;
import com.garanti.TeknikServis.service.BookingService;
import com.garanti.TeknikServis.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping(path = "appointment")
public class BookingController {

    private BookingService appointmentService;
    private BookingRepo appointmentRepo;
    private UserService userService;

    public BookingController(BookingService appointmentService, BookingRepo appointmentRepo, UserService userService) {
        this.appointmentService=appointmentService;
        this.appointmentRepo=appointmentRepo;
        this.userService = userService;
    }

    @PreAuthorize(value = "isAuthenticated()")
    @GetMapping(path = "getById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingDTO> getByIdQueryParam(@RequestParam(value = "id", required = true) Integer id) {
        // http://localhost:9090/appointment/getById?id=1
        BookingDTO res = appointmentService.getById(id);
        if (res != null) {
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(@RequestBody Booking booking)
    {
        // {"note":"Formatlamaistiyorum", "user_ID":2, "service_ID":3}
        //localhost:9090/appointment/save

        if (appointmentService.save(booking))
        {

            return ResponseEntity.status(HttpStatus.CREATED).body("Kaydınız oluşturulmuştur.Kayıt numaranız: "+appointmentRepo.getNextId()+"dır.Lütfen kaybetmeyiniz.!");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Kaydınızda bir sorun oluştu. Lütfen yeniden kayıt oluşturunuz.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "deleteById/{id}")
    public ResponseEntity<String> deleteById(Authentication auth, @PathVariable(value = "id") Integer id) {
        // http://localhost:9090/appointment/deleteById/12
        if (auth.isAuthenticated())
        {
            System.err.println("-------> yes auth");
        }
        else
        {
            // buraya düşeceğini sanmıyoruz
            System.err.println("-------> not auth");
        }
        String username = auth.getPrincipal().toString();
        Users usr = userService.getUserByUsername(username);
        if (!usr.getUSER_ID().equals(appointmentRepo.getBookingById(id).getUSER_ID()))
        {
            return ResponseEntity.badRequest().body("Bu kayıt size ait değildir !!!");
        }

        if (appointmentService.deleteById(id)) {
            return ResponseEntity.ok("Başarı ile silindi");
        } else {
            return ResponseEntity.internalServerError().body("Başarı ile silinemedi");
        }
    }

    @GetMapping("getAppointmentsDatesInOrder")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity getAppointmentsDatesInOrder(@RequestParam(name = "sortType") String type){
        //http://localhost:9090/appointment/getAppointmentsDatesInOrder?sortType=asc
        return ResponseEntity.ok(RestResponse.of(appointmentService.getAppointmentsDatesInOrder(type)));
    }

    //http://localhost:9090/appointment/getAllAppointmentLikeUsername?username=A
    @GetMapping("getAllAppointmentLikeUsername")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity getAllAppointmentLikeUsername (@RequestParam(name = "username") String username){
        return ResponseEntity.ok(RestResponse.of(appointmentService.getAllAppointmentLikeUsername(username)));
    }



    @PatchMapping("appointmentIsComplete")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<?> appointmentIsComplete(@RequestParam(name = "id") int id, @RequestParam (name = "is_done") boolean is_done){
        //http://localhost:9090/appointment/appointmentIsComplete?id=1&is_done=true
        return ResponseEntity.ok(RestResponse.of(appointmentService.appointmentIsComplete(id,is_done)));
    }


}
