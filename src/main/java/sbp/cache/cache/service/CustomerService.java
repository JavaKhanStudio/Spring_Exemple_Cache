package sbp.cache.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sbp.cache.cache.entity.Customer;
import sbp.cache.cache.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final CacheManager cacheManager;

    public CustomerService(CustomerRepository customerRepository, CacheManager cacheManager) {
        this.customerRepository = customerRepository;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "topCustomersCache", cacheManager = "cacheManager")
    public List<Customer> getTop5Customers() {
        System.out.println("Fetching top 5 customers from the database...");
        return customerRepository.findTop5ByOrderByPurchasesDesc();
    }

    @CacheEvict(value = "topCustomersCache", allEntries = true)
    public void refreshCache() {
        System.out.println("Cache invalidated.");
    }

    public void addPurchasesWithoutInvalidatingCache(Long customerId, int additionalPurchases) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setPurchases(customer.getPurchases() + additionalPurchases);
        customerRepository.save(customer);
        System.out.println("Added purchases without cache invalidation for customer " + customer.getName());
    }

    @CacheEvict(value = "topCustomersCache", allEntries = true)
    public void addPurchasesWithCacheInvalidation(Long customerId, int additionalPurchases) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setPurchases(customer.getPurchases() + additionalPurchases);
        customerRepository.save(customer);
        System.out.println("Added purchases and invalidated cache for customer " + customer.getName());
    }

    @CacheEvict(value = "topCustomersCache", allEntries = true, condition = "#additionalPurchases != 1000")
    public void addPurchasesWithConditionalCacheInvalidation(Long customerId, int additionalPurchases) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setPurchases(customer.getPurchases() + additionalPurchases);
        customerRepository.save(customer);
        System.out.println("Added purchases for customer " + customer.getName() + " with " + additionalPurchases + " additional purchases.");
    }

    public void addPurchasesWithConditionalEviction(Long customerId, int additionalPurchases) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setPurchases(customer.getPurchases() + additionalPurchases);
        customerRepository.save(customer);

        if (additionalPurchases != 1002) {
            Cache cache = cacheManager.getCache("topCustomersCache");
            if (cache != null) {
                // cache.clear(); // Toute la cache
                // System.out.println("Cache invalidated due to complex condition.");
                cache.evict(customerId);  // Seulement un élément
                System.out.println("Cache entry for customer " + customerId + " evicted.");
            }
        } else {
            System.out.println("Cache entry not evicted.");
        }
    }
}
