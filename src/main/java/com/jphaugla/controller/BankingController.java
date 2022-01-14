package com.jphaugla.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.jphaugla.domain.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.jphaugla.service.BankService;


@RestController
public class BankingController {

	@Autowired
	private BankService bankService = BankService.getInstance();

	private static final Logger logger = LoggerFactory.getLogger(BankingController.class);
	// customer
	@RequestMapping("/save_customer")
	public String saveCustomer() throws ParseException {
		bankService.saveSampleCustomer();
		return "Done";
	}


	//  account
	@RequestMapping("/save_account")
	public String saveAccount() throws ParseException {
		bankService.saveSampleAccount();
		return "Done";
	}

	//  transaction
	@RequestMapping("/save_transaction")
	public String saveTransaction() throws ParseException {
		bankService.saveSampleTransaction();
		return "Done";
	}

	@GetMapping("/generateData")
	@ResponseBody
	public String generateData (@RequestParam Integer noOfCustomers, @RequestParam Integer noOfTransactions,
								@RequestParam Integer noOfDays, @RequestParam String key_suffix,
								@RequestParam Boolean pipelined)
			throws ParseException, ExecutionException, InterruptedException, IllegalAccessException {

		bankService.generateData(noOfCustomers, noOfTransactions, noOfDays, key_suffix, pipelined);

		return "Done";
	}

	@GetMapping("/generateRedisson")
	@ResponseBody
	public String generateRedisson ()
			throws ParseException, ExecutionException, InterruptedException, IllegalAccessException, IOException {

		bankService.redissonTransaction();

		return "Done";
	}

	@GetMapping("/testPipeline")
	@ResponseBody
	public String testPipeline (@RequestParam Integer noOfRecords)
			throws ParseException, ExecutionException, InterruptedException, IllegalAccessException {

		bankService.testPipeline(noOfRecords);

		return "Done";
	}

/*
	@GetMapping("/customerByStateCity")

	public SearchResults<String, String> getCustomerByStateCity(@RequestParam String state, @RequestParam String city) {
		logger.debug("IN get customerByState with state as " + state + " and city=" + city);
		return bankService.getCustomerByStateCity(state, city);
	}
	@GetMapping("/customerByZipcodeLastname")

	public SearchResults<String, String> getCustomerIdsbyZipcodeLastname(@RequestParam String zipcode, @RequestParam String lastname) {
		logger.debug("IN get getCustomerIdsbyZipcodeLastname with zipcode as " + zipcode + " and lastname=" + lastname);
		return bankService.getCustomerIdsbyZipcodeLastname(zipcode, lastname);
	}
	@GetMapping("/merchantCategoryTransactions")

	public SearchResults<String,String> getMerchantCategoryTransactions
			(@RequestParam String merchantCategory, @RequestParam String account,
			 @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			 @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate)
			throws ParseException {
		logger.debug("In getMerchantCategoryTransactions merchantCategory=" + merchantCategory + " account=" + account +
				" from=" + startDate + " to=" + endDate);
		return bankService.getMerchantCategoryTransactions(merchantCategory, account, startDate, endDate);
	}
	@GetMapping("/merchantTransactions")

	public SearchResults<String,String> getMerchantTransactions
			(@RequestParam String merchant, @RequestParam String account,
			 @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			 @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate)
				throws ParseException {
		logger.info("In getMerchantTransactions merchant=" + merchant + " account=" + account +
				" from=" + startDate + " to=" + endDate);
		return bankService.getMerchantTransactions(merchant, account, startDate, endDate);
	}

	@GetMapping ("/transactionStatusReport")

	public AggregateResults<String>  transactionStatusReport () {
		AggregateResults<String> keycounts = new AggregateResults<>();
		return bankService.transactionStatusReport();

	}
	@GetMapping("/returned_transactions")

	public SearchResults<String,String> getReturnedTransaction () {
		logger.info("in bankcontroller getReturnedTransaction");
		return bankService.getTransactionReturns();
	}

	@GetMapping("/statusChangeTransactions")

	public AggregateResults<String> generateStatusChangeTransactions(@RequestParam String transactionStatus)
			throws ParseException, IllegalAccessException, ExecutionException, InterruptedException {
		 logger.info("generateStatusChangeTransactions transactionStatus=" + transactionStatus);
		 AggregateResults<String> changeReport = new AggregateResults<>();

		 changeReport.addAll(transactionStatusReport());
		 bankService.transactionStatusChange(transactionStatus);
		 changeReport.addAll(transactionStatusReport());

		 return changeReport;

	}

	@GetMapping("/creditCardTransactions")

	public SearchResults<String, String> getCreditCardTransactions
			(@RequestParam String creditCard,
			 @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			 @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate)
			throws ParseException {
		logger.debug("getCreditCardTransactions creditCard=" + creditCard +
				" startDate=" + startDate + " endDate=" + endDate);
		return bankService.getCreditCardTransactions(creditCard, startDate, endDate);
	}

	@GetMapping("/accountTransactions")

	public SearchResults<String, String>  getAccountTransactions
			(@RequestParam String accountNo,
			 @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			 @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate)
			throws ParseException {
		logger.debug("getCreditCardTransactions creditCard=" + accountNo +
				" startDate=" + startDate + " endDate=" + endDate);
		return bankService.getAccountTransactions(accountNo, startDate, endDate);

	}

	@GetMapping("/addTag")

	public void addTag(@RequestParam String transactionID,
					   @RequestParam String tag, @RequestParam String operation) {
		logger.debug("addTags with transactionID=" + transactionID + " tag is " + tag + " operation is " + operation);
		bankService.addTag(transactionID, tag, operation);
	}

 */

