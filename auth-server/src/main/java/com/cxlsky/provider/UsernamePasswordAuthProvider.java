package com.cxlsky.provider;

import com.cxlsky.token.UsernamePasswordAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author CXL
 */
@Component
public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    @Autowired
    @Qualifier("UserDetailServiceImpl")
    private UserDetailsService userDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthToken usernamePasswordAuthToken = (UsernamePasswordAuthToken) authentication;
        String username = String.valueOf(usernamePasswordAuthToken.getPrincipal());
        String password = String.valueOf(usernamePasswordAuthToken.getCredentials());
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BadCredentialsException("用户名或密码为空");
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(username.trim());
        if (userDetails == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw new BadCredentialsException("密码与用户名不匹配");
        }
        UsernamePasswordAuthToken authenticationToken = new UsernamePasswordAuthToken(userDetails, password, userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthToken.class.isAssignableFrom(authentication);
    }
}
