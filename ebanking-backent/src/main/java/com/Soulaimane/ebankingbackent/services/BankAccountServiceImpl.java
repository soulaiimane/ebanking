package com.Soulaimane.ebankingbackent.services;

import com.Soulaimane.ebankingbackent.dtos.*;
import com.Soulaimane.ebankingbackent.entities.*;
import com.Soulaimane.ebankingbackent.enums.AccountStatus;
import com.Soulaimane.ebankingbackent.enums.OperationType;
import com.Soulaimane.ebankingbackent.exeption.AccountNotFoundExeption;
import com.Soulaimane.ebankingbackent.exeption.BalanceNotSufficientExeption;
import com.Soulaimane.ebankingbackent.exeption.CustomerNotFoundExeption;
import com.Soulaimane.ebankingbackent.mappers.BankAccountMapperImpl;
import com.Soulaimane.ebankingbackent.repositories.AccountOperationRepository;
import com.Soulaimane.ebankingbackent.repositories.BankAccountRepository;
import com.Soulaimane.ebankingbackent.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
   private AccountOperationRepository accountOperationRepository;
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private BankAccountMapperImpl mapper;

    /*public BankAccountServiceImpl(AccountOperationRepository operationRepository, BankAccountRepository accountRepository, CustomerRepository customerRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository =accountRepository;
        this.customerRepository = customerRepository;
    }*/

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        log.info("creat new customer");
        Customer customer = mapper.fromCustomerDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDto customerDto1 = mapper.fromCustomer(savedCustomer);
        return customerDto1;

    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) throws CustomerNotFoundExeption {
        Customer customer=customerRepository.findById(customerDto.getId()).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundExeption("Customer Not Found");
        }
        if (customerDto.getName()!=null)customer.setName(customerDto.getName());
        if (customerDto.getEmail()!=null)customer.setEmail(customerDto.getEmail());
        customerRepository.save(customer);
        return mapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundExeption {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundExeption("Customer Not Found");
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public CurrentBankAccountDto currentBankAccount(double balance, double overDraft, Long customerId) throws CustomerNotFoundExeption {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundExeption("Customer Not Found");
        }
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(balance);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);

        return mapper.fromCurrentBankAccount(currentAccount);
    }

    @Override
    public SavingBankAccountDto savingBankAccount(double balance, double interestRate, Long customerId) throws CustomerNotFoundExeption {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundExeption("Customer Not Found");
        }
        mapper.fromCustomer(customer);
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(balance);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);

        return mapper.fromSavingBankAccount(savedSavingAccount);
    }


    @Override
    public List<CustomerDto> customerList() {
        List<Customer> customerList = customerRepository.findAll();
       /* List<CustomerDto> customerDtos=new ArrayList<>();
        for (Customer customer:customerList) {
            CustomerDto customerDto = mapper.fromCustomer(customer);
            customerDtos.add(customerDto);
        }*/
        List<CustomerDto> customerDtos = customerList.stream()
                .map(customer -> mapper.fromCustomer(customer))
                .collect(Collectors.toList());

        return customerDtos;
    }

    @Override
    public BankAccountDto getBankAccount(String accountId) throws AccountNotFoundExeption {
        BankAccount bankAccount=findBankAccount(accountId);
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
           return mapper.fromSavingBankAccount(savingAccount);
        }else {
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
           return mapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws AccountNotFoundExeption, BalanceNotSufficientExeption {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new AccountNotFoundExeption("Account Not Found"));
        if (bankAccount.getBalance()<amount){
            throw new BalanceNotSufficientExeption("Your Balance Sufficient");
        }
        AccountOperation operation=new AccountOperation();
        operation.setOperationDate(new Date());
        operation.setAmount(amount);
        operation.setOperationType(OperationType.DEBIT);
        operation.setDescription(description);
        operation.setAccount(bankAccount);
        AccountOperation savedOperation = accountOperationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws AccountNotFoundExeption, BalanceNotSufficientExeption {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new AccountNotFoundExeption("Account Not Found"));
        AccountOperation operation=new AccountOperation();
        operation.setOperationDate(new Date());
        operation.setAmount(amount);
        operation.setOperationType(OperationType.CREDIT);
        operation.setDescription(description);
        operation.setAccount(bankAccount);
        AccountOperation savedOperation = accountOperationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws AccountNotFoundExeption, BalanceNotSufficientExeption {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from"+accountIdSource);
    }

    @Override
    public CustomerDto getCustomer(Long customerId) throws CustomerNotFoundExeption {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundExeption("Customer Not Found");
        }
        CustomerDto customerDto = mapper.fromCustomer(customer);
        return customerDto;
    }

    @Override
    public List<CustomerDto> searchCustomerList(String keyword) {
        List<Customer> customerList = customerRepository.searchCustomer(keyword);
        List<CustomerDto> customerDtoList = customerList.stream().map(customer ->
                mapper.fromCustomer(customer)).collect(Collectors.toList());

        return customerDtoList;
    }

    @Override
    public List<BankAccountDto> ACCOUNTLIST() {
       List<BankAccount> bankAccountList =bankAccountRepository.findAll();
        List<BankAccountDto> bankAccountDtos = bankAccountList.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                return mapper.fromSavingBankAccount((SavingAccount) bankAccount);
            } else {
                return mapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDtos;
    }

    @Override
    public AccountHistoryDto gatAccountHistory(String accountId, int page, int size) throws AccountNotFoundExeption {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null) throw new AccountNotFoundExeption("Bank Account Not Exist");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByAccountIdOrderByOperationDateDesc
                (accountId,PageRequest.of(page, size));
        AccountHistoryDto accountHistoryDto=new AccountHistoryDto();
        List<AccountOperationDto> accountOperationDtos = accountOperations.stream().map(accountOperation ->
                mapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        accountHistoryDto.setOperationDtos(accountOperationDtos);
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDto;
    }

    @Override
    public List<BankAccountDto> findBankAccountsByCustomerId(Long customerId) throws CustomerNotFoundExeption {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundExeption("Customer Not Found");
        }
        List<BankAccount> bankAccounts = bankAccountRepository.findBankAccountsByCustomerId(customerId);

        List<BankAccountDto> bankAccountsDtos = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                return mapper.fromSavingBankAccount((SavingAccount) bankAccount);
            } else {
                return mapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountsDtos;


    }

    private BankAccount findBankAccount(String accountId) throws AccountNotFoundExeption {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(
                ()->new AccountNotFoundExeption("Account Not Found"));
        return bankAccount;
    }
    public List<AccountOperationDto>  gatAllOperationById(String accountId){
        List<AccountOperation> operations = accountOperationRepository.findByAccountId(accountId);
        List<AccountOperationDto> accountOperationDtos = operations.stream().map(accountOperation ->
                mapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        return accountOperationDtos;

    }
}
