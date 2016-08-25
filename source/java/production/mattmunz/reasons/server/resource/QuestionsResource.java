package mattmunz.reasons.server.resource;

import static java.time.ZonedDateTime.now;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;  
import static javax.ws.rs.core.Response.created;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import mattmunz.reasons.Person;
import mattmunz.reasons.Question;
import mattmunz.reasons.QuestionData;
import mattmunz.reasons.server.repository.PersonRepository;
import mattmunz.reasons.server.repository.QuestionRepository;

@Path("questions")
public class QuestionsResource
{
  /** As needed, can inject this. */
  private final QuestionRepository questionRepository = new QuestionRepository();

  // TODO Inject this
  private final PersonRepository personRepository = new PersonRepository();

  @GET
  @Produces(APPLICATION_JSON)
  public List<Question> getQuestions() 
  { 
    return getQuestionsWithFullQuestioners(questionRepository.get()); 
  }

  @GET
  @Path("/{identifier}")
  @Produces(APPLICATION_JSON)
  public Question getQuestion(String identifier) 
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
  public Response add(QuestionData questionData) throws URISyntaxException 
  { 
    Question question = createQuestion(questionData, findQuestioner(questionData), now());
    
    // TODO Refactor URL
    URI uri = new URI("/Reasons/questions/" + questionRepository.add(question).getIdentifier());
    
    return created(uri).build();
  }

  private Person findQuestioner(QuestionData questionData)
  {
    return questionData.getQuestionerIdentifier() != null 
           ? personRepository.getByIdentifier(questionData.getQuestionerIdentifier())
           : personRepository.add(new Person(questionData.getQuestionerName()));
  }

  private Question 
  	createQuestion(QuestionData questionData, Person questioner, ZonedDateTime submissionDate)
  {
    return new Question(questionData.getText(), questioner, questionData.getAttributionURL(), submissionDate);
  }

  /**
   * For each questionerIdentifier, fetches the corresponding Person.
   */
  private List<Question> getQuestionsWithFullQuestioners(List<Question> questions)
  {
    Map<String, Person> questionersByIdentifier = new HashMap<>();
    List<Question> newQuestions = new ArrayList<>(questions.size());
    
    // TODO Would be nice to use streams/map here
    for (Question question : questions)
    {
    	newQuestions.add(getQuestionWithFullQuestioner(questionersByIdentifier, question));
    }
    
    return newQuestions;
  }

	private Question getQuestionWithFullQuestioner(Map<String, Person> peopleByIdentifier, Question question)
	{
		String questionerIdentifier = question.getQuestioner().getIdentifier();
		Person questioner = peopleByIdentifier.get(questionerIdentifier);
		
		if (questioner != null) { return new Question(question, questioner); }
		
		Person newQuestioner = personRepository.getByIdentifier(questionerIdentifier);
		
		peopleByIdentifier.put(newQuestioner.getIdentifier(), newQuestioner);
		
		return new Question(question, newQuestioner);
	}
}
