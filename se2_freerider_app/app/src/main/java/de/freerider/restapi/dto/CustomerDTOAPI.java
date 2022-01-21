package de.freerider.restapi.dto;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping( "${app.api.endpoints.customers}" )

public interface CustomerDTOAPI {
	
	@Operation(
			summary = "Return all customers from repository.",		// appears as text in API short-list
			description = "Return all customers from repository.",	// appears as text inside API
			tags={ "customers-dto-controller" }	// appears in swagger-ui URL: http://localhost:8080/swagger-ui/index.html#/customers-controller
	)
	
	@ApiResponses( value = {	// also auto-derived by Swagger
			@ApiResponse( responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
			@ApiResponse( responseCode = "401", description = "Unauthorized"),
			@ApiResponse( responseCode = "403", description = "Forbidden"),
			// to remove "404" from docs, set SwaggerConfig::Docket.useDefaultResponseMessages(true) // ->false
//			@ApiResponse( responseCode = "404", description = "Not Found")
		})
	
	@RequestMapping(
			method = RequestMethod.GET,
			value = "",	// relative to interface @RequestMapping
			produces = { "application/json" }
		)
	
	
	ResponseEntity<List<CustomerDTO>> getCustomers();

	
	@Operation(
			summary = "Return customer with {id} from repository.",
			description = "Return customer with {id} from repository.",
			tags={ "customers-dto-controller" }
		)

		
		@RequestMapping(
			method=RequestMethod.GET,
			value="{id}",	// relative to interface @RequestMapping
			produces={ "application/json" }
		)
	
	ResponseEntity<CustomerDTO> getCustomer(@PathVariable("id")long id);
	

	@Operation(
			summary = "Add new customers to repository.",
			description = "Add new customers to repository.",
			tags={ "customers-dto-controller" }
		)

	
		@RequestMapping(
			method = RequestMethod.POST,
			value = ""	
		)

	ResponseEntity<List<CustomerDTO>> postCustomers(@RequestBody List<CustomerDTO> dtos);
	

	@Operation(
			summary = "Update existing customers in repository.",
			description = "Update existing customers in repository.",
			tags={ "customers-dto-controller" }
		)

	
		@RequestMapping(
			method = RequestMethod.PUT,
			value = ""	
		)
	
	ResponseEntity<List<CustomerDTO>> putCustomers(@RequestBody List<CustomerDTO> dtos);
	
	@Operation(
			summary = "Delete customer by its id from repository.",
			description = "Delete customer by its id from repository.",
			tags={ "customers-dto-controller" }
		)

		
		@RequestMapping(
			method = RequestMethod.DELETE,
			value = "{id}"	
		)
	
	ResponseEntity<?> deleteCustomer (@PathVariable("id") long id);
	
}
