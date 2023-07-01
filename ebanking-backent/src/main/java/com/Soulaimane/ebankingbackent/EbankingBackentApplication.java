package com.Soulaimane.ebankingbackent;

import com.Soulaimane.ebankingbackent.dtos.BankAccountDto;
import com.Soulaimane.ebankingbackent.dtos.CurrentBankAccountDto;
import com.Soulaimane.ebankingbackent.dtos.CustomerDto;
import com.Soulaimane.ebankingbackent.dtos.SavingBankAccountDto;
import com.Soulaimane.ebankingbackent.entities.*;
import com.Soulaimane.ebankingbackent.enums.AccountStatus;
import com.Soulaimane.ebankingbackent.enums.OperationType;
import com.Soulaimane.ebankingbackent.exeption.AccountNotFoundExeption;
import com.Soulaimane.ebankingbackent.exeption.BalanceNotSufficientExeption;
import com.Soulaimane.ebankingbackent.exeption.CustomerNotFoundExeption;
import com.Soulaimane.ebankingbackent.repositories.AccountOperationRepository;
import com.Soulaimane.ebankingbackent.repositories.BankAccountRepository;
import com.Soulaimane.ebankingbackent.repositories.CustomerRepository;
import com.Soulaimane.ebankingbackent.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackentApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackentApplication.class, args);
	}
	@Bean
			CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return args -> {
			Stream.of("hassan","yassine","aicha","khadija","moustafa","salman","salma","salim").forEach(name->{
				CustomerDto customerDto=new CustomerDto();
				customerDto.setName(name);
				customerDto.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customerDto);

			});
			bankAccountService.customerList().forEach(customer -> {
				try {
					bankAccountService.currentBankAccount(Math.random()*10000,Math.random()*1000,customer.getId());
				} catch (CustomerNotFoundExeption e) {
					e.printStackTrace();
				}
				try {
					bankAccountService.savingBankAccount(Math.random()*10000,5.5,customer.getId());

				} catch (CustomerNotFoundExeption e) {
					e.printStackTrace();
				}

			});
			List<BankAccountDto> accountlist = bankAccountService.ACCOUNTLIST();
			for (BankAccountDto bankAccountdto:accountlist){
				for (int i = 0; i <5 ; i++) {
					String accountId;
					if (bankAccountdto instanceof SavingBankAccountDto){

						accountId= ((SavingBankAccountDto) bankAccountdto).getId();

					}else {
						accountId= ((CurrentBankAccountDto) bankAccountdto).getId();
					}
					bankAccountService.credit(accountId,15000+Math.random()*90000,"first credit");
					bankAccountService.debit(accountId,1000+Math.random()*9000,"first debit");

				}
			}

		};

	};
	/*@Bean
	CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){

		return args -> {
			Stream.of("hassan","yassine","aicha","khadija","moustafa","salman","salma","salim").forEach(name->{
				Customer customer=new Customer();
				customer.setName(name);
				customer.setMail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount=new CurrentAccount();
				currentAccount.setCustomer(customer);
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*10000);
				currentAccount.setOverDraft(Math.random()*1000);
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCreatedAt(new Date());
				bankAccountRepository.save(currentAccount);

					SavingAccount savingAccount=new SavingAccount();
					savingAccount.setId(UUID.randomUUID().toString());
					savingAccount.setCustomer(customer);
					savingAccount.setBalance(Math.random()*10000);
					savingAccount.setInterestRate(Math.random()*1000);
					savingAccount.setStatus(AccountStatus.ACTIVATED);
					savingAccount.setCreatedAt(new Date());
					bankAccountRepository.save(savingAccount);

			});
			bankAccountRepository.findAll().forEach(bankAccount -> {
				for (int i = 0; i <5 ; i++) {
					AccountOperation accountOperation=new AccountOperation();
					accountOperation.setAccount(bankAccount);
					accountOperation.setOperationType(Math.random()>0.5?OperationType.DEBIT:OperationType.CREDIT);
					accountOperation.setAmount(Math.random()*1000);
					accountOperation.setOperationDate(new Date());
					accountOperationRepository.save(accountOperation);

				}


			});

		};
	}*/
}
