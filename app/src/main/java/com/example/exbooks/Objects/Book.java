package com.example.exbooks.Objects;

enum Condition{
    NEW,USED,TORN
}
public class  Book {
    private String book_name;
    private String author_name;
    private String category;
    private int num_pages;
    private Condition book_cond;
    private boolean for_change;
    private String book_id;
    private String uid;
    private String cityOwner;
    private boolean img_url;

    public Book(){}

    public Book(String book_name, String category, String author_name, int num_pages, int book_cond, boolean for_change, String uid, boolean url){
        this.book_name = book_name;
        this.init_cond(book_cond);
        this.author_name = author_name;
        this.num_pages = num_pages;
        this.category = category;
        this.for_change = for_change;
        this.uid = uid;
        this.img_url = url;
        //this.cityOwner="None";
    }

    public Book(Book other){
        this.book_name = other.getBook_name();
        this.book_cond = other.getBook_cond();
        this.author_name = other.getAuthor_name();
        this.num_pages = other.getNum_pages();
        this.category = other.getCategory();
        this.for_change = other.isFor_change();
        this.uid = other.getUid();
        this.img_url = other.getImgURL();
        this.cityOwner= other.getCityOwner();
    }

    public void init_cond(int book_cond){
        switch (book_cond){
            case 0:
                this.book_cond = Condition.NEW;
                break;
            case 2:
                this.book_cond = Condition.TORN;
                break;
            default:
                this.book_cond = Condition.USED;
                break;
        }
    }

    public String cond_toString(Condition cond){
        switch (cond){
            case NEW:
                return "New Book";
            case TORN:
                return "Little Torn";
            default:
                return "Used Book";
        }
    }

    public String condString(){
        switch (book_cond){
            case NEW:
                return "New Book";
            case TORN:
                return "Little Torn";
            default:
                return "Used Book";
        }
    }



    public Condition getBook_cond() {
        return book_cond;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public int getNum_pages() {
        return num_pages;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setBook_cond(Condition book_cond) {
        this.book_cond = book_cond;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public void setNum_pages(int num_pages) {
        this.num_pages = num_pages;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isFor_change() {
        return for_change;
    }

    public void setFor_change(boolean for_change) {
        this.for_change = for_change;
    }

    public String getBook_id() {
        return book_id;
    }
    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public boolean getImgURL() {
        return img_url;
    }

    public void setImgURL(boolean img_url) {
        this.img_url = img_url;
    }

    public String getCityOwner() {
        return cityOwner;
    }

    public void setCityOwner(String cityOwner) {
        this.cityOwner = cityOwner;
    }
}

