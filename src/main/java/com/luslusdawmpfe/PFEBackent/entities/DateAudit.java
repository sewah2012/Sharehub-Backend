package com.luslusdawmpfe.PFEBackent.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(
        value = {"creationDate", "updatedDate"},
        allowGetters = true
)
public abstract class DateAudit {

    @CreatedDate
    @Column(name = "created_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "updated_on", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime updatedDate;
}