package mattmunz.reasons;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * To be used in the request to create a new question
 */
public class QuestionData
{
  private final String text, attributionURL, questionerIdentifier, questionerName;
  
  public QuestionData(@JsonProperty("text") String text, 
                      @JsonProperty("questionerIdentifier") String questionerIdentifier, 
                      @JsonProperty("questionerName") String questionerName, 
                      @JsonProperty("attributionURL") String attributionURL)
  {
    this.text = text;
    this.attributionURL = attributionURL;
    this.questionerIdentifier = questionerIdentifier;
    this.questionerName = questionerName;
  }
  
  // TODO Would really like to use Optional here... Ideally would use either type.
  public String getQuestionerIdentifier() { return questionerIdentifier; }

  // TODO Would really like to use Optional here...
  public String getQuestionerName() { return questionerName; }

  public String getText() { return text; }

  public String getAttributionURL() { return attributionURL; }
}
