package com.chetan.wt;

public class students {

    String id;
    String mail;
    String qualification;
    String name;
    String city;
    int wallet;
    String durl;

    public students() {
    }


    public students(String id, String name, String email, String degree, String city, String durl, int wallet) {
        this.id = id;
        this.name = name;
        this.mail = email;
        this.qualification = degree;
        this.city = city;
        this.durl = durl;
        this.wallet = wallet;
    }
    public students(String id, String name, String email, String degree, String city, int wallet) {
        this.id = id;
        this.name = name;
        this.mail = email;
        this.qualification = degree;
        this.city = city;
        this.durl="https://i.imgur.com/tGbaZCY.jpg";
        this.wallet = wallet;
    }



    public String getDurl() {
        return durl;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getQualification() {
        return qualification;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public int getWallet() {
        return wallet;
    }
}


