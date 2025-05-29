package Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.studentattendancemanagementapp.ClassAttendanceActivity;
import com.example.studentattendancemanagementapp.R;
import Model.ClassModel;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<ClassModel> classList;
    private Context context; // Added Context field

    // Updated constructor to accept Context
    public ClassAdapter(Context context, List<ClassModel> classList) {
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        if (classList != null && position >= 0 && position < classList.size()) { // Safety check
            ClassModel classModel = classList.get(position);
            holder.classNameTextView.setText(classModel.getClassName());

            // Set click listener to navigate to ClassAttendanceActivity
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ClassAttendanceActivity.class);
                intent.putExtra("CLASS_NAME", classModel.getClassName());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return classList != null ? classList.size() : 0; // Return 0 if classList is null
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.classNameTextView); // Ensure this ID matches class_item.xml
        }
    }
}