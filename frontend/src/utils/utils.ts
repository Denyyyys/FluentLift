import { useOutletContext } from "react-router-dom";
import type { CardOwnerResponseDto } from "../types/card";
import { type CourseResponse, type TextBlockResponse, type UiLesson, type UiClozeBlock, type UiMultipleChoiceBlock, type UiCourse } from "../types/course";
import type { ALL_LANGUAGES_TYPE, AVAILABLE_LANGUAGES_TYPE } from "../constants";

export function sortByNextReviewDate(cards: CardOwnerResponseDto[]) {
    return [...cards].sort(
        (a, b) =>
            new Date(a.nextReviewDate).getTime() - new Date(b.nextReviewDate).getTime()
    );
}

export function countNumberOfLessons(course: UiCourse) {
    let totalNumber = 0;
    course.units.forEach(unit => {
        totalNumber += unit.lessons.length;
    });

    return totalNumber;
}

export function countProgress(totalNumberBlocks: number, totalNumberFinishedBlocks: number): number {
    return Math.round((totalNumberFinishedBlocks / totalNumberBlocks) * 100);
}

export function sortCourseContent(course: CourseResponse) {
    course.units.sort((a, b) => a.unitNumber - b.unitNumber)
    course.units.forEach(unit => unit.lessons.sort((a, b) => a.lessonNumber - b.lessonNumber));
}

export function getSortedBlocksForUiLesson(lesson: UiLesson): (TextBlockResponse | UiClozeBlock | UiMultipleChoiceBlock)[] {
    const blocks: Array<TextBlockResponse | UiClozeBlock | UiMultipleChoiceBlock> = [];

    lesson.textBlocks.forEach(textBlock => blocks.push(textBlock));
    lesson.clozeBlocks.forEach(clozeBlock => { blocks.push(clozeBlock) })
    lesson.multipleChoiceBlocks.forEach(multipleChoiceBlock => { blocks.push(multipleChoiceBlock) })

    blocks.sort((a, b) => a.blockNumber - b.blockNumber)
    return blocks;
}

export type CourseContextType = { uiCourse: UiCourse, updateLesson: (uiLesson: UiLesson) => void };

export function useCourse() {
    return useOutletContext<CourseContextType>();
}

function getAdjacentLesson(course: UiCourse, currLessonNumber: number, offset: number): UiLesson | undefined {
    for (const unit of course.units) {
        let lesson = unit.lessons.find(lesson => lesson.lessonNumber === currLessonNumber + offset);
        if (lesson) {
            return lesson;
        }
    }
    return undefined;
}

export function getPreviousLesson(course: UiCourse, currLessonNumber: number): UiLesson | undefined {
    return getAdjacentLesson(course, currLessonNumber, -1);
}

export function getNextLesson(course: UiCourse, currLessonNumber: number): UiLesson | undefined {
    return getAdjacentLesson(course, currLessonNumber, 1);
}

export function getLanguageCode(languageName: String) {
    switch (languageName) {
        case "English":
            return "EN"
        case "Polish":
            return "PL"
        default:
            return ""
    }
}

export function translateLanguageName(targetLanguage: AVAILABLE_LANGUAGES_TYPE, languageToTranslate: ALL_LANGUAGES_TYPE) {
    const languagesNames = {
        "English": {
            "English": "English",
            "Polish": "Polish",
            "Ukrainian": "Ukrainian",
            "German": "German",
            "Spanish": "Spanish"
        },
        "Polish": {
            "English": "Angielski",
            "Polish": "Polski",
            "Ukrainian": "Ukraiński",
            "German": "Niemiecki",
            "Spanish": "Hiszpański"
        },
        "Ukrainian": {
            "English": "Англійська",
            "Polish": "Польська",
            "Ukrainian": "Українська",
            "German": "Німецька",
            "Spanish": "Іспанська"
        }
    }

    return languagesNames[targetLanguage][languageToTranslate];
}