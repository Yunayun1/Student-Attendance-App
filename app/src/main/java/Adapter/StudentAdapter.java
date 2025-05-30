package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagementapp.R;

import java.util.List;

import Model.AttenStudent;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<AttenStudent> students;
    private Context context;
    private boolean isEditable;

    private OnItemDoubleClickListener doubleClickListener;

    public interface OnItemDoubleClickListener {
        void onItemDoubleClicked(int position);
    }

    public void setOnItemDoubleClickListener(OnItemDoubleClickListener listener) {
        this.doubleClickListener = listener;
    }

    public StudentAdapter(Context context, List<AttenStudent> students, boolean isEditable) {
        this.context = context;
        this.students = students;
        this.isEditable = isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attenstudent, parent, false);
        return new ViewHolder(view);
    }

    private long lastClickTime = 0;

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        AttenStudent student = students.get(position);

        holder.noTextView.setText(String.valueOf(position + 1));
        holder.nameTextView.setText(student.getName());
        holder.genderTextView.setText(student.getGender());

        // Background color green if present, red if absent
        if (student.isPresent()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#A5D6A7")); // light green
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#EF9A9A")); // light red
        }

        // Double click listener on item
        holder.itemView.setOnClickListener(v -> {
            long clickTime = SystemClock.elapsedRealtime();
            if (clickTime - lastClickTime < 300) {
                if (doubleClickListener != null) {
                    doubleClickListener.onItemDoubleClicked(position);
                }
            }
            lastClickTime = clickTime;
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noTextView, nameTextView, genderTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noTextView = itemView.findViewById(R.id.textNo);
            nameTextView = itemView.findViewById(R.id.textName);
            genderTextView = itemView.findViewById(R.id.textGender);
        }
    }
}