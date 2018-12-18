package cz.jh.sos.controller;
import cz.jh.sos.Model.Customer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    JdbcTemplate jdbcTemplate;

    public CustomerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static final int PAGE_SIZE = 3;

    @GetMapping("/test")
    public String test() {
        return "Hondlo world";
    }

    @GetMapping("/customer/{id}")
    public Customer getcustomer(@PathVariable Long id) {

        return jdbcTemplate.queryForObject("select id, name, city , grade from customer where id = ?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Customer.class));
    }

    @GetMapping("/customer")
    public List<Customer> getCustomers(@RequestParam(required = false, defaultValue = "1") Integer pageNo) {

        return jdbcTemplate.query("select id, name, city , grade from customer order by id limit ?,?",
                new Object[]{getHowMuchToSkip(pageNo), PAGE_SIZE},
                new BeanPropertyRowMapper<>(Customer.class));

    }
    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer){
        return customer;


    }
    private int getHowMuchToSkip(int pageNo){

        if(pageNo<=0){
            throw new IllegalArgumentException("Invalid number of page");
        }
        return (pageNo-1) * PAGE_SIZE;
    }

}
