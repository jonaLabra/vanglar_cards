package com.example.vanlgar;

public class model {
    String banner;
    String elector_key;
    String name;
    String photoUrl;
    String section;
    String state;
    String town;

    public model(){

    }

    public model(String Banner,String Elector_key, String Name,String PhotoUrl,String Section,String State,String Town){
        this.banner = Banner;
        this.elector_key = Elector_key;
        this.name = Name;
        this.photoUrl = PhotoUrl;
        this.section = Section;
        this.state = State;
        this.town = Town;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getElector_key() {
        return elector_key;
    }

    public void setElector_key(String elector_key) {
        this.elector_key = elector_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