	@GetMapping("/getTags")
	public HashSet <String> getTransactionTagList(@RequestParam String transactionID) {
		logger.debug("getTags with transactionID=" + transactionID);
		return bankService.getTransactionTagList(transactionID);
	}
	/*

	@GetMapping("/getTaggedTransactions")
	public SearchResults<String,String> getTaggedTransactions
			(@RequestParam String accountNo, @RequestParam String tag)
			throws ParseException {
		logger.debug("In getTaggedTransactions accountNo=" + accountNo + " tag=" + tag );
		return bankService.getTaggedTransactions(accountNo, tag);
	}

	 */

	@GetMapping("/getTransaction")
	public Transaction getTransaction(@RequestParam String transactionID) {
		Transaction transaction = bankService.getTransaction(transactionID);
		return transaction;
	}


	@GetMapping("/customer")

	public Optional<Customer> getCustomer(@RequestParam String customerId) {
		return bankService.getCustomer(customerId);
	}
	@GetMapping("/customerByPhone")

	public Optional<Customer> getCustomerByPhone(@RequestParam String phoneString) {
		logger.debug("In get customerByPhone with phone as " + phoneString);
		return bankService.getCustomerByPhone(phoneString);
	}

	@GetMapping("/customerByEmail")

	public Optional<Customer> getCustomerByEmail(@RequestParam String email) {
		logger.debug("IN get customerByEmail, email is " + email);
		return bankService.getCustomerByEmail(email);
	}

	@GetMapping("/customerLoop")

	public void customerLoop (@RequestParam int numberRange) {
		//  prefix is in bankservice
		// String customerPrefix="Customer:";
		String customerId = "";
		String postfix = "J";
		int baseNumber = 1000001;
		int endNumber = baseNumber + numberRange;
		for (int i=baseNumber;i<endNumber;i++) {
			customerId = String.valueOf(i) + postfix;
			logger.debug("Customer with customerId=" + customerId);
			Optional<Customer> returnCustomer = bankService.getCustomer(customerId);
			logger.debug("got customer " + returnCustomer.get().getFullName());
		}
		return;
	}

	@GetMapping("/emailLoop")

	public void emailLoop (@RequestParam int numberRange) {
		//  prefix is in 	bankservice
		// String customerPrefix="Customer:";
		String customerId = "";
		String email = "";
		String postfix = "J@gmail.com";
		int baseNumber = 1000001;
		int endNumber = baseNumber + numberRange;
		for (int i=baseNumber;i<endNumber;i++) {
			email = "Email:" + String.valueOf(i) + postfix;
			logger.debug("IN get emailloop, email is " + email);
			Optional<Customer> returnCustomer = bankService.getCustomerByEmail(email);
			logger.debug("got customer " + returnCustomer.get().getFullName());
		}
		return;
	}

	@GetMapping("/phoneLoop")

	public void phoneLoop (@RequestParam int numberRange) {
		//  prefix is in 	bankservice
		// String customerPrefix="Customer:";
		String customerId = "";
		String phone = "";
		String postfix = "Jh";
		int baseNumber = 1000001;
		int endNumber = baseNumber + numberRange;
		for (int i=baseNumber;i<endNumber;i++) {
			phone = "Phone:" + String.valueOf(i) + postfix;
			logger.debug("IN get phoneloop, phone is " + phone);
			Optional<Customer> returnCustomer = bankService.getCustomerByPhone(phone);
			logger.debug("got customer " + returnCustomer.get().getFullName());
		}
		return;
	}
	@GetMapping("/customerPhoneLoop")

	public void customerPhoneLoop (@RequestParam int numberRange) {
		//  prefix is in 	bankservice
		// String customerPrefix="Customer:";
		String custPhoneKey = "";
		String postfix = "J";
		int baseNumber = 1000001;
		int endNumber = baseNumber + numberRange;
		for (int i=baseNumber;i<endNumber;i++) {
			custPhoneKey = "CustPhone:" + String.valueOf(i) + postfix;
			logger.debug("IN get customerPhoneloop, customer is " + custPhoneKey);
			List<String> returnPhones = bankService.getCustomerPhone(custPhoneKey);
			logger.debug("return phones" + returnPhones.toString());
		}
		return;
	}

	@GetMapping("/customerEmailLoop")

	public void customerEmailLoop (@RequestParam int numberRange) {
		//  prefix is in 	bankservice
		// String customerPrefix="Customer:";
		String custEmailKey = "";
		String postfix = "J";
		int baseNumber = 1000001;
		int endNumber = baseNumber + numberRange;
		for (int i=baseNumber;i<endNumber;i++) {
			custEmailKey = "CustEmail:" + String.valueOf(i) + postfix;
			logger.debug("IN get customerEmailloop, customer is " + custEmailKey);
			List<String> returnEmails = bankService.getCustomerEmail(custEmailKey);
			logger.debug("return emails" + returnEmails.toString());
		}
		return;
	}

/*
	@GetMapping("/deleteCustomer")

	public int deleteCustomer(@RequestParam String customerString) {
		return bankService.deleteCustomer(customerString);
	}


 */


	@GetMapping("/deleteCustomerEmail")

	public int deleteCustomerEmail(@RequestParam String customerId) {
		return bankService.deleteCustomerEmail(customerId);
	}

	@PostMapping(value = "/postCustomer", consumes = "application/json", produces = "application/json")
	public String postCustomer(@RequestBody Customer customer ) throws ParseException {
		bankService.postCustomer(customer);
		return "Done\n";
	}






}
