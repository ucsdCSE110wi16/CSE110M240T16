package com.parse.starter;

/**
 * Created by Cameron on 2/8/2016.
 */
public class Model {
    protected String name;

    public Model() {this.name = "";}

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
