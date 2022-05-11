/**
 * 앵귤러 학습 자바스크립트!!
 */
const app = angular.module('todo', []);

app.controller('TodoCtrl', function($scope, todoService) {
	// $scope.name = "Jake!!!";
	$scope.todos = todoService.getFromStorage();
	
	$scope.remove = function(todo) {
		todoService.removeFromStorage(todo);
	}
	
	$scope.add = function(title) {
		todoService.addToStorage(title);
		$scope.newTodoTitle = "";
	}
	
	$scope.update = function() {
		todoService.updateToStorage();
	}
	
});
