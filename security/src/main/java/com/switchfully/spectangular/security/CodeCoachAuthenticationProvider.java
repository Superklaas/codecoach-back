package com.switchfully.spectangular.security;


import com.switchfully.spectangular.exceptions.Feature;
import com.switchfully.spectangular.exceptions.User;
import com.switchfully.spectangular.services.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CodeCoachAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public CodeCoachAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.findUserByEmail(authentication.getPrincipal().toString());
        if(user != null && user.isPasswordCorrect(authentication.getCredentials().toString())){
            return new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getEncryptedPassword(),
                    rolesToGrantedAuthorities(new ArrayList<>(user.getRole().getFeatureList())));
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
