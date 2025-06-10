package com.example.taskplanet.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.taskplanet.model.Todo;
import java.util.ArrayList;
import java.util.List;

public class TodoViewModel extends ViewModel {
    private List<Todo> todoList;
    private TodoListListener listener;
    private int nextId = 1;

    public interface TodoListListener {
        void onTodoListChanged();
    }

    public TodoViewModel() {
        todoList = new ArrayList<>();
        initializeDemoData();
    }

    private void initializeDemoData() {
        addTodo("Androidプロジェクトを完成", "TodoList機能を実装");
        addTodo("MVVMアーキテクチャを学習", "");
        addTodo("Javaの基礎を復習", "面接の準備");
    }

    public void setTodoListListener(TodoListListener listener) {
        this.listener = listener;
    }

    public List<Todo> getTodoList() {
        return new ArrayList<>(todoList);
    }

    public void addTodo(String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            return;
        }
        
        Todo todo = new Todo(title.trim(), description != null ? description.trim() : "");
        todo.setId(nextId++);
        todoList.add(0, todo);
        notifyDataChanged();
    }

    public void deleteTodo(int todoId) {
        for (int i = 0; i < todoList.size(); i++) {
            if (todoList.get(i).getId() == todoId) {
                todoList.remove(i);
                notifyDataChanged();
                break;
            }
        }
    }

    public void toggleTodoComplete(int todoId) {
        for (Todo todo : todoList) {
            if (todo.getId() == todoId) {
                todo.setCompleted(!todo.isCompleted());
                notifyDataChanged();
                break;
            }
        }
    }

    public void updateTodo(int todoId, String title, String description) {
        for (Todo todo : todoList) {
            if (todo.getId() == todoId) {
                todo.setTitle(title != null ? title.trim() : "");
                todo.setDescription(description != null ? description.trim() : "");
                notifyDataChanged();
                break;
            }
        }
    }

    public int getTodoCount() {
        return todoList.size();
    }

    public int getCompletedCount() {
        int count = 0;
        for (Todo todo : todoList) {
            if (todo.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    private void notifyDataChanged() {
        if (listener != null) {
            listener.onTodoListChanged();
        }
    }
}