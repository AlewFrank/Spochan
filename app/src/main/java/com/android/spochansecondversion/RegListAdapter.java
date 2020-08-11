package com.android.spochansecondversion;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

public class RegListAdapter extends FirestorePagingAdapter<User, RegListAdapter.UserViewHolder> {

    //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    public RegListAdapter(@NonNull FirestorePagingOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RegListAdapter.UserViewHolder holder, int position, @NonNull User model) {
        String usName = model.getSecondName() + " " + model.getFirstName();
        holder.userName.setText(usName);
    }



    @NonNull
    @Override
    public RegListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_tem_for_reg_list, parent, false);
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




    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userFirstAndSecondNameTextView);

        }
    }


}
