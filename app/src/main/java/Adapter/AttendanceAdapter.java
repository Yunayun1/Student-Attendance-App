//package Adapter;
//
//import Model.AttenStudent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.studentattendancemanagementapp.R;
//import java.util.List;
//
//public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
//
//    private List<AttenStudent> studentList;
//
//    public AttendanceAdapter(List<AttenStudent> studentList) {
//        this.studentList = studentList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_attenstudent, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        AttenStudent student = studentList.get(position);
//
//        holder.noTextView.setText(String.valueOf(position + 1)); // Number starting from 1
//        holder.nameTextView.setText(student.getName());
//        holder.genderTextView.setText(student.getGender());
//    }
//
//    @Override
//    public int getItemCount() {
//        return studentList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView noTextView, nameTextView, genderTextView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            noTextView = itemView.findViewById(R.id.textNo);
//            nameTextView = itemView.findViewById(R.id.textName);
//            genderTextView = itemView.findViewById(R.id.textGender);
//        }
//    }
//}