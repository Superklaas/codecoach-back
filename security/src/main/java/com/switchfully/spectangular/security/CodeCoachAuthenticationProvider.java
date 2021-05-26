package com.switchfully.spectangular.security;


import com.switchfully.spectangular.domain.Feature;
import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CodeCoachAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CodeCoachAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        User user;

        try {
            user = userService.findUserByEmail(authentication.getPrincipal().toString());
        } catch (Exception e) {
            throw new UsernameNotFoundException("Email not found in system");
        }

        if(user != null) {
            String password = authentication.getCredentials().toString();
            if (passwordEncoder.matches(password, user.getEncryptedPassword())) {
                return new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getEncryptedPassword(),
                        rolesToGrantedAuthorities(Feature.getForRole(user.getRole())));
            }
        }
        throw new BadCredentialsException("The provided credentials were invalid.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Collection<? extends GrantedAuthority> rolesToGrantedAuthorities(List<Feature> features) {
        return features.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
