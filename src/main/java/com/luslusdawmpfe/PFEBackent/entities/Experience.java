package com.luslusdawmpfe.PFEBackent.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "experience")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Experience extends DateAudit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ExperienceType experienceType;
    private String title;
    private String details;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AppUser author;

    @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL)
//    @JsonManagedReference
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL)
    @Builder.Default
//    @JsonManagedReference
    private List<Attachement> attachments = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private Set<String> likes = new HashSet<>();

    private Boolean isActive = false;
}