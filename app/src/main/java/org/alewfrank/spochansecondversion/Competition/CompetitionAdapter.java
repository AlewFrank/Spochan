package org.alewfrank.spochansecondversion.Competition;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.alewfrank.spochansecondversion.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

public class CompetitionAdapter extends FirestorePagingAdapter<Competition, CompetitionAdapter.CompetitionViewHolder> {//по сути мы сюда из CompetitionActivity перенесли все, что связано с адаптером

    private OnListItemClick onListItemClick;//это interface, который мы создали внизу public interface OnListItemClick {, я неочень понял зачем он нужен

    public CompetitionAdapter(@NonNull FirestorePagingOptions<Competition> options, OnListItemClick onListItemClick) {//это типа конструктор
        super(options);
        this.onListItemClick = onListItemClick;//после этой строки мы можем использовать этот интерфейс по всему классу адаптера
    }

    @Override
    protected void onBindViewHolder(@NonNull CompetitionViewHolder holder, int position, @NonNull Competition model) {//смотри сначала в CompetitionViewHolder
        holder.competitionTitle.setText(model.getCompetitionTitle());
        holder.competitionData.setText(model.getDaysCompetitionDate() + "." + model.getMonthCompetitionDate() + "." + model.getYearCompetitionDate());
        holder.competitionLocation.setText(model.getCompetitionLocation());

        Glide.with(holder.competitionImage.getContext())//таким образом мы загружаем изображения в наш image View
                .load(model.getCompetitionImageUrl())
                .into(holder.competitionImage);

    }



    @NonNull
    @Override
    public CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//связываем CompetitionActivity с элементом, то есть что именно competitions_item это элемент нашего списка
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competitions_item, parent, false);
        return new CompetitionViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {//чисто для самопроверки, можно вообще удалить
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




    public class CompetitionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {//это те элементы, которые мы устанавливаем в наш competition_item, то есть мы здесь все имплементируем, а используем выше в onBindViewHolder

        private TextView competitionTitle;
        private TextView competitionData;
        private TextView competitionLocation;
        private ImageView competitionImage;


        public CompetitionViewHolder(@NonNull View itemView) {
            super(itemView);

            competitionTitle = itemView.findViewById(R.id.competitionTitleTextView);
            competitionData = itemView.findViewById(R.id.competitionDataTextView);
            competitionLocation = itemView.findViewById(R.id.competitionLocationTextView);
            competitionImage = itemView.findViewById(R.id.competitionImageView);


            itemView.setOnClickListener(this);
        }



        //все что выше это мы перенесли сюда из CompetitionActivity, чтоб не захломлять там место, все что ниже это имплементации того, чтоб при нажатии на карточку открывалась соответствующая активити


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
