package com.cxlsky.service;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

/**
 * @author cxl
 */
@Getter
public class UserDetail extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -6255728877238251305L;

    UserDetail(String username, String password, Set<SimpleGrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
    }

}
