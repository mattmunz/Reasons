package mattmunz.reasons;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import mattmunz.property.PropertiedObject;
import mattmunz.property.Property;
import mattmunz.property.PropertyListBuilder;

public class Question implements PropertiedObject
{
  // TODO Use URL class for URL?
  // TODO Make URL a Optional since it is allowed to be null. How to handle that with Jackson?
  private final String text, attributionURL;
  private final Person questioner;
  
  // TODO Null allowed. Use Optional?
  private final String identifier;
  
  private final ZonedDateTime submissionDate;
  
  public Question(String text, Person questioner, String attributionURL, ZonedDateTime submissionDate)
  {
    this(null, text, questioner, attributionURL, submissionDate);
  }

  public Question(String text, Person questioner, ZonedDateTime submissionDate) 
  { 
  	this(text, questioner, null, submissionDate); 
  }

  public Question(String identifier, String text, Person questioner, String attributionURL, 
  							  ZonedDateTime submissionDate)
  {
    this.identifier = identifier;
    this.text = text;
    this.attributionURL = attributionURL;
    
    if (questioner == null) 
    { 
      throw new IllegalArgumentException("Null not allowed for questioner"); 
    }
    
    this.questioner = questioner;
    
    if (submissionDate == null) 
    { 
      throw new IllegalArgumentException("Null not allowed for submissionDate"); 
    }
    
    this.submissionDate = submissionDate;
  }

  public Question(Question question, Person questioner)
	{
  	this(question.getIdentifier(), question.getText(), questioner, question.getAttributionURL(), 
  			 question.getSubmissionDate());
	}

	public String getText() { return text; }

  /**
   * @return May be null
   */
  public String getAttributionURL() { return attributionURL; }

  public Person getQuestioner() { return questioner; }

  public String getIdentifier() { return identifier; }

	public ZonedDateTime getSubmissionDate() { return submissionDate; }

  @Override
  public String toString() { return getToStringText(); }

  @Override
  public int hashCode() { return getHashCode(); }

  @Override
  public boolean equals(Object other) { return isEqualTo(other); }
  
  @JsonIgnore
  @Override
  public List<Property> getProperties()
  {
    return new PropertyListBuilder().add("identifier", identifier).add("text", text)
                                    .add("questioner", questioner)
                                    .add("attributionURL", attributionURL).build();
  }
}
