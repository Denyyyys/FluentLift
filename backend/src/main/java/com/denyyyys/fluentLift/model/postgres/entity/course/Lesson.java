package com.denyyyys.fluentLift.model.postgres.entity.course;

import java.util.ArrayList;
import java.util.List;

import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlock;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlockAnswer;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceBlock;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.TextBlock;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false)
    private Integer lessonNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id", nullable = false)
    @JsonBackReference
    private Unit unit;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TextBlock> textBlocks = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ClozeBlock> clozeBlocks = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MultipleChoiceBlock> multipleChoiceBlocks = new ArrayList<>();

    public List<ClozeBlockAnswer> getClozeBlockAnswers() {
        return this.getClozeBlocks().stream()
                .flatMap(clozeBlock -> clozeBlock.getAnswers().stream()).toList();
    }
}
