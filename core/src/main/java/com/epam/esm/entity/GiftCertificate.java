package com.epam.esm.entity;


import com.epam.esm.converter.DateConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name="certificates")
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private double price;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="Europe/Minsk")
    private Date creationDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="Europe/Minsk")
    private Date lastUpdateDate = new Date();

    @Transient
    private DateConverter converter;
    private short duration;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE
            })
    @JoinTable(name="certificate_tags",
            joinColumns = @JoinColumn(name="certificate_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private Set<Tag> tags;
//advice
    @JsonIgnore
    @ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;


    public GiftCertificate() {
        this.converter = new DateConverter();
        this.tags = new HashSet<>();
    }

    public GiftCertificate(int id, String name, String description, double price, Timestamp creationDate, Timestamp lastUpdateDate, short duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.duration = duration;
        this.tags = new HashSet<>();
        this.converter = new DateConverter();
    }


    public Date parse(String date) {
        TimeZone tz = TimeZone.getTimeZone("Europe/Minsk");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return Double.compare(that.price, price) == 0 &&
                duration == that.duration &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(converter, that.converter) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, creationDate, lastUpdateDate, converter, duration, tags, user);
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", converter=" + converter +
                ", duration=" + duration +
                ", tags=[" + tags +
                "], user=" + user +
                '}';
    }
}
