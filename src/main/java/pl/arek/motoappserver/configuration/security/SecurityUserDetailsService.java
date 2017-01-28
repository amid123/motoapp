/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.arek.motoappserver.domain.entities.User;

import pl.arek.motoappserver.services.account.AccountService;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>ringo99
 */
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        List<GrantedAuthority> grantedAuthorities = buildGrantedAuthorities(userName);

        UserDetails userDetails = getUserDetaileByUserName(userName, grantedAuthorities);

        return userDetails;
    }

    private UserDetails getUserDetaileByUserName(String userName, List<GrantedAuthority> grantedAuthorities) throws UsernameNotFoundException {

        User user = this.accountService.getUserByUsername(userName);

        return new AppUserDetails(user, grantedAuthorities);
    }

    private List<GrantedAuthority> buildGrantedAuthorities(String userName) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<String> permissions = accountService.getPermissions(userName);
        for (String permission : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission));
        }
        return grantedAuthorities;
    }
}
