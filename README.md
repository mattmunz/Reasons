# Reasons

A system to collect and refine questions, answers, and reasons.

## Release Notes

7/11/16 Initial project publication, including design documents 

## Status

There are currently just some ideas and preliminary designs and UI mockups (see below).

## Motivations

### Initial idea

Many years ago, an idea that started me down the road to the notion of the Reasons project was an idea for a website that one could go to get a concise explication of the case, for and against, a 
proposition such as "You should vote for Ralph Nader". 

On the page for the proposition, you could click "I agree", "I disagree", or "tell me more" for any of the supporting arguments, which themselves would have pages like 
"Voting is a responsibility of all citizens" or "Beltway experience is necessary for a 
president to be successful".     

This system seemed like a better way to make up my mind than dredging through news articles and 
books, which can be fun to read at times, but often contain lots of information I don't 
really need. This makes sense as these sources are designed for a wide audience and generally don't leverage all of the flexibility w.r.t. content that HTML/HTTP offers.

In contrast to those more traditional sources, this hypothetical website could be personalized. By knowing your favored set of propositions (let's just call them beliefs) the site could more smoothly navigate you towards new ideas (even seemingly foreign ones) that you could adopt. Likewise, the system could be used to intentionally browse for ideas that challenge your mindset -- ideas that lie on the boundary between your world view and another -- ideas that require you to challenge some (but not too many) of your beliefs.     

### Goals

* Create a system that promotes positive/true memes<sup>1</sup>
* Create an efficient means of communicating arguments
* Create a database of arguments from which new insights can be derived
* Create a means of reducing spam (including redundancy) in internet communications, in part 
  by creating a sink for such low-information content
  
### Non-goals

* Develop some sort of tool for formal reasoning
* Help bots and other machines in any way. This system is for people.  

## Design

See the [design](documentation/Design.md) page for details.

## Use Cases

1. Ask/show questions (The most basic use case). A website where you can ask questions and see the list of questions that have been asked including information about who asked the question and when they asked it.
  1. Ask a Question
     1. Web Service
         1. Design 
         2. Implementation   
     2. Web UI
         1. Design X
         2. Implementation
  2. List all Questions
     1. Web Service
         1. Design X
         2. Implementation  
     2. Web UI
         1. Design X
         2. Implementation

## TODO 

* Implement the most basic use case (Questions support)
  1. Make a service API and client -- to run locally, TDD
     1. In-memory -- Reuse Jot code
         1. persons: 
           1. POST -- continue at PersonsEndpointTest. Get it to pass...
           2. GET 
         2. questions: POST, GET
     2. With persistence 
  2. Implement Angular UI to access that API  
* Prioritize this list
* Add these questions
  * Should I use Dropbox to replicate my github workspace?
  * What is the Reasons project?
  * Why should I be interested in the reasons project?
  * Why should I contribute to the Reasons project?
  * Why did I publish the cli project?
  * What's a good tutorial for the Reddit API?
* Move the design methodology page -- should probably just be a link on the design page to a general mattmunz methodology page
* This project needs a better name?
* Identify more use cases
  * Implement authentication
  * Deploy to a shared instance (hosted, perhaps Firebase)
  * Get a domain name  
  * Support for plain propositions
  * Support for supporting arguments
  * Support for arguments against the proposition
  * Implement the following arguments/questions "What is the Reasons project?", 
    "Why should I be interested in the reasons project?", "Why should I contribute to the Reasons project?", "should jury selection take demographics of the community into account?", and all questions from design mockup.
  * Search of various kinds
  * Deduplication
  * Voting/moderation 
* Explain the domain model syntax
* Replace the API design with standard description format like Swagger
* Look into integration with Wikidata?
* ... 

## Contact

Please contact [Matt Munz](https://github.com/mattmunz) if you have any difficulty or 
to provide feedback (which would be very welcome).

## Author

[Matt Munz](https://github.com/mattmunz)

------------------------

1) [Meme, as described by Dawkins](https://en.wikipedia.org/wiki/Meme)
