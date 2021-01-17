package com.epam.esm.entity;

import org.springframework.hateoas.RepresentationModel;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="users")
public class User extends RepresentationModel<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<GiftCertificate> certificates;

    public User(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.certificates = new ArrayList<>();
    }

    public User() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<GiftCertificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(certificates, user.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, certificates);
    }
}
