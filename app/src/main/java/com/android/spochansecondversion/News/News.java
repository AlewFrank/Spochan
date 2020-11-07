package com.android.spochansecondversion.News;

public class News {

    private String newsTitle;
    private String newsDescription;
    private String newsData;
    private String newsTime;
    private String newsId;

    private String newsImageUrl_1;
    private String newsImageUrl_2;
    private String newsImageUrl_3;
    private String newsImageUrl_4;
    private String newsImageUrl_5;

    private int previousImageIndex;
    private int nextImageIndex;


    //правой кнопкой мыши, потом generate и дальше Constructor, а ниже это создавалось с помощью Getter and Setter


    public News() {
    }

    public News(String newsTitle, String newsDescription, String newsData, String newsTime, String newsId, String newsImageUrl_1, String newsImageUrl_2, String newsImageUrl_3, String newsImageUrl_4, String newsImageUrl_5, int previousImageIndex, int nextImageIndex) {
        this.newsTitle = newsTitle;
        this.newsDescription = newsDescription;
        this.newsData = newsData;
        this.newsTime = newsTime;
        this.newsId = newsId;
        this.newsImageUrl_1 = newsImageUrl_1;
        this.newsImageUrl_2 = newsImageUrl_2;
        this.newsImageUrl_3 = newsImageUrl_3;
        this.newsImageUrl_4 = newsImageUrl_4;
        this.newsImageUrl_5 = newsImageUrl_5;
        this.previousImageIndex = previousImageIndex;
        this.nextImageIndex = nextImageIndex;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsData() {
        return newsData;
    }

    public void setNewsData(String newsData) {
        this.newsData = newsData;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsImageUrl_1() {
        return newsImageUrl_1;
    }

    public void setNewsImageUrl_1(String newsImageUrl_1) {
        this.newsImageUrl_1 = newsImageUrl_1;
    }

    public String getNewsImageUrl_2() {
        return newsImageUrl_2;
    }

    public void setNewsImageUrl_2(String newsImageUrl_2) {
        this.newsImageUrl_2 = newsImageUrl_2;
    }

    public String getNewsImageUrl_3() {
        return newsImageUrl_3;
    }

    public void setNewsImageUrl_3(String newsImageUrl_3) {
        this.newsImageUrl_3 = newsImageUrl_3;
    }

    public String getNewsImageUrl_4() {
        return newsImageUrl_4;
    }

    public void setNewsImageUrl_4(String newsImageUrl_4) {
        this.newsImageUrl_4 = newsImageUrl_4;
    }

    public String getNewsImageUrl_5() {
        return newsImageUrl_5;
    }

    public void setNewsImageUrl_5(String newsImageUrl_5) {
        this.newsImageUrl_5 = newsImageUrl_5;
    }

    public int getPreviousImageIndex() {
        return previousImageIndex;
    }

    public void setPreviousImageIndex(int previousImageIndex) {
        this.previousImageIndex = previousImageIndex;
    }

    public int getNextImageIndex() {
        return nextImageIndex;
    }

    public void setNextImageIndex(int nextImageIndex) {
        this.nextImageIndex = nextImageIndex;
    }
}
