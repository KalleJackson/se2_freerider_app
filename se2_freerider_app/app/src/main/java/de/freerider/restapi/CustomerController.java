package de.freerider.restapi;

import java.io.IOException;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.freerider.datamodel.Customer;
import de.freerider.repository.CustomerRepository;

//@RestController
class CustomersController implements CustomersAPI{
    @Autowired
    private ApplicationContext context;
    //
    private final ObjectMapper objectMapper;
    //
    private final HttpServletRequest request;
    
    @Autowired
    private CustomerRepository customerRepository;


    /**
     * Constructor.
     * 
     * @param objectMapper entry point to JSON tree for the Jackson library
     * @param request HTTP request object
     */
    public CustomersController( ObjectMapper objectMapper, HttpServletRequest request ) {
        this.objectMapper = objectMapper;
        this.request = request;
    }


    /**
     * GET /people
     * 
     * Return JSON Array of people (compact).
     * 
     * @return JSON Array of people
     */
    @Override
    public ResponseEntity<List<?>> getCustomers() {
        //
        ResponseEntity<List<?>> re = null;
        System.err.println( request.getMethod() + " " + request.getRequestURI() );   
        try {
            ArrayNode arrayNode = peopleAsJSON();
            ObjectReader reader = objectMapper.readerFor( new TypeReference<List<ObjectNode>>() { } );
            List<String> list = reader.readValue( arrayNode );
            //
            re = new ResponseEntity<List<?>>( list, HttpStatus.OK );

        } catch( IOException e ) {
            re = new ResponseEntity<List<?>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return re;
    }


    /**
     * GET /people/pretty
     * 
     * Return JSON Array of people (pretty printed with indentation).
     * 
     * @return JSON Array of people
     */
    @Override
    public ResponseEntity<String> getCustomer(long id) {
        //
        ResponseEntity<String> re = null;
        System.err.println( request.getMethod() + " " + request.getRequestURI() );   
        try {
            ArrayNode arrayNode = peopleAsJSON();
//            
            if(arrayNode.get((int) id-1) != null) {
            String pretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString( arrayNode.get((int) id -1) );
            re = new ResponseEntity<String>( pretty, HttpStatus.OK );
            }
        
            else {
                re = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
            }
            return re;
            
            
        } catch( IOException e ) {
            re = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return re;
    }
    

    

    private ArrayNode peopleAsJSON() {
        //
        ArrayNode arrayNode = objectMapper.createArrayNode();
        
        //
        customerRepository.findAll().forEach( c -> {
            StringBuffer sb = new StringBuffer();
            c.getContacts().forEach( contact -> sb.append( sb.length()==0? "" : "; " ).append( contact ) );
            arrayNode.add(
                objectMapper.createObjectNode()
                	.put( "id", c.getId())
                    .put( "name", c.getLastName() )
                    .put( "first", c.getFirstName() )
                    .put( "contacts", sb.toString() )
            );
        });
        return arrayNode;
    }
    
    private ArrayNode customerAsJSON(Customer customer) {
    	 
    	ArrayNode arrayNode = objectMapper.createArrayNode();
    	
    	customerRepository.findById(customer.getId());
    		StringBuffer sb = new StringBuffer();
    		
    		customer.getContacts().forEach(contact -> sb.append(sb.length()==0? "" : "; " ).append( contact ));
    		
    		arrayNode.add(
    			objectMapper.createObjectNode()
    				.put("id", customer.getId())
    				.put("name", customer.getLastName())
    				.put("first",customer.getFirstName())
    				.put("contacts", sb.toString())
    		);
    		
    		return arrayNode;	
    }


	@Override
	
	public ResponseEntity<List<?>> postCustomers( Map<String, Object>[] jsonMap ) {
		return null;
	}
	


	@Override
	public ResponseEntity<List<?>> putCustomers(Map<String, Object>[] jsonMap) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseEntity<?> deleteCustomer(long id) {
		
	    System.err.println( "DELETE /customers/" + id );
	    if(customerRepository.existsById(id)) {
	    	customerRepository.deleteById(id);
	    	return new ResponseEntity<>( null, HttpStatus.ACCEPTED ); // status 202
	    }else {
	    	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	    }
	    
	}


}
