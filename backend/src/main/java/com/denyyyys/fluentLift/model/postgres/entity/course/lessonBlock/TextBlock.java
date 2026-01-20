package com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock;

import com.denyyyys.fluentLift.model.postgres.entity.course.Lesson;
import com.denyyyys.fluentLift.model.postgres.enums.TextBlockType;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonBackReference
    private Lesson lesson;

    @Column(nullable = false)
    private int blockNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TextBlockType type;

    @Column(nullable = true, length = 1500)
    private String text;

}
