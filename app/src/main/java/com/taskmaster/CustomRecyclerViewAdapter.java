package com.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taskmaster.data.Task;
import com.taskmaster.data.TaskModel;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    List<Task> taskData;


    CustomClickListener listener;

    public CustomRecyclerViewAdapter(List<Task> taskData, CustomClickListener listener) {
        this.taskData = taskData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.task_item_layout,parent,false);

        return new CustomViewHolder(listItemView, listener);
    }


    // will be called multiple times to inject the data into the view holder object
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.title.setText(taskData.get(position).getTitle());
        holder.description.setText(taskData.get(position).getBody());
        holder.state.setText(taskData.get(position).getState());
    }

    @Override
    public int getItemCount() {
        return taskData.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView state;

        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            title = itemView.findViewById(R.id.task_t);
            description = itemView.findViewById(R.id.description);
            state = itemView.findViewById(R.id.state);

            itemView.setOnClickListener(view -> {
                listener.onTaskClickListener(getAdapterPosition());

            });
//            title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.onTaskClickListener(getAdapterPosition());
//                }
//            });
        }

    }

    public interface CustomClickListener{
        void onTaskClickListener(int position);
    }
}
