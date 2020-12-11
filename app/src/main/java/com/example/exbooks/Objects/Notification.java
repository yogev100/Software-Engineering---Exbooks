package com.example.exbooks.Objects;

public class Notification {

    private Book book;
    private String userWantsTheBookId;
    private String wanterName;
    private boolean first;

    public Notification(){first=true;}
    public Notification(String uid, String wanterName, Book book, boolean first){
        this.book=book;
        this.userWantsTheBookId=uid;
        this.wanterName=wanterName;
        this.first=first;

    }

    public Notification(Notification other,boolean first){
        this.book=other.getBook();
        this.userWantsTheBookId=other.getUserWantsTheBookId();
        this.wanterName=other.getWanterName();
        this.first=first;
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

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
