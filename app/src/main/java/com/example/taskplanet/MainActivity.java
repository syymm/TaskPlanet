package com.example.taskplanet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskplanet.adapter.TodoAdapter;
import com.example.taskplanet.model.Todo;
import com.example.taskplanet.viewmodel.TodoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoInteractionListener, TodoViewModel.TodoListListener {
    
    private RecyclerView recyclerViewTodos;
    private TodoAdapter todoAdapter;
    private TodoViewModel todoViewModel;
    private FloatingActionButton btnAddTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initViewModel();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerViewTodos = findViewById(R.id.recyclerViewTodos);
        btnAddTodo = findViewById(R.id.btnAddTodo);
    }

    private void initViewModel() {
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.setTodoListListener(this);
    }

    private void setupRecyclerView() {
        todoAdapter = new TodoAdapter();
        todoAdapter.setOnTodoInteractionListener(this);
        
        recyclerViewTodos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTodos.setAdapter(todoAdapter);
        
        todoAdapter.updateTodoList(todoViewModel.getTodoList());
    }

    private void setupListeners() {
        btnAddTodo.setOnClickListener(v -> showAddTodoDialog());
    }

    private void showAddTodoDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_todo, null);
        
        TextInputEditText etTitle = dialogView.findViewById(R.id.etTitle);
        TextInputEditText etDescription = dialogView.findViewById(R.id.etDescription);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText() != null ? etTitle.getText().toString() : "";
            String description = etDescription.getText() != null ? etDescription.getText().toString() : "";
            
            if (title.trim().isEmpty()) {
                Toast.makeText(this, "タスクタイトルを入力してください", Toast.LENGTH_SHORT).show();
                return;
            }
            
            todoViewModel.addTodo(title, description);
            dialog.dismiss();
            Toast.makeText(this, "タスクが正常に追加されました", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    @Override
    public void onTodoToggle(int todoId) {
        todoViewModel.toggleTodoComplete(todoId);
    }

    @Override
    public void onTodoDelete(int todoId) {
        new AlertDialog.Builder(this)
                .setTitle("タスクを削除")
                .setMessage("このタスクを削除してもよろしいですか？")
                .setPositiveButton("削除", (dialog, which) -> {
                    todoViewModel.deleteTodo(todoId);
                    Toast.makeText(this, "タスクが削除されました", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }

    @Override
    public void onTodoClick(Todo todo) {
        Toast.makeText(this, "クリックしました: " + todo.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTodoListChanged() {
        runOnUiThread(() -> {
            todoAdapter.updateTodoList(todoViewModel.getTodoList());
        });
    }
}