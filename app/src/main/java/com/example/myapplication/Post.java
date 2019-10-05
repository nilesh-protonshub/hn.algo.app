package com.example.myapplication;

public class Post {
    public String created_at;
    public String title;
    public Boolean selectUnselect;

    public Post() {
    }

    public Post(String created_at, String title,Boolean selectUnselect) {
        this.title = title;
        this.selectUnselect = selectUnselect;
        this.created_at = created_at;
    }
}
