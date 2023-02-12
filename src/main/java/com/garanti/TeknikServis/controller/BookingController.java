package com.garanti.TeknikServis.controller;
import com.garanti.TeknikServis.model.Booking;
import com.garanti.TeknikServis.model.BookingDTO;
import com.garanti.TeknikServis.repo.BookingRepo;
import com.garanti.TeknikServis.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "appointment")
public class BookingController {

    private BookingService appointmentService;
    private BookingRepo appointmentRepo;

    public BookingController(BookingService appointmentService, BookingRepo appointmentRepo) {
        this.appointmentService=appointmentService;
        this.appointmentRepo=appointmentRepo;
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "getById", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingDTO> getByIdQueryParam(@RequestParam(value = "id", required = true) Integer id) {
        // localhost:9090/FirstSpringWeb/ogretmen/getById?id=1
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
        //appointmentService.deneme(booking);

        if (appointmentService.save(booking))
        {

            return ResponseEntity.status(HttpStatus.CREATED).body("Kaydınız oluşturulmuştur.Kayıt numaranız: "+appointmentRepo.getNextId()+"dır.Lütfen kaybetmeyiniz.!");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Kaydınızda bir sorun oluştu. Lütfen yeniden kayıt oluşturunuz.");
        }
    }

    @Secured("ROLE_USER")
    @DeleteMapping(path = "deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") Integer id) {
        // localhost:9090/FirstSpringWeb/ogretmen/deleteById/1
        if (appointmentService.deleteById(id)) {
            return ResponseEntity.ok("Başarı ile silindi");
        } else {
            return ResponseEntity.internalServerError().body("Başarı ile silinemedi");
        }
    }


}
