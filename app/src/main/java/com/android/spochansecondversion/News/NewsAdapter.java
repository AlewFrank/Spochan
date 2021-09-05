package com.android.spochansecondversion.News;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spochansecondversion.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

public class NewsAdapter extends FirestorePagingAdapter<News, NewsAdapter.NewsViewHolder> {

    private OnListItemClick onListItemClick;//это interface, который мы создали внизу public interface OnListItemClick {, я неочень понял зачем он нужен

    private int imageCount;
    private int currentNewsIndex;//индекс новости в массиве

    private ArrayList<String> newsArrayId;
    private ArrayList<Integer> currentImageArray;
    private ArrayList<Integer> imageCountArray;


    private Integer flag; //обновляется при открытии новой новости
    private Integer arrayFlag = 0;//обновляется после первого открытия окна новостей и больше не участвует

    //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    public NewsAdapter(@NonNull FirestorePagingOptions<News> options, NewsAdapter.OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onBindViewHolder(@NonNull final NewsViewHolder holder, int position, @NonNull final News model) {

        //Кек

        if (arrayFlag == 0) {
            newsArrayId = new ArrayList<>();
            currentImageArray = new ArrayList<>();
            imageCountArray = new ArrayList<>();
            arrayFlag = 9; //чтобы массив не обновлялся
        }

        flag = 0;//1 это значит что такая строка в массиве есть, а 0 это такого нет

        holder.newsTitle.setText(model.getNewsTitle());
        holder.newsDescription.setText(model.getNewsDescription());
        holder.newsData.setText(model.getNewsData());
        holder.newsTime.setText(model.getNewsTime());


        if (newsArrayId.contains(model.getNewsId())) {
            flag = 1;
            currentNewsIndex = newsArrayId.indexOf(model.getNewsId());
        }

        if (flag==0) { //то есть новость впервые открывается и в массиве ее нет, то заносим все данные о ней в массивы
            newsArrayId.add(model.getNewsId()); //число это какая фотка открыта в данный момент
            currentImageArray.add(1);
            currentNewsIndex = newsArrayId.size() - 1;//так как элемент был вставлен в конец массива + индексы начинаются с нуля, поэтому надо вычесть единицу


            if (model.getNewsImageUrl_1() != null & !model.getNewsImageUrl_1().equals("")) {
                imageCount = 1;
            }

            if (model.getNewsImageUrl_2() != null & !model.getNewsImageUrl_2().equals("")) {
                imageCount = 2;
            }

            if (model.getNewsImageUrl_3() != null & !model.getNewsImageUrl_3().equals("")) {
                imageCount = 3;
            }

            if (model.getNewsImageUrl_4() != null & !model.getNewsImageUrl_4().equals("")) {
                imageCount = 4;
            }

            if (model.getNewsImageUrl_5() != null & !model.getNewsImageUrl_5().equals("")) {
                imageCount = 5;
            }


            imageCountArray.add(imageCount);
        }

        //дальше идут настройки для колличества точек
        holder.point1.setVisibility(View.GONE);
        holder.point2.setVisibility(View.GONE);
        holder.point3.setVisibility(View.GONE);
        holder.point4.setVisibility(View.GONE);
        holder.point5.setVisibility(View.GONE);

        if (imageCountArray.get(currentNewsIndex) >= 2) {
            holder.point1.setVisibility(View.VISIBLE);
            holder.point2.setVisibility(View.VISIBLE);
            holder.nextImageButton.setVisibility(View.VISIBLE);
            holder.nextImageBackGround.setVisibility(View.VISIBLE);
        }

        if (imageCountArray.get(currentNewsIndex) >= 3) {
            holder.point3.setVisibility(View.VISIBLE);
        }

        if (imageCountArray.get(currentNewsIndex) >= 4) {
            holder.point4.setVisibility(View.VISIBLE);
        }

        if (imageCountArray.get(currentNewsIndex) >= 5) {
            holder.point5.setVisibility(View.VISIBLE);
        }



