package com.luslusdawmpfe.PFEBackent.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@Entity
@Table(name = "experience")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experience")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "experience")
    @Builder.Default
    private List<Attachement> attachments = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private Set<String> likes = new HashSet<>();

    private Boolean isActive = false;
}