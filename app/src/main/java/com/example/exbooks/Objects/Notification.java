package com.example.exbooks.Objects;

public class Notification {

    private String book_name;
    private String userWantsTheBookId;

    public Notification(){}
    public Notification(String uid,String bookName){
        this.book_name=bookName;
        this.userWantsTheBookId=uid;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getUserWantsTheBookId() {
        return userWantsTheBookId;
    }

    public void setUserWantsTheBookId(String userWantsTheBookId) {
        this.userWantsTheBookId = userWantsTheBookId;
    }
}
