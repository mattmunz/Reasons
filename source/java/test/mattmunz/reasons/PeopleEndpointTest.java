package mattmunz.reasons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue; 

import java.util.Optional;

import mattmunz.reasons.client.ReasonsClient;

import org.junit.Test;

/**
 * A functional test of the persons endpoint.
 * 
 * ALso used for testing:
 * curl -H "Content-Type: application/json" -X POST -d '{ "name": "Peter Rabbit"}' http://localhost:8080/Reasons/people
 * 
 * TODO Add an annotation like @FunctionalTest
 */
public class PeopleEndpointTest
{
  @Test
  public void createPerson()
  {
    String name = "Marie Curie";

    ReasonsClient client = new ReasonsClient("localhost", 8080);
    
    Person person = client.createPerson(name);
    
    assertNotNull(person);
    assertEquals(name, person.getName());

    // TODO Introduce logging
    System.out.println("Created person: " + person);
  }

  // TODO Add a setup method where we do a basic sanity to confirm that the remote host is 
  //      online and ready for testing.
  // TODO Assumes that test data is present.
  @Test
  public void getPerson()
  {
    String name = "Marie Curie";

    ReasonsClient client = new ReasonsClient("localhost", 8080);
    Optional<Person> person = client.getPerson(name);
    
    assertTrue(person.isPresent());
    assertEquals(name, person.get().getName());

    // TODO Introduce logging
    System.out.println("Person: " + person);
  }
}
