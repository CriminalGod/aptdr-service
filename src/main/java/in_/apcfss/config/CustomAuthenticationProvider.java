package in_.apcfss.config;


import in_.apcfss.domain.Customer;
import in_.apcfss.repo.CustomerRepo;
import in_.apcfss.util.RSAEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final RSAEncryptionService encryptionService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println(">>>>> IN PROVIDER <<<<");
        String userName = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        try {
            System.out.println(pwd);
            pwd = encryptionService.decryptPassword(pwd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(userName);
        System.out.println(pwd);
        System.out.println();
//      1. Fetching user details
        List<Customer> customers = customerRepo.findByEmail(userName);
        if(!customers.isEmpty()) {
//          2. Compare the passwords
            if(passwordEncoder.matches(pwd, customers.get(0).getPwd())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(customers.get(0).getRole()));
                return new UsernamePasswordAuthenticationToken(userName, pwd, authorities);
            } else {
                throw new BadCredentialsException("Invalid Password!");
            }
        } else {
            throw new BadCredentialsException("No user found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
