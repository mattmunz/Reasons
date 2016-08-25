package mattmunz.reasons.server.repository;

import static com.mongodb.client.model.Filters.eq; 
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;   
import java.util.Set;

import mattmunz.persistence.mongodb.MongoClientFactory;
import mattmunz.persistence.mongodb.MongoRepository;
import mattmunz.reasons.Person;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

/**
 * TODO Use Spring Data. By this tutorial, it looks much better: http://spring.io/guides/gs/accessing-data-mongodb/
 */
public class PersonRepository extends MongoRepository
{
  private final PersonMarshaller personMarshaller = new PersonMarshaller();
  
  public PersonRepository() { this(new MongoClientFactory()); }

  public PersonRepository(MongoClientFactory clientFactory)
  {
    super(clientFactory, "reasons", "people");
  }

  /**
   * Lazily creates the person.
   * 
   * TODO This method probably needs a better name.
   */
  public Person add(Person person) 
  { 
    // TODO There's probably a more efficient way to lazy create...
    List<Person> people = getByName(person.getName());
    
    if (!people.isEmpty()) { return people.get(0); }
    
    return applyWithCollection(this::add, person); 
  }

  public List<Person> get() { return getWithCollection(this::get); }

  public Person getByIdentifier(String identifier)
  {
    return applyWithCollection(this::getByIdentifier, identifier);
  }
    
  public List<Person> getByName(String name)
  {
    return applyWithCollection(this::getByName, name);
  }

  public void removeAll() { doWithCollection(MongoCollection::drop); }
  
  /**
   * Remove all people with duplicate names
   */
  public void removeInvalidPeople() { doWithCollection(this::removeInvalidPeople); }

  private List<Person> getByName(MongoCollection<Document> collection, String name)
  {
    return personMarshaller.createPeople(collection.find(eq("name", name)));
  }

  private Person getByIdentifier(MongoCollection<Document> collection, String identifier)
  {
    List<Person> people 
      = personMarshaller.createPeople(collection.find(eq("_id", new ObjectId(identifier))));
    
    if (people.size() != 1)
    {
      throw new RuntimeException("Expecting singleton but got: " + people);
    }
    
    return people.get(0);
  }
  
  private Person add(MongoCollection<Document> collection, Person person)
  {
    Document document = personMarshaller.createDocument(person);
    collection.insertOne(document);
    
    return personMarshaller.createPerson(document);
  }

  private void removeInvalidPeople(MongoCollection<Document> collection)
  {
    Set<String> duplicatedNames = new HashSet<>();
    Set<String> allNames = new HashSet<>();
    List<Person> people = get(collection);
    
    for (String name : people.stream().map(Person::getName).collect(toList()))
    {
      if (!allNames.add(name)) { duplicatedNames.add(name); }
    }
    
    // TODO Extract out field name "name"
    DeleteResult result = collection.deleteMany(Filters.in("name", duplicatedNames));
    
    // TODO Log
    System.out.println(format("%d invalid people were deleted", result.getDeletedCount()));
  }

  private List<Person> get(MongoCollection<Document> collection)
  {
    return personMarshaller.createPeople(collection.find());
  }
}
