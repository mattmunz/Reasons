'use strict';

// Depends on model.js

var QUESTIONS_PATH = 'questions';
var STATEMENTS_PATH = 'statements';
var QUESTION_ANSWERS_PATH = 'question-answers';

// TODO Implement the observer pattern fully for this...
var observerSubjects = []; // Refs that are being observed for changes

var questionsByID = {}; 
var answerIDsByQuestionID = {}; // question id -> set of statement ids
var statementsByID = {};

function unmarshalStatement(data) {
  var dataVal = data.val();
  // TODO Push this out to the view -- really view login anyway
  var submitterName = dataVal.submitterName || 'Anonymous';
  return createStatement(data.key, dataVal.text, dataVal.attributionURL, 
                         dataVal.submissionDate, dataVal.author, dataVal.submitterID, 
                         submitterName, dataVal.submitterPicture);
}

function addQuestion(question) {                                 
  var key = createKey('/' + QUESTIONS_PATH);
  // TODO Instead of duplicating, wouldn't it be better to reference by id here?
  // Write the new post's data simultaneously in the questions list and the user's question list.  
  var paths = ['/' + 'questions' +'/' + key, 
               // TODO Rework this path to be more ReSTy
               '/' + 'user-questions' + '/' + question.submitterID + '/' + key];
  return update(paths, question);
}

function addAnswer(questionID, statement) {                                 
  var key = createKey('/' + STATEMENTS_PATH);
  // TODO Instead of duplicating, wouldn't it be better to reference by id here?
  var paths = ['/' + STATEMENTS_PATH +'/' + key, 
               // TODO Rework this path to be more ReSTy
               '/user-statements/' + statement.submitterID + '/' + key,
               '/' + QUESTION_ANSWERS_PATH + '/' + questionID + '/' + key];
  return update(paths, statement);
}

function addQuestionForCurrentUser(text, author, attributionURL, submissionDate) {
  return addQuestion(createStatementForCurrentUser(text, author, attributionURL, submissionDate));
}

function addAnswerForCurrentUser(questionID, text, author, attributionURL, submissionDate) {
  var statement = createStatementForCurrentUser(text, author, attributionURL, submissionDate);                                
  return addAnswer(questionID, statement);
}

function createStatementForCurrentUser(text, author, attributionURL, submissionDate) {
  var currentUser = firebase.auth().currentUser;    
  var userID = currentUser.uid;
  return createStatement(null, text, attributionURL, submissionDate, author, userID, 
                         getUsername(userID), currentUser.photoURL);                                  
}

function updateUser(userId, name, email, imageUrl) {
  var attributes = { username: name, email: email, profile_picture: imageUrl };
  getRef('users/' + userId).set(attributes);
}

function addQuestionObservers(questionsRef, questionAddObserver, questionUpdateObserver, 
                              questionRemoveObserver, answerAddObserver) {
  questionsRef.on('child_added', function(data) {
    var question = unmarshalStatement(data);
    questionsByID[question.id] = question;
    questionAddObserver(question); 
    addAnswerObservers(question.id, answerAddObserver);
  });

  questionsRef.on('child_changed', function(data) {   
    var question = unmarshalStatement(data);
    questionsByID[question.id] = question;
    questionUpdateObserver(question);
  });

  questionsRef.on('child_removed', function(data) {
    questionID = data.key;
    delete questionsByID[questionID];
    delete answerIDsByQuestionID[questionID];
    questionRemoveObserver(questionID);
  });
}

function addAnswerObservers(questionID, answerAddObserver) {
  if (questionID in answerIDsByQuestionID) { return; }
  var answersRef = getRef(QUESTION_ANSWERS_PATH + "/" + questionID);
  answerIDsByQuestionID[questionID] = new Set();
  
  answersRef.on('child_added', function(data) {
    var statement = unmarshalStatement(data);   
    var statementID = statement.id;
    statementsByID[statementID] = statement;
    answerIDsByQuestionID[questionID].add(statementID);
    answerAddObserver(statementID);
  });

  answersRef.on('child_changed', function(data) {   
    answerIDsByQuestionID[questionID].add(data.statementID);
  });

  answersRef.on('child_removed', function(data) {
    answerIDsByQuestionID[questionID].delete(data.statementID);
  });

  observerSubjects.push(answersRef);
}

function addDataObservers(questionAddObserver, questionUpdateObserver, questionRemoveObserver, 
                          answerAddObserver) {
  var recentQuestionsRef = getRef(QUESTIONS_PATH).limitToLast(100);  
  addQuestionObservers(recentQuestionsRef, questionAddObserver, questionUpdateObserver, 
                       questionRemoveObserver, answerAddObserver);  
    
  observerSubjects.push(recentQuestionsRef);
}

// Stop all currently listening Firebase listeners.
function removeDataObservers() {
  observerSubjects.forEach(function(ref) { ref.off(); });
  observerSubjects = [];
}

function getRef(path) { 
  if (arguments.length === 0) { return firebase.database(); } 
  return firebase.database().ref(path); 
}

function update(paths, data) {
  var updates = {};
  paths.forEach(function(path) { updates[path] = data; });
  return firebase.database().ref().update(updates);
}

function createKey(path) { return getRef(path).push().key; }

function getUsername(userID) { 
  return getRef('/users/' + userID).once('value').then(function(snapshot) {
    return snapshot.val().username;
  });
}
