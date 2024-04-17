package com.thewinningteam.pms.security;

import com.thewinningteam.pms.Repository.UserRepository;
import com.thewinningteam.pms.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User userExist = repository.findByEmailOrUserName(userEmail,userEmail);
        if (userExist == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userExist ;
    }
}
