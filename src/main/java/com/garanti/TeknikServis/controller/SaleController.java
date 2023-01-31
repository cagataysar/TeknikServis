package com.garanti.TeknikServis.controller;

import com.garanti.TeknikServis.model.Sale;
import com.garanti.TeknikServis.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping (path = "sale")
@Tag (name = "Sale Table", description = "This class contains information of the sales.")
public class SaleController {

    private SaleService service;

    public SaleController (SaleService service) {
        this.service = service;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping ("getAll")
    @Operation (summary = "This is to fetch all the sales stored in db.")
    public ResponseEntity< List< Sale > > getAll () {

        // localhost:9090/sale/getAll
        List< Sale > result = service.getAll();
        if ( result == null || result.size() == 0 ) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping (path = "delete/{id}")
    public ResponseEntity< String > deleteById (@PathVariable (value = "id") Integer id) {

        // localhost:9090/sale/delete/2
        if ( service.deleteById(id) ) {
            return ResponseEntity.ok("Başarı ile silindi");
        } else {
            return ResponseEntity.internalServerError().body("Başarı ile silinemedi!");
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping (path = "save",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< String > save (@RequestBody Sale sale) {
        // localhost:9090/sale/save
        // {"price" : 500,  "note" : "notee note ", "product_ID":1,"is_SOLD": true}
        if ( service.save(sale) ) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Başarıyla kaydedildi");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Başarıyla kaydedilemedi");
        }
    }
}