        if (currentImageArray.get(currentNewsIndex) == 1) {

            Glide.with(holder.newsImage.getContext())
                    .load(model.getNewsImageUrl_1())
                    .into(holder.newsImage);

            Glide.with(holder.point1.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point1);
            Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point2);
            Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point3);
            Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point4);
            Glide.with(holder.point5.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point5);

            holder.previousImageButton.setVisibility(View.GONE); //чтоб изначально нельзя было выбрать предыдущую кнопку
            holder.previousImageBackGround.setVisibility(View.GONE);

            if (imageCountArray.get(currentNewsIndex) >= 2) {
                holder.nextImageButton.setVisibility(View.VISIBLE);
                holder.nextImageBackGround.setVisibility(View.VISIBLE);
            } else {
                holder.nextImageButton.setVisibility(View.GONE); //чтоб изначально нельзя было выбрать следующую кнопку, если у нас одно изображение
                holder.nextImageBackGround.setVisibility(View.GONE);
            }
        } else if (currentImageArray.get(currentNewsIndex) == 2) {

            Glide.with(holder.newsImage.getContext())
                    .load(model.getNewsImageUrl_2())
                    .into(holder.newsImage);

            Glide.with(holder.point1.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point1);
            Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point2);
            Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point3);
            Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point4);
            Glide.with(holder.point5.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point5);

            holder.previousImageButton.setVisibility(View.VISIBLE);
            holder.previousImageBackGround.setVisibility(View.VISIBLE);

            if (imageCountArray.get(currentNewsIndex)>=3) {
                holder.nextImageButton.setVisibility(View.VISIBLE);
                holder.nextImageBackGround.setVisibility(View.VISIBLE);
            } else {
                holder.nextImageButton.setVisibility(View.GONE);
                holder.nextImageBackGround.setVisibility(View.GONE);
            }
        } else if (currentImageArray.get(currentNewsIndex) == 3) {

            Glide.with(holder.newsImage.getContext())
                    .load(model.getNewsImageUrl_3())
                    .into(holder.newsImage);

            Glide.with(holder.point1.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point1);
            Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point2);
            Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point3);
            Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point4);
            Glide.with(holder.point5.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point5);

            holder.previousImageButton.setVisibility(View.VISIBLE);
            holder.previousImageBackGround.setVisibility(View.VISIBLE);

            if (imageCountArray.get(currentNewsIndex)>=4) {
                holder.nextImageButton.setVisibility(View.VISIBLE);
                holder.nextImageBackGround.setVisibility(View.VISIBLE);
            } else {
                holder.nextImageButton.setVisibility(View.GONE);
                holder.nextImageBackGround.setVisibility(View.GONE);
            }
        } else if (currentImageArray.get(currentNewsIndex) == 4) {

            Glide.with(holder.newsImage.getContext())
                    .load(model.getNewsImageUrl_4())
                    .into(holder.newsImage);

            Glide.with(holder.point1.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point1);
            Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point2);
            Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point3);
            Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point4);
            Glide.with(holder.point5.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point5);

            holder.previousImageButton.setVisibility(View.VISIBLE);
            holder.previousImageBackGround.setVisibility(View.VISIBLE);

            if (imageCountArray.get(currentNewsIndex)>=5) {
                holder.nextImageButton.setVisibility(View.VISIBLE);
                holder.nextImageBackGround.setVisibility(View.VISIBLE);
            } else {
                holder.nextImageButton.setVisibility(View.GONE);
                holder.nextImageBackGround.setVisibility(View.GONE);
            }
        } else if (currentImageArray.get(currentNewsIndex) == 5) {

            Glide.with(holder.newsImage.getContext())
                    .load(model.getNewsImageUrl_5())
                    .into(holder.newsImage);

            Glide.with(holder.point1.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point1);
            Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point2);
            Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point3);
            Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point4);
            Glide.with(holder.point5.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point5);

            holder.previousImageButton.setVisibility(View.VISIBLE);
            holder.previousImageBackGround.setVisibility(View.VISIBLE);
            holder.nextImageButton.setVisibility(View.GONE);
            holder.nextImageBackGround.setVisibility(View.GONE);
        }


        //для переменных previousImageIndex и nextImageIndex нужно обязательно использовать firebase, так как иначе численные значения карточек будут путаться и нажимая кнопку на одной карте, ты увеличишь значений другой, короче такие переменные они общие для всех карточек
        holder.previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentNewsIndex = newsArrayId.indexOf(model.getNewsId());

                if (model.getNewsImageUrl_4() != null && currentImageArray.get(currentNewsIndex) == 5) {

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_4())
                            .into(holder.newsImage);

                    Glide.with(holder.point5.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point5);
                    Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point4);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 4);

                    holder.nextImageButton.setVisibility(View.VISIBLE);
                    holder.nextImageBackGround.setVisibility(View.VISIBLE);
                } else if (model.getNewsImageUrl_3() != null && currentImageArray.get(currentNewsIndex) == 4) {

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_3())
                            .into(holder.newsImage);

                    Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point4);
                    Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point3);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 3);

                    holder.nextImageButton.setVisibility(View.VISIBLE);
                    holder.nextImageBackGround.setVisibility(View.VISIBLE);
                } else if (model.getNewsImageUrl_2() != null && currentImageArray.get(currentNewsIndex) == 3) {

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_2())
                            .into(holder.newsImage);

                    Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point3);
                    Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point2);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 2);

                    holder.nextImageButton.setVisibility(View.VISIBLE);
                    holder.nextImageBackGround.setVisibility(View.VISIBLE);
                } else if (model.getNewsImageUrl_1() != null && currentImageArray.get(currentNewsIndex) == 2) {

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_1())
                            .into(holder.newsImage);

                    Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point2);
                    Glide.with(holder.point1.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point1);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 1);

                    holder.previousImageButton.setVisibility(View.GONE);
                    holder.previousImageBackGround.setVisibility(View.GONE);
                    holder.nextImageButton.setVisibility(View.VISIBLE);
                    holder.nextImageBackGround.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentNewsIndex = newsArrayId.indexOf(model.getNewsId());

                if (model.getNewsImageUrl_2() != null && currentImageArray.get(currentNewsIndex) == 1) {

                    if (imageCountArray.get(currentNewsIndex)<=2) {
                        holder.nextImageButton.setVisibility(View.GONE);
                        holder.nextImageBackGround.setVisibility(View.GONE);
                    }

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_2())
                            .into(holder.newsImage);

                    Glide.with(holder.point1.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point1);
                    Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point2);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 2);

                    holder.previousImageButton.setVisibility(View.VISIBLE);
                    holder.previousImageBackGround.setVisibility(View.VISIBLE);
                } else if (model.getNewsImageUrl_3() != null && currentImageArray.get(currentNewsIndex) == 2) {

                    if (imageCountArray.get(currentNewsIndex)<=3) {
                        holder.nextImageButton.setVisibility(View.GONE);
                        holder.nextImageBackGround.setVisibility(View.GONE);
                    }

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_3())
                            .into(holder.newsImage);

                    Glide.with(holder.point2.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point2);
                    Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point3);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 3);
                } else if (model.getNewsImageUrl_4() != null && currentImageArray.get(currentNewsIndex) == 3) {

                    if (imageCountArray.get(currentNewsIndex)<=4) {
                        holder.nextImageButton.setVisibility(View.GONE);
                        holder.nextImageBackGround.setVisibility(View.GONE);
                    }

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_4())
                            .into(holder.newsImage);

                    Glide.with(holder.point3.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point3);
                    Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point4);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 4);
                } else if (model.getNewsImageUrl_5() != null && currentImageArray.get(currentNewsIndex) == 4) {

                    holder.nextImageButton.setVisibility(View.GONE);
                    holder.nextImageBackGround.setVisibility(View.GONE);

                    Glide.with(holder.newsImage.getContext())
                            .load(model.getNewsImageUrl_5())
                            .into(holder.newsImage);

                    Glide.with(holder.point4.getContext()).load(R.drawable.ic_baseline_point_for_news_count_disable_24).into(holder.point4);
                    Glide.with(holder.point5.getContext()).load(R.drawable.ic_baseline_point_for_news_count_enable_24).into(holder.point5);

                    currentImageArray.remove(currentNewsIndex);
                    currentImageArray.add(currentNewsIndex, 5);
                }
            }
        });



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

        private TextView newsTitle;
        private TextView newsDescription;
        private TextView newsData;
        private TextView newsTime;
        private ImageView newsImage;
        private Button previousImageButton;
        private Button nextImageButton;
        private ImageView previousImageBackGround, nextImageBackGround;
        private ImageView point1, point2, point3, point4, point5;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            newsTitle = itemView.findViewById(R.id.newsTitleTextView);
            newsDescription = itemView.findViewById(R.id.newsDescriptionTextView);
            newsData = itemView.findViewById(R.id.newsDataTextView);
            newsTime = itemView.findViewById(R.id.newsTimeTextView);
            newsImage = itemView.findViewById(R.id.newsImageView);
            previousImageButton = itemView.findViewById(R.id.previousImageButton);
            nextImageButton = itemView.findViewById(R.id.nextImageButton);
            previousImageBackGround = itemView.findViewById(R.id.previousImageBackGround);
            nextImageBackGround = itemView.findViewById(R.id.nextImageBackGround);

            point1 = itemView.findViewById(R.id.point1);
            point2 = itemView.findViewById(R.id.point2);
            point3 = itemView.findViewById(R.id.point3);
            point4 = itemView.findViewById(R.id.point4);
            point5 = itemView.findViewById(R.id.point5);

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

