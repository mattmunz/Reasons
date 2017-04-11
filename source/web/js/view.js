'use strict';

// Depends on model.js

function importDocuments() {
  var container = document.getElementById("content-container"); 
  var importedSplashHeader = document.querySelector('link#splash_and_header-link').import;	
	
  insertImportedElement(container, importedSplashHeader.querySelector('.header'));
	
	// TODO This variable is defined to support mockups only and should be removed.
	if (!USE_SPLASH) { return; }
	
	insertImportedElement(container, importedSplashHeader.querySelector('section#page-splash'));
}

importDocuments();

// Shortcuts to DOM Elements
var elements = {
  questionForm:  document.getElementById('message-form'),
  addAnswerForm: document.getElementById('add-answer-form'),

  addQuestionElement:          document.getElementById('add-question'),
  questionTextInput:           document.getElementById('new-question-title'),
  questionQuestionerInput:     document.getElementById('new-question-questioner'),
  questionAttributionURLInput: document.getElementById('new-question-attribution-url'),

  addAnswerElement:          document.getElementById('add-statement'),
  answerTextInput:           document.getElementById('new-answer-text'),
  answerAuthorInput:         document.getElementById('new-answer-author'),
  answerAttributionURLInput: document.getElementById('new-answer-attribution-url'),

  signInButton:  document.getElementById('sign-in-button'),
  signOutButton: document.getElementById('sign-out-button'),
  splashPage:    document.getElementById('page-splash'),

  addButton:              document.getElementById('add'),
  recentQuestionsSection: document.getElementById('questions-list'),
  recentMenuButton:       document.getElementById('menu-recent'),
  debugMenuButton:        document.getElementById('menu-debug'),
  debugSection:           document.getElementById('debug'),
  questionDetailSection:  document.getElementById('question-detail'),
  feedbackMenuButton:     document.getElementById('menu-feedback'),
  feedbackSection:        document.getElementById('feedback'),
}

function getTDHTML(body) {
  return '<td class="mdl-data-table__cell--non-numeric">' + body + '</td>';	
}

function getSpanHTML(className) { return '<span class="' + className + '"></span>'; }

function getPHTML(className) { return '<p class="' + className + '"></p>'; }

/**
 * Displays the given section element and changes styling of the given button.
 */
function showSection(sectionElement, buttonElement) {
  elements.recentQuestionsSection.style.display = 'none';
  elements.debugSection.style.display = 'none';
  elements.addQuestionElement.style.display = 'none';
  elements.addAnswerElement.style.display = 'none';
  elements.questionDetailSection.style.display = 'none';
  elements.feedbackSection.style.display = 'none';
  elements.recentMenuButton.classList.remove('is-active');

  if (sectionElement) { sectionElement.style.display = 'block'; }
  if (buttonElement) { buttonElement.classList.add('is-active'); }
}

function setInnerText(parentElement, className, text) {
  getFirstChildElement(parentElement, className).innerText = text;	
}

function getFirstChildElement(parentElement, className) {
  return parentElement.getElementsByClassName(className)[0]	
}

// Create an element from raw HTML
function createElement(parentElementName, html) {
  var parent = document.createElement(parentElementName);
  parent.innerHTML = html;
  return parent.firstChild;	
}

// TODO Refactor to take a statement argument
function getQuestionListElement(questionID, text, questioner, submitter, attributionURL, 
                                submitterPictureURL, submissionDateText, includeID) {
  var questionListElement = createElement('tbody', getQuestionListRowHTML(questionID, includeID));
  // TODO This is super hacky. Should really switch to angular or something.
  setInnerText(questionListElement, 'text', text);
  setInnerText(questionListElement, 'questioner', questioner);
  setInnerText(questionListElement, 'submitter', submitter);
  setInnerText(questionListElement, 'attribution-url', attributionURL);
  getFirstChildElement(questionListElement, 'avatar').style.backgroundImage 
    = 'url("' + submitterPictureURL + '")';
  setInnerText(questionListElement, 'submission-date', submissionDateText);  
  return questionListElement;    
}

// TODO Refactor to take a statement argument, merge with above
function getDebugStatementListElement(statementID, text, author, submitter, attributionURL, 
                                      submitterPictureURL, submissionDateText) {
  var statementListElement = createElement('tbody', getStatementListRowHTML(statementID));
  // TODO This is unwieldy. Should really switch to angular or something.
  setInnerText(statementListElement, 'text', text);
  setInnerText(statementListElement, 'author', author);
  setInnerText(statementListElement, 'submitter', submitter);
  setInnerText(statementListElement, 'attribution-url', attributionURL);
  getFirstChildElement(statementListElement, 'avatar').style.backgroundImage 
    = 'url("' + submitterPictureURL + '")';
  setInnerText(statementListElement, 'submission-date', submissionDateText);  
  return statementListElement;    
}

