package com.cxlsky.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * className UserDetailServiceImpl
 * description UserDetailServiceImpl
 *
 * @author cxl
 */
@Service("UserDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetail("cxl", "$2a$10$9IeDHvGu8jhICMac700fGe2IE1WL1f/DZZzgT4bD4U1x29stxqbOy", new HashSet<>());
    }


}
