package mattmunz.reasons.server.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON; 
import static javax.ws.rs.core.Response.created;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List; 

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import mattmunz.reasons.Person;
import mattmunz.reasons.server.repository.PersonRepository;

@Path("people")
public class PeopleResource
{
  /** As needed, can inject this. */
  private final PersonRepository repository = new PersonRepository();
  
  @GET
  @Produces(APPLICATION_JSON)
  public List<Person> getPeople(@QueryParam("name") String name) 
  { 
    return name == null ? repository.get() : repository.getByName(name);
  }
  
  @GET
  @Path("/{identifier}")
  @Produces(APPLICATION_JSON)
  public Person getPerson(String identifier) 
  {
    String message 
      = "NYI. Lookup by Jot identifier [" + identifier + "] is not yet supported.";
    throw new RuntimeException(message);
  }
  
  /**
   * TODO A post to this with an empty body ({}) results in NPE when it should result in 
   *      bad request params exception or something. Is caused by Jackson? 
   *      How to mark fields as required (non-null) in a way that Jackson understands?     
   */
  @POST
  @Produces(APPLICATION_JSON)
  public Response add(Person person) throws URISyntaxException 
  { 
    // TODO Refactor URL
    URI uri = new URI("/Reasons/people/" + repository.add(person).getIdentifier());
    
    return created(uri).build();
  }
}
