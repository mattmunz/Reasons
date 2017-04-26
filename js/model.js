'use strict';

function createStatement(id, text, attributionURL, submissionDate, author, submitterID, 
                         submitterName, submitterPicture) {
  var statement = {
    text: text,
    attributionURL: attributionURL,    
    submissionDate: submissionDate,
    author: author,
    submitterID: submitterID,
    submitterName: submitterName,
    submitterPicture: submitterPicture,
  };
  
  if (id) { statement.id = id; }
  
  return statement;
}
