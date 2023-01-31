package com.garanti.TeknikServis.service;

import com.garanti.TeknikServis.model.Users;
import com.garanti.TeknikServis.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService
{
    private UserRepo userRepository;
    private PasswordEncoder bCryptPasswordEncoder;

    public boolean userSave(String username, String password, String e_mail)
    {
        Users usr = new Users(userRepository.getNextId(), username, bCryptPasswordEncoder.encode(password), e_mail);
        return userRepository.save(usr);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
    {
        Users myUser = userRepository.getByUserName(username);
        if (myUser == null)
        {

            throw new UsernameNotFoundException("Kullanıcı adı bulunamadı");
        }

        User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(myUser.getUSERNAME());
        builder.password(myUser.getPASSWORD());
        List<GrantedAuthority> roles = userRepository.getUserRoles(myUser.getUSER_ID());
        builder.authorities(roles);
        return builder.build();
    }

    public Users getUserByUsername(String username)
    {
        return userRepository.getByUserName(username);
    }
}
