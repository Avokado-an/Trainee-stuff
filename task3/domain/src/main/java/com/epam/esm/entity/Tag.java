package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<GiftCertificate> certificates;

    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
