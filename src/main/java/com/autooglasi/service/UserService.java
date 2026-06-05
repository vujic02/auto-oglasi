package com.autooglasi.service;

import com.autooglasi.dto.RegistrationForm;
import com.autooglasi.entity.User;

import java.util.List;

public interface UserService {

    /** Registruje novog korisnika sa rolom ROLE_USER. */
    User register(RegistrationForm form);

    User findByUsername(String username);

    User findById(Long id);

    List<User> findAll();

    /** Uključuje/isključuje nalog korisnika (admin). */
    void toggleEnabled(Long id);

    void delete(Long id);
}
