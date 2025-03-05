package in_.apcfss.controller;

import in_.apcfss.domain.Customer;
import in_.apcfss.repo.CustomerRepo;
import in_.apcfss.service.AuthenticationService;
import in_.apcfss.util.RSAEncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final RSAEncryptionUtil encryptionService;

    @GetMapping("/auth/public-key")
    public String getPublicKey() {
        return encryptionService.getPublicKey();
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody Customer customer) throws Exception {
        String decryptedPassword = encryptionService.decryptPassword(customer.getPwd());
        return "Received decrypted password: " + decryptedPassword;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        ResponseEntity<String> reponse = null;
        try {
            String encodedPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(encodedPwd);
            Customer savedCustomer = customerRepo.save(customer);
            if (savedCustomer.getId() > 0) {
                reponse = ResponseEntity.status(HttpStatus.CREATED).body("Registered");
            }
        } catch (Exception e) {
            reponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed...");
        }
        return reponse;
    }

    @RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        List<Customer> customers = customerRepo.findByEmail(authentication.getName());
        Optional<Customer> optionalCustomer = Optional.of(customers.get(0));
        System.out.println(optionalCustomer.get());
        return optionalCustomer.orElse(null);
    }


    @GetMapping("/get-user")
    public Customer getUserDetailsAfterLogin(@RequestParam String name) {
        List<Customer> customers = customerRepo.findByEmail(name);
        Optional<Customer> optionalCustomer = Optional.of(customers.get(0));
        System.out.println(optionalCustomer.get());
        return optionalCustomer.orElse(null);
    }

}
