package com.example.todowithziro.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todowithziro.R;
import com.example.todowithziro.pojo.Todo;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Todo> todoList;
    private RecyclerAdapter.ClickListener clickListener;

    public RecyclerAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        todoList = new ArrayList<>();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.txtName.setText(todo.name);
        holder.txtNo.setText("#" + String.valueOf(todo.todo_id));
        holder.txtDesc.setText(todo.description);
        holder.txtdate.setText(todo.date);


    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    public void updateTodoList(List<Todo> data) {
        todoList.clear();
        todoList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Todo data) {
        todoList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName,txtNo,txtDesc,txtCategory,txtdate;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            txtNo = view.findViewById(R.id.txtNo);
            txtName = view.findViewById(R.id.txtName);
            txtDesc = view.findViewById(R.id.txtDesc);
            txtCategory = view.findViewById(R.id.txtCategory);
            cardView = view.findViewById(R.id.cardView);
            txtdate = view.findViewById(R.id.txtDate);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.launchIntent(todoList.get(getAdapterPosition()).todo_id);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}