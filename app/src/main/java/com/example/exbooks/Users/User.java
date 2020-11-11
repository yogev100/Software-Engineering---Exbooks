package com.example.exbooks.Users;

public abstract class User {
    private String username,
            fname,
            lname,
            email,
            password,
            city,
            phone;

    public User(String username, String fname, String lname, String email,
                String password, String city, String phone){
        this.username=username;
        this.fname=fname;
        this.lname=lname;
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

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
