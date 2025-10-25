package org.khan.coursetodoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    private List<ToDo> list;
    private OnItemClickListener listener;
    private SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault());

    public ToDoAdapter(List<ToDo> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ToDo t = list.get(position);
        holder.course.setText(t.getCourseId());
        holder.last.setText(df.format(t.getLastSaveTime()));
        holder.title.setText(t.getTitle());

        String text = t.getText() == null ? "" : t.getText();
        if (text.length() > 80) text = text.substring(0, 80) + "…";
        holder.preview.setText(text);

        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(holder.getAdapterPosition()));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView course, last, title, preview;
        VH(@NonNull View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.tvCourse);
            last = itemView.findViewById(R.id.tvLast);
            title = itemView.findViewById(R.id.tvTitle);
            preview = itemView.findViewById(R.id.tvPreview);
        }
    }
}
