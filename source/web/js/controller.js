// Depends on view.js

'use strict';

// TODO Adopt a UI framework like angular

/**
 * Used to detect Auth state change events that do not involve a user status change 
 * (e.g. programmatic token refresh).
 */
var currentUserID;
var currentQuestionID;

function createQuestionListElement(question, includeID) {	
  return getQuestionListElement(question.id, question.text, question.author, 
                                question.submitterName || 'Anonymous', 
                                getAttributionURLText(question.attributionURL), 
                                question.submitterPicture || './images/silhouette.jpg', 
                                getSubmissionDateText(question.submissionDate), includeID);  
}

function createDebugAnswersElement(statementID) {
  var statement = statementsByID[statementID];
  return getDebugStatementListElement(statement.id, statement.text, statement.author, 
      statement.submitterName || 'Anonymous', 
      getAttributionURLText(statement.attributionURL), 
      statement.submitterPicture || './images/silhouette.jpg', 
      getSubmissionDateText(statement.submissionDate));
}

// Starts listening for new questions and populates the questions list.
function startDBQueries() {	  
  var questionAddHandler = function(question) {
	  var tableBodyElement = getFirstChildElement(elements.recentQuestionsSection, 'question-list-table-body');
	  tableBodyElement.insertBefore(createQuestionListElement(question, false), tableBodyElement.firstChild);
	  
    var tableBodyElement2 = getFirstChildElement(elements.debugSection, 'debug-questions-table-body');
    tableBodyElement2.insertBefore(createQuestionListElement(question, true), 
                                   tableBodyElement2.firstChild);
  };
  
  var questionUpdateHandler = function(question) {    
    var tableBodyElement = elements.recentQuestionsSection.getElementsByClassName('question-list-table-body')[0];
    var questionElement = tableBodyElement.getElementsByClassName('question-' + question.id)[0];
    questionElement.getElementsByClassName('text')[0].innerText = question.text;
    questionElement.getElementsByClassName('submitter')[0].innerText = question.submitterName;
  };
    
  var questionRemoveHandler = function(questionID) {    
    var tableBodyElement = sectionElement.getElementsByClassName('question-list-table-body')[0];
    var questionElement = tableBodyElement.getElementsByClassName('question-' + questionID)[0];
    questionElement.parentElement.removeChild(questionElement);
  };
  
  var answerAddHandler = function(statementID) {
    var tableBodyElement = getFirstChildElement(elements.debugSection, 'debug-statements-table-body');
    tableBodyElement.insertBefore(createDebugAnswersElement(statementID), 
                                  tableBodyElement.firstChild);
  };
  
  addDataObservers(questionAddHandler, questionUpdateHandler, questionRemoveHandler, 
                   answerAddHandler);
}

/**
 * Cleanups the UI and removes all Firebase listeners.
 */
function cleanupUi() {
  // Remove all previously displayed posts.
  getFirstChildElement(elements.recentQuestionsSection, 'question-list-table-body').innerHTML = '';  
  removeDataObservers();
}

/**
 * Triggers every time there is a change in the Firebase auth state (i.e. user signed-in or user 
 * signed out).
 */
function onAuthStateChanged(user) {
  // Ignore token refresh events.
  if (user && currentUserID === user.uid) { return; }

  cleanupUi();

  if (user) {
    currentUserID = user.uid;
    elements.splashPage.style.display = 'none';
    updateUser(user.uid, user.displayName, user.email, user.photoURL);
    startDBQueries();
    return;
  } 
  
  currentUserID = null;
  // Display the splash page where you can sign-in.
  elements.splashPage.style.display = '';  
}

function showQuestionDetail(questionID) {
  while (elements.questionDetailSection.hasChildNodes()) {
    elements.questionDetailSection.removeChild(elements.questionDetailSection.lastChild);
  }  
  
  var questions = questionsByID[questionID];  
  var answerIDs = answerIDsByQuestionID[questionID];
  console.log("showQuestionDetail: question: " + questions + ", answers: " + answerIDs 
              + ", a.size: " + answerIDs.size);   
  var answers = Array.from(answerIDs).map(function(id) { return statementsByID[id]; });
  
  elements.questionDetailSection.appendChild(getQuestionDetailElement(questions, answers));
  showSection(elements.questionDetailSection);
}

function showAddAnswerForm(questionID) {
  showSection(elements.addAnswerElement);
  elements.answerTextInput.value = '';
  elements.answerAuthorInput.value = '';
  currentQuestionID = questionID;
}

// Create bindings on load
window.addEventListener('load', function() {
  elements.signInButton.addEventListener('click', function() {
	 firebase.auth().signInWithPopup(new firebase.auth.GoogleAuthProvider());
  });
  elements.signOutButton.addEventListener('click', function() { firebase.auth().signOut(); });
	
  firebase.auth().onAuthStateChanged(onAuthStateChanged);
	
  // Saves question on form submit
  elements.questionForm.onsubmit = function(e) {
    e.preventDefault();
    var text = elements.questionTextInput.value;
    var questioner = elements.questionQuestionerInput.value;
    var submissionDate = new Date();
    var attributionURL = elements.questionAttributionURLInput.value
    if (text) 
    {
      addQuestionForCurrentUser(text, questioner, attributionURL, submissionDate)
        .then(function() { elements.recentMenuButton.click(); });
	    elements.questionTextInput.value = '';
	    elements.questionQuestionerInput.value = '';
    }
  };

  elements.addAnswerForm.onsubmit = function(e) {
    e.preventDefault();
    var text = elements.answerTextInput.value;
    var author = elements.answerAuthorInput.value;
    var submissionDate = new Date();
    var attributionURL = elements.answerAttributionURLInput.value;
    if (text) 
    {
      addAnswerForCurrentUser(currentQuestionID, text, author, attributionURL, submissionDate)
        .then(function() { elements.recentMenuButton.click(); });
      elements.answerTextInput.value = '';
      elements.answerAuthorInput.value = '';
    }
  };

  elements.recentMenuButton.onclick = function() { 
    showSection(elements.recentQuestionsSection, elements.recentMenuButton); 
  };
  elements.debugMenuButton.onclick = function() { 
    showSection(elements.debugSection, elements.debugMenuButton); 
  };
  elements.addButton.onclick = function() {
    showSection(elements.addQuestionElement);
    elements.questionTextInput.value = '';
    elements.questionQuestionerInput.value = '';
  };
  elements.recentMenuButton.onclick();
  elements.feedbackMenuButton.onclick = function() { 
    showSection(elements.feedbackSection, elements.feedbackMenuButton); 
  };

}, false);

