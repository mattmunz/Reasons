package mattmunz.reasons.server.repository;

import static com.google.common.collect.Lists.newArrayList;  

import java.util.List;

import mattmunz.reasons.Person;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;

public class PersonMarshaller
{
  static final String NAME_ATTRIBUTE_NAME = "name";
  // TODO Can refactor with similar classes?
  private static final String IDENTIFIER_ATTRIBUTE_NAME = "_id";

  List<Person> createPeople(FindIterable<Document> documents)
  {
    return newArrayList(documents.map(this::createPerson));
  }

  // TODO Can use Jackson for serialization instead????
  Document createDocument(Person person)
  {
    return new Document(NAME_ATTRIBUTE_NAME, person.getName());
  }
  
  // TODO Replace with Jackson?
  Person createPerson(Document document) {
    
    String identifier = document.get(IDENTIFIER_ATTRIBUTE_NAME, ObjectId.class).toString();
    String name = document.get(NAME_ATTRIBUTE_NAME, String.class);
    
    return new Person(name, identifier);
  }
}
