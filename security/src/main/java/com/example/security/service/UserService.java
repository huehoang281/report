package com.example.security.service;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User getUserById (int id) {
        return userRepository.findById(String.valueOf(id)).orElse(null);
    }
    public User getUserByName (String name) {
        return userRepository.findByName(name).orElse(null);
    }
    public void registerUser(User user) {
     userRepository.save(user);
    }


}
