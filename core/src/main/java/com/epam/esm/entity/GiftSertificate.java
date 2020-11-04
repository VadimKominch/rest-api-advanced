package com.epam.esm.entity;


import com.epam.esm.converter.DateConverter;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftSertificate {

    private int id;
    private String name;
    private String description;
    private double price;
    private String creationDate;
    private String lastUpdateDate;

    @Transient
    private DateConverter converter;
    private short duration;
    private List<Tag> tags;

    public GiftSertificate() {
        this.converter = new DateConverter();
        this.tags = new ArrayList<>();
    }

    public GiftSertificate(int id, String name, String description, double price,String creationDate,String lastUpdateDate,short duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.tags = new ArrayList<>();
        this.converter = new DateConverter();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = converter.formatDate(date);
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date date) {
        this.lastUpdateDate = converter.formatDate(date);
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tagList) {
        this.tags = tagList;
    }

    @Override
    public String toString() {
        return "GiftSertificate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
