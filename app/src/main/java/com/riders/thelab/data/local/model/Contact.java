package com.riders.thelab.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Contract;
import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "contact")
@Setter
@Getter
@ToString
@Parcel
public class Contact {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public Long id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "url_image")
    public String encodedImage;

    @Contract(pure = true)
    public Contact() {
        id = 0L;
    }
}
