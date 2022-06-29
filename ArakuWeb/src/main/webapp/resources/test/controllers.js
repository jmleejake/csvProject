/**
 * 
 */

angular.module('todo').controller('TodoCtrl', function($scope, todoService) {
	// $scope.name = "Jake!!!";
	$scope.todos = todoService.get();
	
	$scope.remove = function(todo) {
//		todoService.remove(todo);
		
		const data = {
				seq_id: todo.seq_id
		}
		todoService.postSomething('/araku/ang/delTodo', data);
	}
	
	$scope.add = function(title) {
//		todoService.add(title);
		
		const data = {
				cntnt: title
		}
		todoService.postSomething('/araku/ang/maniTodo', data);
		$scope.newTodoTitle = "";
	}
	
	$scope.update = function() {
		todoService.update();
	}
	
	$scope.update2 = function(todo) {
		const data = {
				seq_id: todo.seq_id
				, cntnt: todo.cntnt
		}
		todoService.postSomething('/araku/ang/maniTodo', data);
	}
	
});