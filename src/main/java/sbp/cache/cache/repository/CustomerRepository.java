package sbp.cache.cache.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sbp.cache.cache.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findTop5ByOrderByPurchasesDesc();
}