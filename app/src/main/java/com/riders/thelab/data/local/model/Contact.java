package com.riders.thelab.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "password")
    public String password;

    @Contract(pure = true)
    public Contact() {
        id = 0L;
    }

    @Ignore
    public Contact(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
