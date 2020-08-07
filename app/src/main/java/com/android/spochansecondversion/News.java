package com.android.spochansecondversion;

public class News {

    private String newsDescription;
    private String newsData;
    private String newsTime;
    private String newsId;
    private String newsImageUrl;

    //правой кнопкой мыши, потом generate и дальше Constructor, а ниже это создавалось с помощью Getter and Setter


    public News() {
    }

    public News(String newsDescription, String newsData, String newsTime, String newsId, String newsImageUrl) {
        this.newsDescription = newsDescription;
        this.newsData = newsData;
        this.newsTime = newsTime;
        this.newsId = newsId;
        this.newsImageUrl = newsImageUrl;
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

    public String getNewsImageUrl() {
        return newsImageUrl;
    }

    public void setNewsImageUrl(String newsImageUrl) {
        this.newsImageUrl = newsImageUrl;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }
}
