package org.admin.spochansecondversion.Rating;

public class Group {

    private String title;
    private String index;

    public Group() {
    }

    public Group(String title, String index) {
        this.title = title;
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
