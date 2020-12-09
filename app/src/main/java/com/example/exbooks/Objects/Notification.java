package com.example.exbooks.Objects;

public class Notification {

    private Book book;
    private String userWantsTheBookId;
    private String wanterName;

    public Notification(){}
    public Notification(String uid,String wanterName,Book book){
        this.book=book;
        this.userWantsTheBookId=uid;
        this.wanterName=wanterName;

    }

    public Notification(Notification other){
        this.book=other.getBook();
        this.userWantsTheBookId=other.getUserWantsTheBookId();
        this.wanterName=other.getWanterName();
    }

    public String getWanterName() {
        return wanterName;
    }

    public void setWanterName(String wanterName) {
        this.wanterName = wanterName;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUserWantsTheBookId() {
        return userWantsTheBookId;
    }

    public void setUserWantsTheBookId(String userWantsTheBookId) {
        this.userWantsTheBookId = userWantsTheBookId;
    }
}
