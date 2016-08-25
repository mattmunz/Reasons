package mattmunz.reasons.server.repository;

import static com.google.common.collect.Lists.newArrayList;

import java.time.ZonedDateTime;
import java.util.List;

import mattmunz.persistence.mongodb.TimeMarshaller;
import mattmunz.reasons.Person;
import mattmunz.reasons.Question;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;

public class QuestionMarshaller
{
  static final String TEXT_ATTRIBUTE_NAME = "text";
  private static final String IDENTIFIER_ATTRIBUTE_NAME = "_id";
  private static final String QUESTIONER_IDENTIFIER_ATTRIBUTE_NAME = "questionerIdentifier";
  private static final String ATTRIBUTION_URL_ATTRIBUTE_NAME = "attributionURL";
  private static final String SUBMISSION_DATE_ATTRIBUTE_NAME = "submissionDate";
  
  private final TimeMarshaller timeMarshaller = new TimeMarshaller();

  List<Question> createQuestions(FindIterable<Document> documents)
  {
    return newArrayList(documents.map(this::createQuestion));
  }

  // TODO Can use Jackson for serialization instead????
  // TODO Handle nulls better?
  Document createDocument(Question question)
  {
    return new Document(TEXT_ATTRIBUTE_NAME, question.getText())
                .append(QUESTIONER_IDENTIFIER_ATTRIBUTE_NAME, question.getQuestioner().getIdentifier())
                .append(ATTRIBUTION_URL_ATTRIBUTE_NAME, question.getAttributionURL())
    						.append(SUBMISSION_DATE_ATTRIBUTE_NAME, timeMarshaller.getTimeText(question.getSubmissionDate()));
  }

  // TODO Replace with Jackson?
  Question createQuestion(Document document) {
    
    String identifier = document.get(IDENTIFIER_ATTRIBUTE_NAME, ObjectId.class).toString();
    String text = document.get(TEXT_ATTRIBUTE_NAME, String.class);
    String questionerIdentifier = document.get(QUESTIONER_IDENTIFIER_ATTRIBUTE_NAME, String.class);
    String attributionURL = document.get(ATTRIBUTION_URL_ATTRIBUTE_NAME, String.class);
    String submissionDateText = document.get(SUBMISSION_DATE_ATTRIBUTE_NAME, String.class);

    Person questioner = new Person(null, questionerIdentifier);
		ZonedDateTime submissionDate = timeMarshaller.parseDate(submissionDateText);

		return new Question(identifier, text, questioner, attributionURL, submissionDate);
  }
}
