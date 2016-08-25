package mattmunz.reasons.client;

import static java.util.Arrays.asList; 

import java.util.List;  
import java.util.Optional;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import mattmunz.reasons.Person;
import mattmunz.reasons.Question;
import mattmunz.service.ServiceClient;

/** 
 * TODO Rework to use Sprint Boot/ReST?
 */
public class ReasonsClient extends ServiceClient
{
  private final GenericType<List<Person>> peopleType = new GenericType<List<Person>>() {};
  private final GenericType<List<Question>> questionsType = new GenericType<List<Question>>() {};

  public ReasonsClient(String host, int port) { super(host, port, "Reasons"); }

  public Optional<Person> getPerson(String name)
  {
    // TODO Should use UriBuilder
    WebTarget target = getTarget("people").queryParam("name", name);
    
    List<Person> people = get(target, peopleType);
    
    // TODO General purpose logic should be made more general
    if (people.isEmpty()) { return Optional.empty(); }
    
    int personCount = people.size();
    
    if (personCount > 1)
    {
      new RuntimeException("Expected 1 person but got " + personCount + ": " + people);
    }
    
    return Optional.of(people.get(0));
  }

  public Person createPerson(String name) 
  { 
    return (Person) post("people", new Person(name)).getEntity();
  }

  public void createQuestions(Question... questions)
  {
    asList(questions).stream().forEach(this::postQuestion);
  }

  public List<Question> getQuestions() { return get("questions", questionsType); }

  private void postQuestion(Question question) { post("questions", question); }
}
