import type { CardOwnerResponseDto } from "../types/card";
import { type CourseResponse, type CourseUnitResponse, type LessonProgress, type LessonResponse, type MultipleChoiceBlockResponse, type UnitProgress, type TextBlockResponse, BlockType, type UiLesson, type UiClozeBlock, type UiMultipleChoiceBlock } from "../types/course";

export function sortByNextReviewDate(cards: CardOwnerResponseDto[]) {
    return [...cards].sort(
        (a, b) =>
            new Date(a.nextReviewDate).getTime() - new Date(b.nextReviewDate).getTime()
    );
}

export function countNumberOfLessons(course: CourseResponse) {
    let totalNumber = 0;
    course.units.forEach(unit => {
        totalNumber += unit.lessons.length;
    });

    return totalNumber;
}

export function getUnitProgress(unitId: number, unitProgresses: UnitProgress[]) {
    return unitProgresses.find(unit => unit.unitId === unitId);
}

export function getLessonProgress(lessonId: number, lessonProgresses: LessonProgress[]) {
    return lessonProgresses.find(lesson => lesson.lessonId === lessonId);
}

export function getUnitForLessonId(course: CourseResponse, lessonId: number) {

    const unit = course.units.find(unit => {
        return unit.lessons.some(lesson => lesson.id === lessonId)
    });

    if (!unit) {
        throw new Error(`Lesson with id ${lessonId} was not found`)
    }

    return unit;
}

export function getLesson(unit: CourseUnitResponse, lessonId: number) {
    const lesson = unit.lessons.find(lesson => lesson.id === lessonId);

    if (!lesson) {
        throw new Error(`Lesson with id ${lessonId} was not found`)
    }

    return lesson;
}

export function sortCourseContent(course: CourseResponse) {
    course.units.sort((a, b) => a.unitNumber - b.unitNumber)
    course.units.forEach(unit => unit.lessons.sort((a, b) => a.lessonNumber - b.lessonNumber));
}

export function getSortedBlocksForUiLesson(lesson: UiLesson) {
    const blocks: Array<TextBlockResponse | UiClozeBlock | UiMultipleChoiceBlock> = [];

    lesson.textBlocks.forEach(textBlock => blocks.push(textBlock));
    lesson.clozeBlocks.forEach(clozeBlock => { blocks.push(clozeBlock) })
    lesson.multipleChoiceBlocks.forEach(multipleChoiceBlock => { blocks.push(multipleChoiceBlock) })

    blocks.sort((a, b) => a.blockNumber - b.blockNumber)
    return blocks;
} 