// TODO Refactor this to make two similar methods for debug statements and debug questions
function getQuestionListRowHTML(questionID, includeID) { 
  // TODO Refactor for readability
  var body = '';
  if (includeID) { body += '  ' + getTDHTML('' + questionID + '') + "\n"; }

  var textRowHTML = getTDHTML('<a href="#" class="text" onclick="showQuestionDetail(\'' 
                              + questionID + '\')"></a>');
  body += '  ' + textRowHTML + "\n"
          + '  ' + getTDHTML(getSpanHTML('questioner')) + "\n"
          + '  ' + getTDHTML(getSpanHTML('avatar') + getSpanHTML('submitter')) + "\n"
          + '  ' + getTDHTML(getSpanHTML('submission-date')) + "\n"
          // TODO Make a real hyperlink
          + '  ' + getTDHTML(getSpanHTML('attribution-url')) + "\n"  

  return '<tr class="question question-' + questionID + '">\n' + body + '</tr>';
}

function getStatementListRowHTML(statementID) {  
  return '<tr class="statement statement-' + statementID + '">'
      // TODO Refactor for readability
      + '  ' + getTDHTML('' + statementID + '') + "\n"
      + '  ' + getTDHTML('<a href="#" class="text" onclick="showStatementDetail(\'' + statementID + '\')"></a>') + "\n"
      + '  ' + getTDHTML(getSpanHTML('author')) + "\n"
      + '  ' + getTDHTML(getSpanHTML('avatar') + getSpanHTML('submitter')) + "\n"
      + '  ' + getTDHTML(getSpanHTML('submission-date')) + "\n"
      // TODO Make a real hyperlink
      + '  ' + getTDHTML(getSpanHTML('attribution-url')) + "\n"
      + '</tr>';
}

function insertImportedElement(containerElement, importedElement) {
  containerElement.insertBefore(importedElement.cloneNode(true), containerElement.firstElementChild);  
}

function showFeedbackView()
{
  var url = "mailto:" + reasonsConfiguration.feedbackEmail + "?subject=Reasons:%20Feedback";
  window.open(url, '_blank');
}

// TODO Improve formatting. This would be better with Angular's date formatters.
function getSubmissionDateText(date) {
  if (!date) { return ''; }
  date = new Date(date);
  var hours = date.getHours();
  var tod = '';
  if (hours > 11) { tod = 'PM'; } else { tod = 'AM'; }
  return '' + (date.getMonth() + 1) + '/' + date.getDate() + '/' + date.getFullYear() + ' ' 
         + (hours % 12) + ":" + pad(date.getMinutes(), 2) + ' ' + tod;
}

function pad(num, size) {
  var text = "00" + num;
  return text.substr(text.length - size);
}

function getAttributionURLText(attributionURL) {
  if (!attributionURL) { return ''; }
  return attributionURL;  
}

function getQuestionDetailElement(question, answers /* Array of Statement */) {
  return createElement('section', getQuestionDetailHTML(question, answers));
}

// TODO Refactor. The styling is not great here. The text is too big/clunky.
function getQuestionDetailHTML(question, answers /* Array of Statement */) {
  var answersListHTML = getAnswersListHTML(question, answers);
  return '<div>\n' 
         + '  <h1>' + question.text + '</h1>\n'
         + '  <p>By ' + (question.author || '?') + '; ' + 'Submitted ' 
         + getSubmissionDateText(question.submissionDate) + ' by ' + question.submitterName + '</p>\n'
         // TODO Implement question "editing"
         + '  <p>[<a href="#">Edit</a>]</p>\n'
         + '  <h2>Answers</h2>\n' + answersListHTML 
         // TODO Implement. This will need some sort of questions-similar questions join object
         //      Will also need some sort of backend process that identifies similar questions.
         + '  <h2>Similar Questions</h2>\n'
         + '  <ul>\n'
         + '    <li><a href="#">What makes the sky blue?</a></li>\n'
         + '    <li><a href="#">What color is the sky usually, and why?</a></li>\n'
         + '  </ul>\n'  
         + '</div>';
}

function getAnswersListHTML(question, answers /* Array of Statement */) {
  console.log("getAnswersListHTML: start. question: " + question + ", a.size: " + answers.size);
  var answersListHTML = '  <ul>\n';  
  for (const answer of answers) {
    console.log("getAnswersListHTML: rendering answer: " + answer);
    answersListHTML += getAnswersListItemHTML(answer) + '\n';
  }
  return answersListHTML + '    ' + '<li>[<a href="#" onclick="showAddAnswerForm(\'' + question.id 
         + '\')">Add an answer</a>]</li>\n' + '  </ul>\n';
}

function getAnswersListItemHTML(answer /* Statement */) {
  // TODO Replace mock URL with actual detail view for answers/statements
  return '<li><a href="mockups/statement_detail.html">' 
         + answer.text + '</a> ' 
         + '[<a href="#">Sounds right</a>] [<a href="#">Sounds wrong</a>]</li>' 
}

