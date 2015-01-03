package com.toptal.expensetracker.service;

import com.toptal.expensetracker.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

/**
 * @author: Sergey Royz
 * Date: 27.12.2014
 */
@Service("authService")
public class AuthService implements UserDetailsService {

    @Inject
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsAdapter(user);
    }

    public static User getUser() {
        return ((AuthService.UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    public static class UserDetailsAdapter implements UserDetails {

        @Getter
        private User user;

        public UserDetailsAdapter(User user) {
            this.user = user;
        }

        public String getEmail() {
            return user.getEmail();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "user";
                }
            });
        }

        @Override
        public String getPassword() {
            //TODO: deal with hashing
            return user.getPasswordHash();
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

    }
}

