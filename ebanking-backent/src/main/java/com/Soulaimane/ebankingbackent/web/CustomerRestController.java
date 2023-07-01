package com.Soulaimane.ebankingbackent.web;

import com.Soulaimane.ebankingbackent.dtos.CustomerDto;
import com.Soulaimane.ebankingbackent.entities.Customer;
import com.Soulaimane.ebankingbackent.exeption.CustomerNotFoundExeption;
import com.Soulaimane.ebankingbackent.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDto> customerListRest(){
       return bankAccountService.customerList();

    }
    @GetMapping("/customers/search")
    public List<CustomerDto> searchCustomerListRest(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomerList("%"+keyword+"%");

    }
    @GetMapping("/customers/{customerId}")
    public CustomerDto getcustomer(@PathVariable Long customerId) throws CustomerNotFoundExeption {
        return bankAccountService.getCustomer(customerId);

    }
    @PostMapping("/customers")
    public CustomerDto saveCustomerRest(@RequestBody CustomerDto customerDto){
            return bankAccountService.saveCustomer(customerDto);
    }
    @PutMapping("/customers/{customerId}")
    public CustomerDto updateCustomerRest(@RequestBody CustomerDto customerDto,@PathVariable Long customerId) throws CustomerNotFoundExeption {
        customerDto.setId(customerId);
        return bankAccountService.updateCustomer(customerDto);
    }
    @DeleteMapping("/customers/{customerId}")
    public void deleteCustomerRest(@PathVariable Long customerId) throws CustomerNotFoundExeption {
        bankAccountService.deleteCustomer(customerId);
    }



}
