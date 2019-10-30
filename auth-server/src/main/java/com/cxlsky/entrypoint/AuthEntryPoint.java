package com.cxlsky.entrypoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

/**
 * @author CXL
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthEntryPoint {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    @GetMapping("/password")
    public String encrypt(@RequestParam("password") String password) {
        String encode = passwordEncoder.encode(password);

        log.info("==========================> encrypt result: [{}]", encode);
        return encode;
    }

    @GetMapping({"/password/matches"})
    public boolean matches(@RequestParam("raw") String raw, @RequestParam("encode") String encode) {
        boolean matches = passwordEncoder.matches(raw, encode);

        log.info("==========================> matches result: [{}]", matches);
        return matches;
    }

    @PutMapping({"/token/evict"})
    public boolean evictToken(@RequestParam("token") String token) {
        boolean revokeToken = consumerTokenServices.revokeToken(token);

        log.info("==========================> evictToken result: [{}]", revokeToken);
        return revokeToken;
    }


}
