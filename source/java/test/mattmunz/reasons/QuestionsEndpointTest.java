package mattmunz.reasons;

import static java.time.ZonedDateTime.now;
import static org.junit.Assert.assertFalse;

import java.util.List;

import mattmunz.reasons.client.ReasonsClient;

import org.junit.Test;

/**
 * A functional test of the questions endpoint.
 * 
 * TODO Add an annotation like @FunctionalTest
 */
public class QuestionsEndpointTest
{
  private final ReasonsClient client = new ReasonsClient("localhost", 8080);

  @Test
  public void createQuestions()
  {
    Person person1 = client.getPerson("Marie Curie").get();
    
    Question question1 = new Question("Why is the sky blue?", person1, "http://foo.bar/baz", now());
    Question question2 = new Question("Why is Mars red?", person1, now());
    
    client.createQuestions(question1, question2);
  }

  // TODO Add a setup method where we do a basic sanity to confirm that the remote host is 
  //      online and ready for testing.
  // TODO Assumes that test data is present.
  @Test
  public void getAllQuestions()
  {
    List<Question> questions = client.getQuestions();
    
    assertFalse(questions.isEmpty());
    
    System.out.println("Questions: " + questions);
  }
}
