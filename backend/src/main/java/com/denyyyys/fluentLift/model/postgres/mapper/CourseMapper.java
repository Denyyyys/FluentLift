package com.denyyyys.fluentLift.model.postgres.mapper;

import java.util.ArrayList;
import java.util.List;

import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.LessonCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.UnitCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.ClozeBlockAnswerDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.ClozeBlockCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.LessonBlockCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.MultipleChoiceBlockCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.MultipleChoiceOptionDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.TextBlockCreateDto;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.model.postgres.entity.course.Lesson;
import com.denyyyys.fluentLift.model.postgres.entity.course.Unit;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlock;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlockAnswer;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceBlock;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceOption;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.TextBlock;

public class CourseMapper {
    public static Course toEntity(CourseCreateDto courseDto) {
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setGoals(courseDto.getGoals());
        course.setPrerequisiteLevel(courseDto.getPrerequisiteLevel());
        course.setOutcomeLevel(courseDto.getOutcomeLevel());
        course.setBaseLanguage(courseDto.getBaseLanguage());
        course.setTargetLanguage(courseDto.getTargetLanguage());

        course.setUnits(
                courseDto.getUnits().stream().map((unitDto) -> {
                    return toEntity(course, unitDto);
                }).toList());

        return course;
    }

    public static Unit toEntity(Course course, UnitCreateDto unitDto) {
        Unit unit = new Unit();
        unit.setCourse(course);
        unit.setTitle(unitDto.getTitle());
        unit.setOverview(unitDto.getOverview());
        unit.setUnitNumber(unitDto.getUnitNumber());
        unit.setLessons(
                unitDto.getLessons().stream().map(lessonDto -> {
                    return toEntity(unit, lessonDto);
                }).toList());
        return unit;
    }

    public static Lesson toEntity(Unit unit, LessonCreateDto lessonDto) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setLessonNumber(lessonDto.getLessonNumber());
        lesson.setUnit(unit);

        List<TextBlockCreateDto> textBlockDtos = new ArrayList<>();
        List<ClozeBlockCreateDto> clozeBlockDtos = new ArrayList<>();
        List<MultipleChoiceBlockCreateDto> multipleChoiceBlockDtos = new ArrayList<>();

        for (LessonBlockCreateDto lessonBlockDto : lessonDto.getBlocks()) {
            if (lessonBlockDto instanceof TextBlockCreateDto textBlockCreateDto) {
                textBlockDtos.add(textBlockCreateDto);
            } else if (lessonBlockDto instanceof ClozeBlockCreateDto clozeBlockCreateDto) {
                clozeBlockDtos.add(clozeBlockCreateDto);
            } else if (lessonBlockDto instanceof MultipleChoiceBlockCreateDto multipleChoiceBlockCreateDto) {
                multipleChoiceBlockDtos.add(multipleChoiceBlockCreateDto);
            } else {
                throw new IllegalArgumentException("Unsupported LessonBlock type: " + lessonBlockDto.getClass());
            }
        }

        lesson.setTextBlocks(
                textBlockDtos.stream().map(textBlockDto -> {
                    return toEntity(lesson, textBlockDto);
                }).toList());

        lesson.setClozeBlocks(
                clozeBlockDtos.stream().map(clozeDto -> toEntity(lesson, clozeDto)).toList());

        lesson.setMultipleChoiceBlocks(multipleChoiceBlockDtos.stream().map(choiceBlock -> {
            return toEntity(lesson, choiceBlock);
        }).toList());

        return lesson;
    }

    public static TextBlock toEntity(Lesson lesson, TextBlockCreateDto textBlockCreateDto) {
        TextBlock textBlock = new TextBlock();
        textBlock.setLesson(lesson);
        textBlock.setBlockNumber(textBlockCreateDto.getBlockNumber());
        textBlock.setType(textBlockCreateDto.getTextBlockType());
        textBlock.setText(textBlockCreateDto.getText());

        return textBlock;
    }

    public static ClozeBlock toEntity(Lesson lesson, ClozeBlockCreateDto clozeBlockCreateDto) {
        ClozeBlock clozeBlock = new ClozeBlock();

        clozeBlock.setLesson(lesson);
        clozeBlock.setBlockNumber(clozeBlockCreateDto.getBlockNumber());
        clozeBlock.setQuestion(clozeBlockCreateDto.getQuestion());
        clozeBlock.setTemplate(clozeBlockCreateDto.getTemplate());
        clozeBlock.setAnswers(
                clozeBlockCreateDto.getAnswers().stream().map(answerDto -> {
                    return toEntity(clozeBlock, answerDto);
                }).toList());

        return clozeBlock;
    }

    public static ClozeBlockAnswer toEntity(ClozeBlock clozeBlock, ClozeBlockAnswerDto clozeBlockAnswerDto) {
        ClozeBlockAnswer answer = new ClozeBlockAnswer();

        answer.setCloze(clozeBlock);
        answer.setKey(clozeBlockAnswerDto.getKey());
        answer.setExpected(clozeBlockAnswerDto.getExpected());
        answer.setCaseSensitive(clozeBlockAnswerDto.getCaseSensitive());

        return answer;
    }

    public static MultipleChoiceBlock toEntity(Lesson lesson,
            MultipleChoiceBlockCreateDto multipleChoiceBlockCreateDto) {
        MultipleChoiceBlock block = new MultipleChoiceBlock();

        block.setLesson(lesson);
        block.setBlockNumber(multipleChoiceBlockCreateDto.getBlockNumber());
        block.setQuestion(multipleChoiceBlockCreateDto.getQuestion());

        block.setChoiceOptions(multipleChoiceBlockCreateDto.getChoices().stream().map(choice -> {
            return toEntity(block, choice);
        }).toList());

        return block;
    }

    public static MultipleChoiceOption toEntity(MultipleChoiceBlock block, MultipleChoiceOptionDto choiceOptionDto) {
        MultipleChoiceOption option = new MultipleChoiceOption();

        option.setMultipleChoiceBlock(block);
        option.setText(choiceOptionDto.getText());
        option.setIsCorrect(choiceOptionDto.getIsCorrect());

        return option;
    }
}
