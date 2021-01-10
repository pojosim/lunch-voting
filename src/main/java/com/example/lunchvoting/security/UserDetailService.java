package com.example.lunchvoting.security;

import com.example.lunchvoting.repository.RestaurantRepository;
import com.example.lunchvoting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return new AuthorizedUser(userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User " + name + " is not found")));
    }
}