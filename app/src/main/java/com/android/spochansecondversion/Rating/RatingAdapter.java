package com.android.spochansecondversion.Rating;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spochansecondversion.R;
import com.android.spochansecondversion.User;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

public class RatingAdapter  extends FirestorePagingAdapter<User, RatingAdapter.RatingViewHolder> {

    //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    public RatingAdapter(@NonNull FirestorePagingOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RatingAdapter.RatingViewHolder holder, int position, @NonNull User model) {
        holder.userName.setText(model.getFirstName());
        holder.userSurname.setText(model.getSecondName());
        String bornDate = model.getDaysBornDate() + "." + model.getMonthBornDate() + "." + model.getYearBornDate();
        holder.userBornDate.setText(bornDate);
        holder.userPoints.setText(model.getUserPoints());
    }



    @NonNull
    @Override
    public RatingAdapter.RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new RatingAdapter.RatingViewHolder(view);
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




    public class RatingViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private TextView userSurname;
        private TextView userCity;
        private TextView userBornDate;
        private TextView userPoints;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userNameTextView);
            userSurname = itemView.findViewById(R.id.userSurnameTextView);
            userCity = itemView.findViewById(R.id.userCityTextView);
            userBornDate = itemView.findViewById(R.id.userBornDateTextView);
            userPoints = itemView.findViewById(R.id.userPointsTextView);


        }
    }


}
