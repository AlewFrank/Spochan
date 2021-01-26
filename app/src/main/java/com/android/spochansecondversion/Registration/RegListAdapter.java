package com.android.spochansecondversion.Registration;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.RatingAdapter;
import com.android.spochansecondversion.User;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RegListAdapter extends RecyclerView.Adapter<RegListAdapter.UserViewHolder> {

    //Тут адаптервзят из AwesomeChat, так как там идет работа с RealTime Database, так что если есть какие-то вопросы, то смотри туда

    private OnUserClickListener listener;
    private ArrayList<User> users;

    public interface OnUserClickListener {
        void onUserClick(int position);
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    public RegListAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void onBindViewHolder(@NonNull RegListAdapter.UserViewHolder holder, int position) {
        User currentUser = users.get(position);
        holder.userName.setText(currentUser.getSecondName());
        holder.userSurname.setText(currentUser.getFirstName());
        String bornDate = currentUser.getDaysBornDate() + "." + currentUser.getMonthBornDate() + "." + currentUser.getYearBornDate();
        holder.userClub.setText(currentUser.getUserClub());
        holder.userBornDate.setText(bornDate);
        holder.userGroup.setText(currentUser.getUserGroup());

        if (!currentUser.isHasPayed() || !currentUser.isHasComeOn()) {
            holder.colorfulRelativeLayout.setBackgroundColor((int) R.color.Red);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @NonNull
    @Override
    public RegListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_for_reg_list, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(view, listener);
        return viewHolder;
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView userName;
        private final TextView userSurname;
        private final TextView userGroup;
        private final TextView userBornDate;
        private final TextView userClub;
        private RelativeLayout colorfulRelativeLayout;

        public UserViewHolder(@NonNull View itemView, final OnUserClickListener listener) {
            super(itemView);

            userName = itemView.findViewById(R.id.userNameTextView);
            userSurname = itemView.findViewById(R.id.userSurnameTextView);
            userClub = itemView.findViewById(R.id.userClubTextView);
            userBornDate = itemView.findViewById(R.id.userBornDateTextView);
            userGroup = itemView.findViewById(R.id.userGroupTextView);
            colorfulRelativeLayout = itemView.findViewById(R.id.colorfulRelativeLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onUserClick(position);
                        }
                    }
                }
            });
        }
    }
}
