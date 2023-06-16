package com.rajeshkawali.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rajeshkawali.dto.CustomerDTO;
import com.rajeshkawali.exception.ResponseStatus;
import com.rajeshkawali.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajesh_Kawali
 *
 */
@Slf4j
@RequestMapping("/api")
@RestController
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

	public static final String CLASS_NAME = CustomerController.class.getName();

	@Autowired
	private CustomerService customerService;

	@Tag(name = "getAllCustomers", description = "Get All Customers APIs")
	@Operation(summary = "Retrieve customers", tags = {"GET"}, description = "Get a customer object by specifying its id. The response is customer object provided back to client.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomerDTO.class)))),
			@ApiResponse(responseCode = "404", description = "Customer not found.", content = {@Content(schema = @Schema()) }) })
	@GetMapping("/v1/customer/getAll")
	public List<CustomerDTO> getAllCustomers() {
		String _function = ".getAllCustomers";
		log.info(CLASS_NAME + _function + "::ENTER");
		List<CustomerDTO> customerList = new ArrayList<>();
		customerList = customerService.getAllCustomers();
		log.info(CLASS_NAME + _function + "::EXIT");
		return customerList;
	}

	@ApiResponse(responseCode = "201", content = {
			@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE) })
	@Operation(
			summary = "Add a new customer.", 
			description = "This api is used to add a new customer in database", 
			tags = {"addCustomer"})
	//@Tag(name = "addCustomer", description = "Add customer details")
	@PostMapping("/v1/customer/add")
	public ResponseEntity<?> addCustomer(
			@Parameter(description = "Customer details") @Valid @RequestBody CustomerDTO customerDTO) {
		String _function = ".addCustomer";
		log.info(CLASS_NAME + _function + "::ENTER");
		log.debug(CLASS_NAME + _function + "::Customer details: {}", customerDTO);
		CustomerDTO addedCustomer = customerService.addCustomer(customerDTO);
		log.info(CLASS_NAME + _function + "::EXIT");
		return ResponseEntity.status(HttpStatus.CREATED).body(addedCustomer);

	}

	//@Tag(name = "CustomerById", description = "Get customer using customer id")
	 @Operation(
		      summary = "Retrieve a customer by Id",
		      description = "Get a customer object by specifying its id.",
		      tags = { "customerById", "GET"})
		  @ApiResponses({
		      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json") }),
		      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(description = "No customer found")) }),
		      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(description = "Server error")) }) })
	@GetMapping("/v1/customer/{id}")
	public ResponseEntity<?> customerById(
			@Parameter(description = "Customer id", required = true) @PathVariable Long id) {
		String _function = ".customerById";
		log.info(CLASS_NAME + _function + "::ENTER");
		log.debug(CLASS_NAME + _function + "::Requested customer id: {} ", id);
		CustomerDTO addedCustomer = customerService.customerById(id);
		if (addedCustomer != null) {
			log.info(CLASS_NAME + _function + "::EXIT");
			return ResponseEntity.status(HttpStatus.OK).body(addedCustomer);
		} else {
			log.error(CLASS_NAME + _function + "::Customer not available for given id: {}", id);
			throw ResponseStatus.idNotFound.apply(id);
		}
	}

	//@Tag(name = "findCustomerBySurname", description = "Get customer using surname")
	 @Operation(
		      summary = "Retrieve a customer by surname",
		      description = "Get a customer object by specifying its surname.",
		      tags = { "findCustomerBySurname", "GET"})
		  @ApiResponses({
		      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		      @ApiResponse(responseCode = "404", description = "No customer found") })
	@GetMapping("/v1/customer")
	public ResponseEntity<?> findCustomerBySurname(@Parameter(explode = Explode.TRUE, name = "surname", in = ParameterIn.QUERY, description = "Customer surname", style = ParameterStyle.FORM, schema = @Schema(type = "string", defaultValue = "available", allowableValues = { "koli", "kawali", "joshi" }))
			@RequestParam String surname) {
		String _function = ".findCustomerBySurname";
		log.info(CLASS_NAME + _function + "::ENTER");
		log.debug(CLASS_NAME + _function + "::Customer surname: {} ", surname);
		CustomerDTO addedCustomer = customerService.findCustomerBySurname(surname);
		if (addedCustomer != null) {
			log.info(CLASS_NAME + _function + "::EXIT");
			return ResponseEntity.status(HttpStatus.OK).body(addedCustomer);
		} else {
			log.error(CLASS_NAME + _function + "::Customer not available for given surname: {}", surname);
			throw ResponseStatus.nameNotFound.apply(surname);
		}
	}
	
	@Parameters({ 
			@Parameter(name = "id", description = "customer id to search and update", required = true),
			@Parameter(name = "CustomerDTO", description = "Customer details to update") })
	@ApiResponses({ @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(description = "Customer details are successfully updated to the DB"), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(schema = @Schema(description = "Customer not found for the given id")) }) })
	//@Tag(name = "updateCustomer", description = "Update the customer details")
	@PutMapping("/v1/customer/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
		String _function = ".updateCustomer";
		log.info(CLASS_NAME + _function + "::ENTER");
		log.debug(CLASS_NAME + _function + "::Customer details to update-> id: {}, Customer details: {}", id,
				customerDTO);
		CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
		if (updatedCustomer != null) {
			log.info(CLASS_NAME + _function + "::EXIT");
			return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
		} else {
			log.error(CLASS_NAME + _function + "::Customer not available for given Id: {} ", id);
			throw ResponseStatus.idNotFound.apply(id);
		}
	}

	@Operation(summary = "Delete customer by Id", description = "This api is used to delete the customer by id", tags = {
			"deleteCustomer"})
	@ApiResponses({ @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(schema = @Schema(description = "Customer not found for given Id")) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(schema = @Schema(defaultValue = "Server error")) }) })
	//@Tag(name = "deleteCustomer", description = "Delete the customer details")
	@DeleteMapping("/v1/customer/{id}")
	public ResponseEntity<?> deleteCustomer(
			@Parameter(description = "Customer id to delete", required = true) @PathVariable Long id) {
		String _function = ".deleteCustomer";
		log.info(CLASS_NAME + _function + "::ENTER");
		log.debug(CLASS_NAME + _function + "::Customer id to delete from db: {} ", id);
		String result = customerService.deleteCustomer(id);
		if (result != null) {
			log.info(CLASS_NAME + _function + "::EXIT");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			log.error(CLASS_NAME + _function + "::Customer not available for given id: {} ", id);
			throw ResponseStatus.idNotFound.apply(id);
		}
	}

}
