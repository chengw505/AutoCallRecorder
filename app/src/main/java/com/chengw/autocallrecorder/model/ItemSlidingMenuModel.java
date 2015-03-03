package com.chengw.autocallrecorder.model;

/**
 * Created by Cheng on 3/2/2015.
 */
public class ItemSlidingMenuModel {

    private final String name;
    private final Integer id;

    public ItemSlidingMenuModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
