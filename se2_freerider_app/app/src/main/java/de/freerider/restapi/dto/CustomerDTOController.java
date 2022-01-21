package de.freerider.restapi.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.freerider.datamodel.Customer;
import de.freerider.repository.CustomerRepository;

@RestController
public class CustomerDTOController implements CustomerDTOAPI{
	
	@Autowired
	private ApplicationContext context;
	
//	private final ObjectMapper objectMapper;
	
	private final HttpServletRequest request;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public CustomerDTOController (HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public ResponseEntity<List<CustomerDTO>> getCustomers() {
		
		System.err.println(request.getMethod() + " " + request.getRequestURI());
		
		List<CustomerDTO> list = new ArrayList<>();
		
		for(Customer c : customerRepository.findAll()) {
			list.add(new CustomerDTO(c));
		}
		
		return new ResponseEntity<List<CustomerDTO>>(list, HttpStatus.OK);
		
	}

	@Override
	public ResponseEntity<CustomerDTO> getCustomer(long id) {
		
		ResponseEntity<CustomerDTO> re = null;
		System.err.println(request.getMethod() + " " + request.getRequestURI());
		
		Optional<Customer> optCustomer = customerRepository.findById(id);
		
		if( !optCustomer.isPresent()) {
			re = new ResponseEntity<CustomerDTO>(HttpStatus.NOT_FOUND);
		}
		
		Customer customer = optCustomer.get();
		
		CustomerDTO cDTO = new CustomerDTO(customer);
		
		re = new ResponseEntity<CustomerDTO>(cDTO,HttpStatus.OK);
		
		return re;
	}

	@Override
	public ResponseEntity<List<CustomerDTO>> postCustomers(List<CustomerDTO> dtos) {
		
		
		System.err.println(request.getMethod() + " "+request.getRequestURI());
		
		if(dtos == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		for(int i = 0; i<dtos.size();i++) {
			Optional<Customer>	optCustomer = dtos.get(i).create();
			
			if(!optCustomer.isPresent()) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			Customer customer = optCustomer.get();
			if(customerRepository.existsById(customer.getId())) {
				
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);
			}
			customerRepository.save(customer);
		}
		return new ResponseEntity<>(null, HttpStatus.CREATED);	
	}

	@Override
	public ResponseEntity<List<CustomerDTO>> putCustomers(List<CustomerDTO> dtos) {
		System.err.println(request.getMethod() + " " +request.getRequestURI());
        ArrayList<CustomerDTO> nopers = new ArrayList<CustomerDTO>();
        
            try {
        
        dtos.stream().forEach(dto -> {
            dto.print();
            
            Optional<Customer> optCustomer = dto.create();
            if(optCustomer.isPresent()) {
                if(customerRepository.existsById(optCustomer.get().getId())) {
                    CustomerDTO.print(optCustomer);
                    customerRepository.deleteById(optCustomer.get().getId());
                    customerRepository.save(optCustomer.get());
                }else {
                    nopers.add(dto);
                }               
            } else {
                nopers.add(dto);
            }
        });
        
        if(nopers.size()==0) {
        	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else {
        	return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        }
            } catch (NumberFormatException e) {
                System.out.println(e);
                }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            
	}

	@Override
	public ResponseEntity<?> deleteCustomer(long id) {
		System.err.println( "DELETE /customers/" + id );
	    if(customerRepository.existsById(id)) {
	    	customerRepository.deleteById(id);
	    	return new ResponseEntity<>( null, HttpStatus.ACCEPTED ); 
	    }else {
	    	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	    }
	}

}
