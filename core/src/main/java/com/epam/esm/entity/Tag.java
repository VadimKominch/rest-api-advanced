package com.epam.esm.entity;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="tags")
public class Tag  extends RepresentationModel<Tag> {
    @Column(name="name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Tag() {
    }

    public Tag(String name,int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
