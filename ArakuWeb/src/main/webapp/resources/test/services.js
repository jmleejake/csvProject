angular.module('todo').factory('todoService', function($http) {
	var KEY = 'TODO_DATA';
	var storage = {
			todos: [
				{
					title:'요가수련'
					, completed: false
					, createdAt: Date.now()
				}
				,{
					title:'앵귤러학습'
					, completed: false
					, createdAt: Date.now()
				}
				,{
					title:'운동하기'
					, completed: true
					, createdAt: Date.now()
				}
			]
	
			, _saveToLocalStorage: function(data) {
				localStorage.setItem(KEY, JSON.stringify(data));
			}
	
			, _getFromLocalStorage: function() {
				return JSON.parse(localStorage.getItem(KEY)) || [];
			}
			
			, get: function() {
				angular.copy(storage._getFromLocalStorage(), storage.todos);
				return storage.todos;
			}
			
			, remove: function(todo) {
				var idx = storage.todos.findIndex(function(item) {
					return item.title === todo.title;
				});
				if(idx > -1) {
					storage.todos.splice(idx, 1);
					storage._saveToLocalStorage(storage.todos);
				}
			}
			
			, add: function(title) {
				if('' === title || undefined == title) {
					alert('내용을 입력해주세요.');
					return;
				}
				var newTodo = {
					title:title
					, completed: false
					, createdAt: Date.now()
				};
				storage.todos.push(newTodo);
				storage._saveToLocalStorage(storage.todos);
			}
			
			, update: function() {
				storage._saveToLocalStorage(storage.todos);
			}
			
			, dbTest: function() {
				return $http.get('/araku/amazon/showDealerMaster').then(succ, err);
				function succ(res) {
					console.log('succ');
					console.log(res);
				}
				function err(res) {
					console.log('err');
					console.log(res);
				}
			}
			, getSomething: function(url, data) {
				return $http.get(url, {params: data}).then(succ, err);
				function succ(res) {
					console.log('succ');
					console.log(res);
				}
				function err(res) {
					console.log('err');
					console.log(res);
				}
			}
			, postSomething: function(url, data) {
				return $http.post(url, JSON.stringify(data)).then(succ, err);
				function succ(res) {
					console.log('succ');
					console.log(res);
				}
				function err(res) {
					console.log('err');
					console.log(res);
				}
			}
	
	}
	return storage;
	
});