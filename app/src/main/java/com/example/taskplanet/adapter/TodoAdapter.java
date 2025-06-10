package com.example.taskplanet.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taskplanet.R;
import com.example.taskplanet.model.Todo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<Todo> todoList;
    private OnTodoInteractionListener listener;

    public interface OnTodoInteractionListener {
        void onTodoToggle(int todoId);
        void onTodoDelete(int todoId);
        void onTodoClick(Todo todo);
    }

    public TodoAdapter() {
        this.todoList = new ArrayList<>();
    }

    public void setOnTodoInteractionListener(OnTodoInteractionListener listener) {
        this.listener = listener;
    }

    public void updateTodoList(List<Todo> newTodoList) {
        this.todoList.clear();
        this.todoList.addAll(newTodoList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.bind(todo);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbCompleted;
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvTime;
        private ImageButton btnDelete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Todo todo) {
            cbCompleted.setChecked(todo.isCompleted());
            tvTitle.setText(todo.getTitle());
            
            if (todo.getDescription() != null && !todo.getDescription().isEmpty()) {
                tvDescription.setText(todo.getDescription());
                tvDescription.setVisibility(View.VISIBLE);
            } else {
                tvDescription.setVisibility(View.GONE);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
            tvTime.setText(sdf.format(new Date(todo.getCreatedTime())));

            updateTextAppearance(todo.isCompleted());

            cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onTodoToggle(todo.getId());
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTodoDelete(todo.getId());
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTodoClick(todo);
                }
            });
        }

        private void updateTextAppearance(boolean isCompleted) {
            if (isCompleted) {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvTitle.setAlpha(0.6f);
                tvDescription.setAlpha(0.6f);
                tvTime.setAlpha(0.6f);
            } else {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvTitle.setAlpha(1.0f);
                tvDescription.setAlpha(1.0f);
                tvTime.setAlpha(1.0f);
            }
        }
    }
}