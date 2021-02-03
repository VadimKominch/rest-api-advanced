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
@Table(name="orders")
public class Order extends RepresentationModel<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Double price;

    @Column(name = "buy_date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="Europe/Minsk")
    private String buyDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE
            })
    @JoinTable(name="certificate_orders",
            joinColumns = @JoinColumn(name="certificate_id"),
            inverseJoinColumns = @JoinColumn(name="order_id"))
    private List<GiftCertificate> certificates;

    @Transient
    private DateConverter converter;

    public Order() {
        converter = new DateConverter();
        this.certificates = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<GiftCertificate> getCertificate() {
        return certificates;
    }

    public void setCertificate(List<GiftCertificate> certificates) {
        this.certificates = certificates;
        if(certificates != null)
            this.price = certificates.stream().map(GiftCertificate::getPrice).reduce(0.0, Double::sum);
        else
            this.price = 0.0;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = converter.formatDate(buyDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id &&
                price.equals(order.price) &&
                buyDate.equals(order.buyDate) &&
                user.equals(order.user) &&
                certificates.equals(order.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, buyDate, user, certificates);
    }
}
