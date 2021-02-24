package com.android.spochansecondversion.Registration;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.Group;

import java.util.ArrayList;

public class FiltrationAdapter  extends RecyclerView.Adapter<FiltrationAdapter.FiltrationViewHolder> {

    ArrayList<Group> groups;
    Context context;

    public FiltrationAdapter(ArrayList<Group> groups,
                        Context context) {
        this.groups = groups;
        this.context = context;
    }

    @NonNull
    @Override
    public FiltrationAdapter.FiltrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,
                parent, false);
        FiltrationViewHolder filtrationViewHolder = new FiltrationViewHolder(view);
        return filtrationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FiltrationViewHolder holder, int position) {
        Group group = groups.get(position);

        holder.title.setText(group.getTitle());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class FiltrationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;

        public FiltrationViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.titleTextView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();//получаем позицию
            Group group = groups.get(position);//извлекаем из эррай листа нужную позицию


            Intent intent = new Intent(context, FiltrationActivity.class);
            intent.putExtra("title", group.getTitle());
            intent.putExtra("index", group.getIndex());
            context.startActivity(intent);

        }
    }
}