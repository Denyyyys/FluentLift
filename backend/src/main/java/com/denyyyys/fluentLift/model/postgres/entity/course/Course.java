package com.denyyyys.fluentLift.model.postgres.entity.course;

import java.util.ArrayList;
import java.util.List;

import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlockAnswer;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceBlock;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private AppUser creator;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @Column(columnDefinition = "TEXT", name = "goal")
    private List<String> goals = new ArrayList<>();

    @Column(nullable = false)
    private String prerequisiteLevel;

    @Column(nullable = false)
    private String outcomeLevel;

    @Column(nullable = false)
    private String baseLanguage;

    @Column(nullable = false)
    private String targetLanguage;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Unit> units = new ArrayList<>();

    public List<ClozeBlockAnswer> getClozeBlockAnswers() {
        return this.getUnits().stream()
                .flatMap(unit -> unit.getLessons().stream()
                        .flatMap(lesson -> lesson.getClozeBlocks().stream()
                                .flatMap(clozeBlock -> clozeBlock.getAnswers().stream())))
                .toList();
    }

    public List<MultipleChoiceBlock> getMultipleChoiceBlocks() {
        return this.getUnits().stream()
                .flatMap(unit -> unit.getLessons().stream()
                        .flatMap(lesson -> lesson.getMultipleChoiceBlocks().stream()))
                .toList();
    }

}
