package com.nascorp.marketpal.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.nascorp.marketpal.repository.UserRepository;
import com.nascorp.marketpal.entity.User;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .accountLocked(!user.isEnabled())
                .build();
    }

    
}
