package com.linhminhoo.kukki.Items;

/**
 * Created by linhminhoo on 7/6/2015.
 */
public class NewFeedItems {
    int id;
    String title, artist, time_finish, total_like, total_comment, total_view, total_kcal, feedImage;

    public NewFeedItems() {
    }

    public NewFeedItems(int id, String title, String artist, String time_finish, String total_like, String total_comment, String total_view, String total_kcal, String feedImage) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.time_finish = time_finish;
        this.total_like = total_like;
        this.total_comment = total_comment;
        this.total_view = total_view;
        this.total_kcal = total_kcal;
        this.feedImage = feedImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTime_finish() {
        return time_finish;
    }

    public void setTime_finish(String time_finish) {
        this.time_finish = time_finish;
    }

    public String getTotal_like() {
        return total_like;
    }

    public void setTotal_like(String total_like) {
        this.total_like = total_like;
    }

    public String getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(String total_comment) {
        this.total_comment = total_comment;
    }

    public String getTotal_view() {
        return total_view;
    }

    public void setTotal_view(String total_view) {
        this.total_view = total_view;
    }

    public String getTotal_kcal() {
        return total_kcal;
    }

    public void setTotal_kcal(String total_kcal) {
        this.total_kcal = total_kcal;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }
}
