package com.garanti.TeknikServis.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Users
{
    private Integer USER_ID;
    @NonNull
    private String USERNAME = "";
    @NonNull
    private String PASSWORD = "";
    @NonNull
    private String USER_EMAIL = "";
}
