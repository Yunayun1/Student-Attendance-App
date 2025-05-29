package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studentattendancemanagementapp.R;
import com.example.studentattendancemanagementapp.model.AttenStudent;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private Context context;
    private List<AttenStudent> studentList;
    private boolean isEditable;

    public StudentAdapter(Context context, List<AttenStudent> students, boolean isEditable) {
        this.context = context;
        this.studentList = students;
        this.isEditable = isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate your item layout here
        View view = LayoutInflater.from(context).inflate(R.layout.item_attenstudent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AttenStudent student = studentList.get(position);
        // bind data to views here
        // show editable views if isEditable is true
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Define your item views here

        public ViewHolder(View itemView) {
            super(itemView);
            // initialize views
        }
    }
}
