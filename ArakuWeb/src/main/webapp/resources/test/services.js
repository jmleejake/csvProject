angular.module('todo').factory('todoService', function() {
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
			
			, getFromStorage: function() {
				angular.copy(storage._getFromLocalStorage(), storage.todos);
				return storage.todos;
			}
			
			, removeFromStorage: function(todo) {
				var idx = storage.todos.findIndex(function(item) {
					return item.title === todo.title;
				});
				if(idx > -1) {
					storage.todos.splice(idx, 1);
					storage._saveToLocalStorage(storage.todos);
				}
			}
			
			, addToStorage: function(title) {
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
			
			, updateToStorage: function() {
				storage._saveToLocalStorage(storage.todos);
			}
	
	}
	return storage;
});