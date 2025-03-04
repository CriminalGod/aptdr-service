package in_.apcfss.repo;

import in_.apcfss.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    List<Customer> findByEmail(String email);

}
