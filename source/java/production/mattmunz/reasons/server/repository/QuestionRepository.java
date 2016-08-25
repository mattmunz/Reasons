package mattmunz.reasons.server.repository;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static java.lang.String.format;

import java.util.List; 

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import mattmunz.persistence.mongodb.MongoClientFactory;
import mattmunz.persistence.mongodb.MongoRepository;
import mattmunz.reasons.Question;

public class QuestionRepository extends MongoRepository
{
  private final QuestionMarshaller questionMarshaller = new QuestionMarshaller();

  public QuestionRepository() { this(new MongoClientFactory()); }

  public QuestionRepository(MongoClientFactory clientFactory)
  {
    super(clientFactory, "reasons", "questions");
  }

  public List<Question> get() { return getWithCollection(this::get); }
  
  public Question add(Question question) { return applyWithCollection(this::add, question); }

  /**
   * Remove all questions where questionerIdentifier is null
   */
  public void removeInvalidQuestions()
  {
    doWithCollection(this::removeInvalidQuestions);
  }
  
  private List<Question> get(MongoCollection<Document> collection)
  {
    return questionMarshaller.createQuestions(collection.find());
  }

  private Question add(MongoCollection<Document> collection, Question question)
  {
    Document document = questionMarshaller.createDocument(question);
    collection.insertOne(document);
    
    return questionMarshaller.createQuestion(document);
  }
  
  private void removeInvalidQuestions(MongoCollection<Document> collection)
  {
    DeleteResult result 
    	= collection.deleteMany(or(eq("questionerIdentifier", null), 
    														 eq("submissionDate", null)));
    
    // TODO Log
    System.out.println(format("%d invalid questions were deleted", result.getDeletedCount()));
  }
}
