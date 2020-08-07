package com.android.spochansecondversion;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

public class NewsAdapter extends FirestorePagingAdapter<News, NewsAdapter.NewsViewHolder> {

    private OnListItemClick onListItemClick;//это interface, который мы создали внизу public interface OnListItemClick {, я неочень понял зачем он нужен

    //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    public NewsAdapter(@NonNull FirestorePagingOptions<News> options, NewsAdapter.OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull News model) {
        holder.newsDescription.setText(model.getNewsDescription());
        holder.newsData.setText(model.getNewsData());
        holder.newsTime.setText(model.getNewsTime());

        Glide.with(holder.newsImage.getContext())
                .load(model.getNewsImageUrl())
                .into(holder.newsImage);

    }



    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
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




    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView newsDescription;
        private TextView newsData;
        private TextView newsTime;
        private ImageView newsImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            newsDescription = itemView.findViewById(R.id.newsDescriptionTextView);
            newsData = itemView.findViewById(R.id.newsDataTextView);
            newsTime = itemView.findViewById(R.id.newsTimeTextView);
            newsImage = itemView.findViewById(R.id.newsImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {//получили после нажатия на красную лампочку после написания в методе выше implements View.OnClickListener
            //getItem(getAdapterPosition() строкой ниже это и есть DocumentSnapshot snapshot, который мы передаем в public interface OnListItemClick {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());//вот эта строка осуществляет переход к интерфейсу(хз что это означает) ниже public interface OnListItemClick {
        }
    }

    public interface OnListItemClick {
        void onItemClick(DocumentSnapshot snapshot, int position);
    }


}

