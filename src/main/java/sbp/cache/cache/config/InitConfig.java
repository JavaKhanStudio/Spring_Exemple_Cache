package sbp.cache.cache.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import sbp.cache.cache.entity.Customer;
import sbp.cache.cache.repository.CustomerRepository;

@Configuration
public class InitConfig {

    private final CustomerRepository customerRepository;

    public InitConfig(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @PostConstruct
    public void initializeCustomers() {
        for (int i = 1; i <= 20; i++) {
            Customer customer = new Customer();
            customer.setId((long) i);
            customer.setName("Customer " + i);
            customer.setPurchases((int) (Math.random() * 100));
            customer.setFavoriteProduct("Product " + (i % 5));
            customerRepository.save(customer);
        }
        System.out.println("Initialized 20 customers.");
    }

}
