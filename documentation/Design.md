# Design

## Methodology

The general approach is to start with the simplest system that can do anything useful in 
the direction of the project's goal. After that, it's just a matter of making similar 
incremental steps, at each point reevaluating the goals of the project and its design and 
implementation. As much as possible, this development should be done in the open.

## Domain model

_TODO The formatting/notation here is a little hard to read and should be improved._

All objects have a field like the following unless stated otherwise.

* Identifier (unique)

### Person

* Name 

### Question

* Text
* Questioner :: Person
* AttributionURL (Optional) -- _A resource that supports the claim that the questioner is in fact the author of the question._

### PersonRepository

* create :: (Name) -> Person -- _TODO Authentication should be used here to prevent impersonation._
* get :: (Name) -> Person 

### QuestionRepository

* create :: (Text, Person, URL) -> Question | AlreadyExistsError(Question)
* get :: [Question] 

### Web Service APIs

For the following methods, all responses/request bodies are in JSON.

* reasons/api 
  * persons _-> PersonRepository TODO How to model lazy create functaionality?_
     * GET: _-> PersonRepository.get_
     * POST: _-> PersonRepository.create_  
  * questions _-> QuestionRepository_
     * GET: _-> QuestionRepository.get_
     * POST: _-> QuestionRepository.create_ 

## UI 

These [UI mockups](https://cdn.rawgit.com/mattmunz/Reasons/master/documentation/ReasonsHome.Mockup.html) are really rough but should give a sense of 
how the application is intended to work, structurally. 

## Author

[Matt Munz](https://github.com/mattmunz)
