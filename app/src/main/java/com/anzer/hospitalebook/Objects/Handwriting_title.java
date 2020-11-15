package com.anzer.hospitalebook.Objects;

/**
 * Created by David on 5/21/2018.
 */

public class Handwriting_title {
    private static String title;

    public Handwriting_title(String title){
        this.title = title;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
