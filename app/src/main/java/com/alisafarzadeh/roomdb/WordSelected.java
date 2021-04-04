package com.alisafarzadeh.roomdb;

public class WordSelected {
    String persian;
    String english;

    public WordSelected(String persian, String english) {
        this.persian = persian;
        this.english = english;
    }

    public String getPersian() {
        return persian;
    }

    public String getEnglish() {
        return english;
    }

    public void setPersian(String persian) {
        this.persian = persian;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
