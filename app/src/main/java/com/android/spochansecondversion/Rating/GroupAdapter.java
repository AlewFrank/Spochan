package com.android.spochansecondversion.Rating;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spochansecondversion.R;

import java.util.ArrayList;

public class GroupAdapter  extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    ArrayList<Group> groups;
    Context context;

    public GroupAdapter(ArrayList<Group> groups,
                              Context context) {
        this.groups = groups;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,
                parent, false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(view);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groups.get(position);

        holder.title.setText(group.getTitle());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        public TextView title;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.titleTextView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();//получаем позицию
            Group group = groups.get(position);//извлекаем из эррай листа нужную позицию


            Intent intent = new Intent(context, RatingActivity.class);
            intent.putExtra("title", group.getTitle());
            intent.putExtra("index", group.getIndex());
            context.startActivity(intent);

        }
    }
}
