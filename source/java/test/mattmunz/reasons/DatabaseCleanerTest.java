package mattmunz.reasons;

import static org.junit.runners.MethodSorters.NAME_ASCENDING;
import mattmunz.reasons.server.repository.PersonRepository;
import mattmunz.reasons.server.repository.QuestionRepository;

import org.junit.FixMethodOrder;
import org.junit.Test;

/**
 * A utility for removing invalid test data from the database
 */
@FixMethodOrder(NAME_ASCENDING)
public class DatabaseCleanerTest
{
  @Test
  public void test1CleanQuestions()
  {
    new QuestionRepository().removeInvalidQuestions();
  }

  @Test
  public void test2CleanPeople()
  {
    new PersonRepository().removeInvalidPeople();
  }
}
