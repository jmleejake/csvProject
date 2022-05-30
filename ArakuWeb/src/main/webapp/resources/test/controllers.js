/**
 * 
 */

angular.module('todo').controller('TodoCtrl', function($scope, todoService) {
	// $scope.name = "Jake!!!";
	$scope.todos = todoService.get();
	todoService.getSomething('/araku/ang/getTodo');
	const data = {
			seq_id: '3'
	}
	todoService.postSomething('/araku/ang/delTodo', data);
	
	$scope.remove = function(todo) {
		todoService.remove(todo);
	}
	
	$scope.add = function(title) {
		todoService.add(title);
		$scope.newTodoTitle = "";
	}
	
	$scope.update = function() {
		todoService.update();
	}
	
});