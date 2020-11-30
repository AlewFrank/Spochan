package com.android.spochansecondversion.Registration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RegListAdapter extends FirestorePagingAdapter<User, RegListAdapter.UserViewHolder> {

    //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    private RegListAdapter.OnListItemClick onListItemClick;

    public RegListAdapter(@NonNull FirestorePagingOptions<User> options, RegListAdapter.OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull RegListAdapter.UserViewHolder holder, int position, @NonNull User model) {
        holder.userName.setText(model.getSecondName());
        holder.userSurname.setText(model.getFirstName());
        String bornDate = model.getDaysBornDate() + "." + model.getMonthBornDate() + "." + model.getYearBornDate();
        holder.userCity.setText(model.getUserCity());
        holder.userBornDate.setText(bornDate);
        holder.userGroup.setText(model.getUserGroup());
    }



    @NonNull
    @Override
    public RegListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_for_reg_list, parent, false);
        return new RegListAdapter.UserViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);

        switch (state) {
            case LOADED:
                Log.d("PAGING_LOG", "Total items loaded: " + getItemCount());
                break;
            case ERROR:
                Log.d("PAGING_LOG", "Error loading data");
                break;
            case FINISHED:
                Log.d("PAGING_LOG", "All data loading ");
                break;
            case LOADING_MORE:
                Log.d("PAGING_LOG", "Loading new page");
                break;
            case LOADING_INITIAL:
                Log.d("PAGING_LOG", "Loading initial page");
                break;
        }
    }




    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView userName;
        private final TextView userSurname;
        private final TextView userCity;
        private final TextView userBornDate;
        private final TextView userGroup;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userNameTextView);
            userSurname = itemView.findViewById(R.id.userSurnameTextView);
            userCity = itemView.findViewById(R.id.userCityTextView);
            userBornDate = itemView.findViewById(R.id.userBornDateTextView);
            userGroup = itemView.findViewById(R.id.userGroupTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(DocumentSnapshot snapshot, int position);
    }


}
