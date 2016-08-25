
app.controller('reasonsController', function($scope, $window, $http) {

  var questionsURL = "../questions";

  $scope.author = 'Matt Munz';
  
  // TODO Hardcoded!
  $scope.user = { firstName: 'Matt', lastName: 'Munz' };
  
  $scope.newQuestionText = "";
  $scope.newQuestionQuestionerName = "";
  $scope.newQuestionAttributionURL = "";

  $scope.createQuestion = function() {
  
    var successCallback = function(response) { /* TODO Log instead: alert("Add question succeeded."); */ };
    var errorCallback = function(response) { 

      // TODO For some reason this alert shows even when the query succeeds!
      // alert("Add question failed. Response: status: " + response.status + ", data: " + response.data); 
    };
    
    var newQuestion = { 
    		
      text: $scope.newQuestionText, questionerName: $scope.newQuestionQuestionerName, 
      attributionURL: $scope.newQuestionAttributionURL
    };
  
    $http.post(questionsURL, newQuestion).then(successCallback, errorCallback);
           
    $window.location.reload();
  };
  
  $http.get(questionsURL).then(function(response) { $scope.questions = response.data; });
});
