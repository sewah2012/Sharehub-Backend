package com.luslusdawmpfe.PFEBackent.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private AppUser author;
    private String description;
    private LocalDate dateCreated;
    private Boolean isEnabled;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;
//
//    @Transient
//    private Long experience_id;
}
