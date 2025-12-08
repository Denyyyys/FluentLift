import { useEffect, useState } from "react";
import { Outlet, useParams } from "react-router-dom";
import { BlockType, type ClozeBlockResponse, type ClozeBlockUserAnswer, type CourseAnswers, type CourseResponse, type LessonAnswers, type LessonResponse, type MultipleChoiceBlockResponse, type UiClozeBlock, type UiClozeBlockAnswer, type UiCourse, type UiLesson, type UiMultipleChoiceBlock, type UiMultipleChoiceOption, type UiUnit, type UnitAnswers, type UnitResponse, type ProgressInfo } from "../types/course";
import { useAuth } from "../context/AuthContext";
import axios, { HttpStatusCode } from "axios";
import { BACKEND_BASE_URL } from "../constants";
import { countProgress, type CourseContextType } from "../utils/utils";
import LoadingSpinner from "../components/common/LoadingSpinner";
import UnreachableState from "../components/common/UnreachableState";


function CourseLayout() {
    const { courseId } = useParams<{ courseId: string }>();
    // const [course, setCourse] = useState<CourseResponse | null>(null);
    const [uiCourse, setUiCourse] = useState<UiCourse | null>(null);
    const [loadingCourse, setLoadingCourse] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const { token } = useAuth();

    const updateLesson = (updatedLesson: UiLesson) => {
        setUiCourse(prev => {
            if (!prev) {
                return prev;
            }

            return {
                ...prev,
                units: prev.units.map(unit => {
                    if (unit.lessons.find(lesson => lesson.id === updatedLesson.id)) {
                        unit = {
                            ...unit,
                            lessons: unit.lessons.map(lesson => {
                                if (lesson.id === updatedLesson.id) {
                                    return updatedLesson;
                                }
                                return lesson;
                            })
                        }
                    }
                    return unit;
                })
            }
        }
        )
    }

    const getNumberFinishedClozeAnswer = (clozeBlocks: UiClozeBlock[]): number => {
        return clozeBlocks.flatMap(block => block.answers).filter(answer => !!answer.userAnswer).length;
    }

    const getNumberFinishedMCBlocks = (mcBlocks: UiMultipleChoiceBlock[]): number => {
        return mcBlocks.reduce((acc, currBlock) => {
            return acc + (currBlock.choiceOptions.some(option => option.isSelected) ? 1 : 0);
        }, 0);
    }

    const normalizeCourse = (course: CourseResponse, courseAnswers: CourseAnswers): UiCourse => {
        let uiCourse = {
            baseLanguage: course.baseLanguage,
            creator: course.creator,
            title: course.title,
            description: course.description,
            goals: course.goals,
            prerequisiteLevel: course.prerequisiteLevel,
            outcomeLevel: course.outcomeLevel,
            targetLanguage: course.targetLanguage,
            id: course.id,
            progressInfo: {
                progress: 0,
                finishedBlocks: 0,
                totalBlocks: 0
            } as ProgressInfo,
            units: [] as UiUnit[]
        } satisfies UiCourse;

        if (course.units.length !== courseAnswers.unitAnswers.length) {
            throw Error("lengths of fetched course's units and course's units answer are not the same")
        }

        course.units.sort((a, b) => a.id - b.id);
        courseAnswers.unitAnswers.sort((a, b) => a.unitId - b.unitId);
        let totalFinishedBlocks = 0;
        let totalNumberBlocks = 0;

        for (let i = 0; i < courseAnswers.unitAnswers.length; i++) {
            if (courseAnswers.unitAnswers[i].unitId !== course.units[i].id) {
                throw Error("id of fetched course's unit id and course's unit answer id are not the same when they should be. Probably these arrays contains different values inside")
            }
            const normalizedUnit = normalizeUnit(course.units[i], courseAnswers.unitAnswers[i]);
            totalFinishedBlocks += normalizedUnit.progressInfo.finishedBlocks
            totalNumberBlocks += normalizedUnit.progressInfo.totalBlocks
            uiCourse.units.push(normalizedUnit)
        }

        uiCourse.progressInfo.finishedBlocks = totalFinishedBlocks;
        uiCourse.progressInfo.totalBlocks = totalNumberBlocks;
        uiCourse.progressInfo.progress = countProgress(totalNumberBlocks, totalFinishedBlocks)

        return uiCourse;
    }

    const normalizeUnit = (unit: UnitResponse, unitAnswers: UnitAnswers): UiUnit => {
        let uiUnit = {
            id: unit.id,
            overview: unit.overview,
            title: unit.title,
            unitNumber: unit.unitNumber,
            progressInfo: {
                progress: 0,
                finishedBlocks: 0,
                totalBlocks: 0
            } as ProgressInfo,
            lessons: [] as UiLesson[]
        } satisfies UiUnit;

        if (unit.lessons.length !== unitAnswers.lessonAnswers.length) {
            throw Error("lengths of fetched unit's lessons and unit's lesson answer are not the same")
        }

        unit.lessons.sort((a, b) => a.id - b.id);
        unitAnswers.lessonAnswers.sort((a, b) => a.lessonId - b.lessonId);

        let totalFinishedBlocks = 0;
        let totalNumberBlocks = 0;
        for (let i = 0; i < unit.lessons.length; i++) {
            if (unit.lessons[i].id !== unitAnswers.lessonAnswers[i].lessonId) {
                throw Error("id of fetched unit's lesson id and unit's lesson answer id are not the same when they should be. Probably these arrays contains different values inside")
            }

            const normalizedLesson = normalizeLesson(unit.lessons[i], unitAnswers.lessonAnswers[i]);
            totalFinishedBlocks += normalizedLesson.progressInfo.finishedBlocks
            totalNumberBlocks += normalizedLesson.progressInfo.totalBlocks

            uiUnit.lessons.push(normalizedLesson)

        }

        uiUnit.progressInfo.finishedBlocks = totalFinishedBlocks;
        uiUnit.progressInfo.totalBlocks = totalNumberBlocks;
        uiUnit.progressInfo.progress = countProgress(totalNumberBlocks, totalFinishedBlocks)

        return uiUnit;
    }

    const normalizeLesson = (lesson: LessonResponse, lessonAnswers: LessonAnswers): UiLesson => {
        let uiLesson = {
            progressInfo: {
                finishedBlocks: 0,
                progress: 0,
                totalBlocks: 0
            } as ProgressInfo,
            id: lesson.id,
            title: lesson?.title,
            lessonNumber: lesson.lessonNumber,
            textBlocks: lesson.textBlocks,
            clozeBlocks: [] as UiClozeBlock[],
            multipleChoiceBlocks: [] as UiMultipleChoiceBlock[]
        } satisfies UiLesson;

        uiLesson.clozeBlocks = lesson.clozeBlocks.map(clozeBlock => normalizeClozeBlock(clozeBlock, lessonAnswers.clozeAnswers))
        uiLesson.multipleChoiceBlocks = lesson.multipleChoiceBlocks.map(mcBlock => normalizeMCBlock(mcBlock, lessonAnswers.userSelectedMcosIds))

        const totalNumberFinishedBlocks = getNumberFinishedClozeAnswer(uiLesson.clozeBlocks) + getNumberFinishedMCBlocks(uiLesson.multipleChoiceBlocks)
        const totalNumberBlocks = uiLesson.clozeBlocks.flatMap(ClozeBlock => ClozeBlock.answers).length + uiLesson.multipleChoiceBlocks.length

        uiLesson.progressInfo.progress = countProgress(totalNumberBlocks, totalNumberFinishedBlocks)
        uiLesson.progressInfo.finishedBlocks = totalNumberFinishedBlocks;
        uiLesson.progressInfo.totalBlocks = totalNumberBlocks;

        return uiLesson
    }

    const normalizeClozeBlock = (clozeBlock: ClozeBlockResponse, clozeBlockUserAnswers: ClozeBlockUserAnswer[]): UiClozeBlock => {
        let uiClozeBlock = {
            id: clozeBlock.id,
            type: BlockType.Cloze,
            blockNumber: clozeBlock.blockNumber,
            question: clozeBlock.question,
            template: clozeBlock.template,
            answers: [] as UiClozeBlockAnswer[]
        } as UiClozeBlock;

        uiClozeBlock.answers = clozeBlock.answers.map(answer => {
            let uiAnswer = {
                id: answer.id,
                caseSensitive: answer.caseSensitive,
                expected: answer.expected,
                key: answer.key,
                userAnswer: ""
            } satisfies UiClozeBlockAnswer;

            let foundUserAnswer = clozeBlockUserAnswers.find(clozeUserAnswer => clozeUserAnswer.clozeBlockAnswerId === answer.id);

            if (foundUserAnswer !== undefined) {
                uiAnswer.userAnswer = foundUserAnswer.userInput;
            }

            return uiAnswer;
        });

        return uiClozeBlock;
    }

    const normalizeMCBlock = (mcBlock: MultipleChoiceBlockResponse, mcUserSelectedOptionIds: number[]): UiMultipleChoiceBlock => {
        let uiMcBlock = {
            id: mcBlock.id,
            blockNumber: mcBlock.blockNumber,
            type: BlockType.MultipleChoice,
            question: mcBlock.question,
            choiceOptions: [] as UiMultipleChoiceOption[]
        } satisfies UiMultipleChoiceBlock;

        uiMcBlock.choiceOptions = mcBlock.choiceOptions.map(choiceOption => {
            let uiChoiceOption = {
                id: choiceOption.id,
                isCorrect: choiceOption.isCorrect,
                text: choiceOption.text,
                isSelected: false as boolean
            } satisfies UiMultipleChoiceOption;

            let foundUserSelectedChoiceOption = mcUserSelectedOptionIds.find(mcUserSelectedOption => mcUserSelectedOption === choiceOption.id);
            if (foundUserSelectedChoiceOption !== undefined) {
                uiChoiceOption.isSelected = true;
            }

            return uiChoiceOption;
        })

        return uiMcBlock;
    }

    const fetchCourse = async (courseId: string) => {
        try {
            setLoadingCourse(true);
            setError(null);

            setUiCourse(null);
            const courseResponse = await axios.get<CourseResponse>(`${BACKEND_BASE_URL}/courses/${courseId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            const courseAnswersResponse = await axios.get<CourseAnswers>(`${BACKEND_BASE_URL}/courses/${courseId}/user-answers`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setUiCourse(normalizeCourse(courseResponse.data, courseAnswersResponse.data));

        } catch (error) {
            console.error(`Axios Error while getting enrollment for course with id ${courseId}:`, error);

            setError(handleCourseError(error, courseId));
        } finally {
            setLoadingCourse(false);
        }
    }
    useEffect(() => {
        console.log("useEffect for fetching course");
        if (courseId) {
            fetchCourse(courseId);
        }
    }, [courseId])

    const handleCourseError = (error: unknown, courseId: string) => {
        if (axios.isAxiosError(error)) {
            console.error(`Axios Error while getting course with id ${courseId}:`, error);
            if (error.status === HttpStatusCode.NotFound) {
                return "course-not-found";
            } else {
                return "axios-general-error";
            }
        } else {
            console.error(`Something went wrong while getting course with id ${courseId}:`, error);
            return "general-fetching-error";
        }
    }

    if (loadingCourse) {
        return <LoadingSpinner />
    }

    if (error !== null) {
        switch (error) {
            case "course-not-found":
                return <div>
                    <h2>Ooops it seems like course which you are looking for doesn't exist or was deleted or archived.</h2>
                </div>
            default:
                return <div>
                    <h2>Some unexpected error happened. See console logs for more info.</h2>
                </div>
        }
    }

    if (!uiCourse) {
        return <UnreachableState />
    }

    const context: CourseContextType = { uiCourse, updateLesson };

    return <Outlet context={context} />
}

export default CourseLayout