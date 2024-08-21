package sbp.cache.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sbp.cache.cache.entity.Customer;
import sbp.cache.cache.service.CustomerService;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/top-customers")
    public List<Customer> getTopCustomers() {
        return customerService.getTop5Customers();
    }

    @GetMapping("/refresh-cache")
    public String refreshCache() {
        customerService.refreshCache();
        return "Cache refreshed";
    }

    @PostMapping("/{customerId}/add")
    public String addPurchasesWithoutInvalidatingCache(@PathVariable Long customerId, @RequestParam int purchases) {
        customerService.addPurchasesWithoutInvalidatingCache(customerId, purchases);
        return "Added purchases without cache invalidation";
    }

    @PostMapping("/{customerId}/add-and-invalidate")
    public String addPurchasesWithCacheInvalidation(@PathVariable Long customerId, @RequestParam int purchases) {
        customerService.addPurchasesWithCacheInvalidation(customerId, purchases);
        return "Added purchases and invalidated cache";
    }

    @PostMapping("/{customerId}/add-purchases-with-condition")
    public String addPurchasesWithConditionalCacheInvalidation(@PathVariable Long customerId, @RequestParam int purchases) {
        customerService.addPurchasesWithConditionalCacheInvalidation(customerId, purchases);
        return "Added purchases with conditional cache invalidation";
    }

    @PostMapping("/{customerId}/add-purchases-with-manual-eviction-condition")
    public String addPurchasesWithConditionalManualCacheInvalidation(@PathVariable Long customerId, @RequestParam int purchases) {
        customerService.addPurchasesWithConditionalEviction(customerId, purchases);
        return "Added purchases with conditional manual cache invalidation";
    }

}