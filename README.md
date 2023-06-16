# Springboot-openapi-application
This spring boot v3 project explain the usage of openapi documentation (Swagger v3)

### Swagger UI URL:
> http://localhost:8282/swagger-ui/index.html

### Swagger 2 to Swagger 3 annotations
Swagger 3 is an updated version of Swagger 2 and has some changes in annotations:

* @Api → @Tag
* @ApiIgnore → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
* @ApiImplicitParam → @Parameter
* @ApiImplicitParams → @Parameters
* @ApiModel → @Schema
* @ApiModelProperty(hidden = true) → @Schema(accessMode = READ_ONLY)
* @ApiModelProperty → @Schema
* @ApiOperation(value = "foo", notes = "bar") → @Operation(summary = "foo", description = "bar")
* @ApiParam → @Parameter
* @ApiResponse(code = 404, message = "foo") → @ApiResponse(responseCode = "404", description = "foo")

### To use Swagger 3 annotation in Spring Boot v3
You need to add the springdoc-openapi-starter-webmvc-ui dependency to your Maven project’s pom.xml file
```
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

### Swagger 3 @Tag annotation
In Swagger 3, the @Tag annotation is used to provide additional information about tags in the Swagger documentation. 
Tags are used to group API operations together and provide a way to categorize and organize them in a meaningful way.
The @Tag annotation can be added to a method inside a Controller to provide a name and description for the tag.
```
@Tag(name = "Customer", description = "Customer management APIs")
```

### Swagger 3 @Operation annotation
In Swagger 3, the @Operation annotation is used to provide metadata for a single API operation.
```
@Operation(
		      summary = "Retrieve a customer by Id",
		      description = "Get a customer object by specifying its id.",
		      tags = { "customerById", "GET"})
```

### Swagger 3 @ApiResponses and @ApiResponse annotation
In Swagger 3, the @ApiResponses annotation can be added to an API operation method to provide a list of possible responses for that operation. 
Each response is specified using an @ApiResponse annotation.

The @ApiResponse annotation specifies the HTTP status code, description, and content of a response. 
The content is described using the @Content annotation, which includes the media type and schema of the response body.
```
@ApiResponses({
		      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(description = "No customer found")) }),
		      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(description = "Server error")) }) })
```

### Swagger 3 @Parameter annotation
The @Parameter annotation in Swagger 3 is used to describe a parameter for an operation in an OpenAPI / Swagger document.
```
@GetMapping("/v1/customer")
	public ResponseEntity<?> findCustomerBySurname(@Parameter(explode = Explode.TRUE, name = "surname", in = ParameterIn.QUERY, description = "Customer surname", style = ParameterStyle.FORM, schema = @Schema(type = "string", defaultValue = "available", allowableValues = { "Koli", "Kawali", "Joshi" }))
			@RequestParam String surname) {
}

or

@GetMapping("/v1/customer/{id}")
	public ResponseEntity<?> customerById(
			@Parameter(description = "Customer id", required = true) @PathVariable Long id) {
}
```

Or you can use @Parameters annotation to define multiple input parameters for an operation.
```
@Parameters({ 
			@Parameter(name = "id", description = "customer id to search and update", required = true),
			@Parameter(name = "CustomerDTO", description = "Customer details to update") })
@PutMapping("/v1/customer/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
}
```

### Swagger 3 @Schema annotation
In Swagger 3, the @Schema annotation is used in to provide additional information about the schema of a model or parameter in your API.
```
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
}
```