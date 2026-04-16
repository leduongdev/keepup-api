package com.ra.base_spring_boot.config.security.principle;

import com.ra.base_spring_boot.model.Account;
import com.ra.base_spring_boot.model.enums.AccountStatus;
import com.ra.base_spring_boot.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomAccountDetailsService implements UserDetailsService {

    private final AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + account.getRole().getRoleName().name()));

        if (account.getRole().getPermissions() != null) {
            account.getRole().getPermissions().forEach(p ->
                    authorities.add(new SimpleGrantedAuthority(p.getName())));
        }

        return new AccountPrincipal(account, authorities);
    }
}
