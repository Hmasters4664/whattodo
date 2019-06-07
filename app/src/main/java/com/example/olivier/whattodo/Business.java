package com.example.olivier.whattodo;

public class Business {
    private String bname,oname,description;
     private int  number;


    public Business(){

    }
    public Business(String business,String owner,String desc, int num){
        this.bname = business;
        this.description=desc;

}

    public String getBname() {
        return bname;
    }

    public String getDescription() {
        return description;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
