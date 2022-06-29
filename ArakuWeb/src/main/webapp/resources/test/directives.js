angular.module('todo').directive('todoTitle', function() {
	return {
		template: '<h2>Todo</h2>'
	}
});

angular.module('todo').directive('todoItem', function() {
	return {
//		templateUrl:svr+'/resources/test/todoItem.html'
		templateUrl:svr+'/resources/test/todoItem2.html'
		/*
		template: '<div class="input-group">                                                            '
					+'	<span class="input-group-addon">                                                   '
					+'		<input type="checkbox" ng-model="todo.completed">                              '
					+'	</span>                                                                            '
					+'	<input type="text" class="form-control" ng-model="todo.title">                     '
					+'	<span class="input-group-btn">                                                     '
					+'		<button class="btn btn-danger" type="button" ng-click="remove(todo)">X</button>'
					+'	</span>                                                                            '
					+'</div>                                                                               '
		*/
	}
});

angular.module('todo').directive('todoFilters', function() {
	return {
		templateUrl:svr+'/resources/test/todoFilters.html'
	}
});

angular.module('todo').directive('todoForm', function() {
	return {
		templateUrl:svr+'/resources/test/todoForm.html'
	}
});