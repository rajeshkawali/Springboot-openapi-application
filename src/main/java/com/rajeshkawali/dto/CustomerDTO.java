
package com.rajeshkawali.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rajesh_Kawali
 * 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName", "surname", "smoothiePreference", "mobileNumber" })
@Schema(description = "Customer dto information")
public class CustomerDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "customer id", example = "123133")
	@JsonProperty("id")
	private Long id;

	@Schema(description = "This field describe the customer's name", example = "Rajesh")
	@NotBlank(message = "First name shouldn't be null or empty")
	@JsonProperty("firstName")
	private String firstName;

	@Schema(description = "This field describe the customer's surname", example = "Kawali")
	@NotNull(message = "Surname shouldn't be null")
    @NotEmpty(message = "Surname shouldn't be empty")
	@JsonProperty("surname")
	private String surname;

	@Schema(description = "This field describe the customer's favorite fruit", example = "Strawberry")
	@NotBlank(message = "Smoothie Preference shouldn't be null or empty")
	@JsonProperty("smoothiePreference")
	private String smoothiePreference;

	//@Size(max = 15, min = 10, message = "Invalid mobile number")
	//@Pattern(regexp = "^\\d{10}$", message = "Invalid mobile number")
	@Schema(description = "This field describe the customer's mobile number", example = "9988776655")
	@Digits(message = "Invalid mobile number", fraction = 0, integer = 10)
	@JsonProperty("mobileNumber")
	private Long mobileNumber;
}