package org.alewfrank.spochansecondversion.Rating;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.alewfrank.spochansecondversion.R;
import org.alewfrank.spochansecondversion.User;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

public class RatingAdapter  extends FirestorePagingAdapter<User, RatingAdapter.RatingViewHolder> {

    //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    private OnListItemClick onListItemClick;

    public RatingAdapter(@NonNull FirestorePagingOptions<User> options, RatingAdapter.OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull RatingViewHolder holder, int position, @NonNull User model) {
        holder.userName.setText(model.getFirstName());
        holder.userSurname.setText(model.getSecondName());
        String bornDate = model.getDaysBornDate() + "." + model.getMonthBornDate() + "." + model.getYearBornDate();
        holder.userBornDate.setText(bornDate);
        holder.userPoints.setText(model.getUserPoints());
        holder.userCity.setText(model.getUserCity());

        if (model.getAvatarUrl() != null) {
            Glide.with(holder.userImageView.getContext())
                    .load(model.getAvatarUrl())
                    .into(holder.userImageView);
        }
    }



    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_for_raiting, parent, false);
        return new RatingViewHolder(view);
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




    public class RatingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userName;
        private TextView userSurname;
        private TextView userCity;
        private TextView userBornDate;
        private TextView userPoints;

        private ImageView userImageView;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userNameTextView);
            userSurname = itemView.findViewById(R.id.userSurnameTextView);
            userCity = itemView.findViewById(R.id.userCityTextView);
            userBornDate = itemView.findViewById(R.id.userBornDateTextView);
            userPoints = itemView.findViewById(R.id.userPointsTextView);

            userImageView = itemView.findViewById(R.id.userImageView);

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
