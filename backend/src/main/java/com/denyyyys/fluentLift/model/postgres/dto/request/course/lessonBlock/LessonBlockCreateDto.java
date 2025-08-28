package com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextBlockCreateDto.class, name = "text"),
        @JsonSubTypes.Type(value = ClozeBlockCreateDto.class, name = "cloze"),
        @JsonSubTypes.Type(value = MultipleChoiceBlockCreateDto.class, name = "multipleChoice")
})
public abstract class LessonBlockCreateDto {
    @NotNull
    private Integer blockNumber;

    public abstract String getType();

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }
}
