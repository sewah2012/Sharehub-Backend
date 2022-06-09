package com.luslusdawmpfe.PFEBackent.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Attachement extends DateAudit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    private AttachementType type;
    private String attachmentUrl;
}