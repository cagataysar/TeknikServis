package com.garanti.TeknikServis.controller;

import com.garanti.TeknikServis.model.Users;
import com.garanti.TeknikServis.repo.UserRepo;
import com.garanti.TeknikServis.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag (name = "User Table", description = "This class enables to user operations.")
@SecurityScheme (
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class UserController
{
    private UserService service;

    @Operation (summary = "This method is to fetch the users from database.")
    @SecurityRequirement (name = "Bearer Authentication")
    @GetMapping(path = "test")
    @Secured(value = "ROLE_ADMIN")
    public String getByUserName()
    {
        //localhost:9090/test
        return "merhaba";

    }


    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(@RequestBody Users users)
    {
        //localhost:9090/save
        // {"username":"test","password":"1234","user_EMAIL":"test@gmail.com"}
        if (service.userSave(users.getUSERNAME(),users.getPASSWORD(), users.getUSER_EMAIL()))
        {
            return ResponseEntity.status(HttpStatus.CREATED).body("Sisteme başarı ile kaydedildiniz");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sisteme kaydolurken hata oluştu");
        }
    }

}
