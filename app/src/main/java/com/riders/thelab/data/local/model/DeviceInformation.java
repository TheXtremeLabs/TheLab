package com.riders.thelab.data.local.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DeviceInformation {

    private String name;
    private String brand;
    private String model;
    private String serial;
    private String fingerPrint;
    private String hardware;
    private String IMEI;
    private String id;
    private int screenWidth;
    private int screenHeight;

    private String androidVersionName;
    private int sdkVersion;
    private String androidRelease;
    private boolean rooted;


    public DeviceInformation() {
    }
}
