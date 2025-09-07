package com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MultipleChoiceUserSelectedAnswerId implements Serializable {
    private Long userId;
    private Long multipleChoiceOptionId;
}
