package com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock;

import com.denyyyys.fluentLift.model.postgres.entity.AppUser;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MultipleChoiceUserSelectedAnswer {
    @EmbeddedId
    private MultipleChoiceUserSelectedAnswerId id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @MapsId("multipleChoiceOptionId")
    @JoinColumn(name = "multiple_choice_option_id", nullable = false)
    private MultipleChoiceOption multipleChoiceOption;
}
