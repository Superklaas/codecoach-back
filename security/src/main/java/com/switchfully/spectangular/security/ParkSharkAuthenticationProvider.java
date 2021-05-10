package com.switchfully.spectangular.security;


import com.switchfully.spectangular.account.Account;
import com.switchfully.spectangular.account.Feature;
import com.switchfully.spectangular.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ParkSharkAuthenticationProvider implements AuthenticationProvider {

    private final AccountService accountService;

    @Autowired
    public ParkSharkAuthenticationProvider(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Account user = accountService.findAccountByEmailAndPassword(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
        if(user != null){
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
