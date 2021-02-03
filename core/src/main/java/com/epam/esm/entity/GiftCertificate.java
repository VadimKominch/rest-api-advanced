package com.epam.esm.entity;


import com.epam.esm.converter.DateConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "certificates")
public class GiftCertificate extends RepresentationModel<GiftCertificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="title")
    private String name;

    private String description;
    private Double price;
    @Column(name = "creation_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="Europe/Minsk")
    private String creationDate;
    @Column(name = "last_update_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="Europe/Minsk")
    private String lastUpdateDate;

    @Transient
    private DateConverter converter;

    private Short duration;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE
            })
    @JoinTable(name="certificate_tags",
            joinColumns = @JoinColumn(name="certificate_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private List<Tag> tags;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REMOVE
            },
            mappedBy = "certificates")
    @JsonIgnore
    private List<Order> orders;

    public GiftCertificate() {
        this.converter = new DateConverter();
        this.tags = new ArrayList<>();
        this.orders = new ArrayList<>();
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

    public Double getPrice() {
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

    public Short getDuration() {
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

    @JsonIgnore
    public List<Order> getOrderList() {
        return orders;
    }

    public void setOrderList(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "GiftSertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creationDate='" + creationDate + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate)) return false;
        if (!super.equals(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id == that.id &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                price.equals(that.price) &&
                creationDate.equals(that.creationDate) &&
                duration.equals(that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, price, creationDate, lastUpdateDate, duration);
    }
}
