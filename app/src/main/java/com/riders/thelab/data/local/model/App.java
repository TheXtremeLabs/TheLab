package com.riders.thelab.data.local.model;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class App {

    // From Packages
    String name;
    Drawable drawableIcon;
    String version;
    String packageName;

    // From activities
    private String title;
    private String description;
    private int icon;
    private Class<? extends Activity> activity;


    /**
     * Default constructor
     */
    public App() {
    }


    /**
     * Use for app packages
     *
     * @param name
     * @param drawableIcon
     * @param version
     * @param packageName
     */
    public App(String name, Drawable drawableIcon, String version, String packageName) {
        this.name = name;
        this.drawableIcon = drawableIcon;
        this.version = version;
        this.packageName = packageName;
    }


    /**
     * Use for activities
     *
     * @param title
     * @param description
     * @param icon
     * @param activity
     */
    public App(String title, String description, int icon, Class<? extends Activity> activity) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.activity = activity;
    }
}
