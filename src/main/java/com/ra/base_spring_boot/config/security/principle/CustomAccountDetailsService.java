package com.ra.base_spring_boot.config.security.principle;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAccountDetailsService implements UserDetailsService {

    private final AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepo.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account is not existing: " + email));
        return new AccountPrincipal(account);
    }
}
