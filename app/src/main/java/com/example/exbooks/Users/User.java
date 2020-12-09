package com.example.exbooks.Users;

public abstract class User {
    private String fullname,
            email,
            password,
            city,
            phone;


    public User(){}
    public User(String fullname, String email,
                String password, String city, String phone){
        this.fullname=fullname;
        this.email=email;
        this.password=password;
        this.city=city;
        this.phone=phone;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getfullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
