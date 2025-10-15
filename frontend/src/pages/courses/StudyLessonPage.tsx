import { Link, useParams } from "react-router-dom"
import { BlockType, type CourseProgress, type LessonResponse, type UiClozeBlock, type UiClozeBlockAnswer, type UiLesson, type UiMultipleChoiceBlock, type UiMultipleChoiceOption } from "../../types/course";
import { useEffect, useMemo, useState } from "react";
import axios, { HttpStatusCode } from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { useAuth } from "../../context/AuthContext";
import { getLesson, getSortedBlocksForUiLesson, getUnitForLessonId, useCourse } from "../../utils/utils";
import LessonBlockSwitch from "../../components/courses/LessonBlockSwitch";
import { FaArrowLeft } from "react-icons/fa";
import { FaArrowRight } from "react-icons/fa";

function StudyLessonPage() {
    const { courseId, lessonId } = useParams();

    const { course } = useCourse();
    const [courseProgress, setCourseProgress] = useState<CourseProgress | null>();
    const [uiLesson, setUiLesson] = useState<UiLesson | null>(null);
    const [loadingCourseProgress, setLoadingCourseProgress] = useState(true);
    const [savingProgress, setSavingProgress] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const { token } = useAuth();

    // TODO check if works when uiLesson is removed from list of dependencies
    const blocks = useMemo(() => (uiLesson ? getSortedBlocksForUiLesson(uiLesson) : []), [uiLesson]);

    useEffect(() => {
        setLoadingCourseProgress(true);
        if (courseId) {
            fetchCourseProgress(courseId)
        }
    }, [courseId, lessonId])

    const saveProgress = async () => {
        setSavingProgress(true)
        try {

        } catch (e) {

        } finally {
            setSavingProgress(false)
        }
    }

    const normalizeLesson = (lesson: LessonResponse) => {
        let uiLesson: UiLesson = {
            id: lesson.id,
            title: lesson.title,
            lessonNumber: lesson.lessonNumber,
            clozeBlocks: [],
            multipleChoiceBlocks: [],
            textBlocks: []
        }

        uiLesson.textBlocks = lesson.textBlocks;
        uiLesson.clozeBlocks = lesson.clozeBlocks.map(clozeBlock => ({
            id: clozeBlock.id,
            blockNumber: clozeBlock.blockNumber,
            question: clozeBlock.question,
            template: clozeBlock.template,
            type: BlockType.Cloze,
            answers: clozeBlock.answers.map(answer => ({
                id: answer.id,
                caseSensitive: answer.caseSensitive,
                expected: answer.expected,
                key: answer.key,
                userAnswer: ""
            }) satisfies UiClozeBlockAnswer)
        }) satisfies UiClozeBlock)

        uiLesson.multipleChoiceBlocks = lesson.multipleChoiceBlocks.map(mcBlock => ({
            id: mcBlock.id,
            blockNumber: mcBlock.blockNumber,
            type: BlockType.MultipleChoice,
            question: mcBlock.question,
            choiceOptions: mcBlock.choiceOptions.map(option => ({
                id: option.id,
                isCorrect: option.isCorrect,
                text: option.text,
                isSelected: false,
            }) satisfies UiMultipleChoiceOption)
        }) satisfies UiMultipleChoiceBlock)


        return uiLesson;
    }

    const handleClozeAnswerChange = (blockId: number, answerId: number, value: string) => {
        setUiLesson(prev => {
            if (!prev) {
                return prev;
            }
            return {
                ...prev,
                clozeBlocks: prev.clozeBlocks.map(clozeBlock => {
                    if (clozeBlock.id === blockId) {
                        return {
                            ...clozeBlock,
                            answers: clozeBlock.answers.map(answer => {
                                if (answer.id === answerId) {
                                    return {
                                        ...answer,
                                        userAnswer: value
                                    };
                                } else {
                                    return answer;
                                }
                            })
                        }
                    } else {
                        return clozeBlock;
                    }
                })
            }
        })
    }

    const handleMultipleChoiceAnswerChange = (mcBlockId: number, mcOptionId: number, value: boolean) => {
        setUiLesson(prev => {
            if (!prev) {
                return prev;
            }
            return {
                ...prev,
                multipleChoiceBlocks: prev.multipleChoiceBlocks.map(mcBlock => {
                    if (mcBlock.id === mcBlockId) {
                        return {
                            ...mcBlock,
                            choiceOptions: mcBlock.choiceOptions.map(option => {
                                if (option.id === mcOptionId) {
                                    return {
                                        ...option,
                                        isSelected: value
                                    }
                                } else {
                                    return option;
                                }
                            })
                        }
                    } else {
                        return mcBlock;
                    }
                })
            }
        })
    }

    const fetchCourseProgress = async (courseId: string) => {
        try {
            const unit = getUnitForLessonId(course, Number(lessonId));
            const lesson = getLesson(unit, Number(lessonId));
            setUiLesson(normalizeLesson(lesson));
            const courseProgressResponse = await axios.get<CourseProgress>(`${BACKEND_BASE_URL}/courses/${courseId}/progress-with-answers`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            setCourseProgress(courseProgressResponse.data);
        } catch (error) {
            setError(handleFetchError(error, courseId));
        } finally {
            setLoadingCourseProgress(false);
        }

    }

    const handleFetchError = (error: unknown, courseId: string) => {
        if (axios.isAxiosError(error)) {
            console.error(`Axios Error while getting course or course progress for course with id ${courseId}:`, error);
            if (error.status === HttpStatusCode.NotFound) {
                return "course-progress-not-found";
            } else {
                return "axios-general-error";
            }
        } else {
            console.error(`Something went wrong while getting course or course progress for course with id ${courseId}:`, error);
            return "general-fetching-error";
        }
    }

    const navigateToCourseOverview = () => {

    }
    if (courseId === undefined || lessonId === undefined) {
        return <div>How did you get there?</div>
    }

    if (loadingCourseProgress) {
        return <div>Loading...</div>
    }

    if (error !== null) {
        switch (error) {
            case "lesson-not-found":
                return <div>It seems that you have problem in URL. Are you sure such lesson exists?</div>
            default:
                return <div>Oops something went wrong. Please see console logs.</div>
        }
    }

    if (courseProgress === null || course === null || uiLesson === null) {
        return <div>It should never happen though 🤔</div>
    }

    const unit = getUnitForLessonId(course, Number(lessonId));

    return (
        <>
            <h1 className="mt-4">{course.title}</h1>
            <div className="d-flex justify-content-between mt-1 mb-1">
                <h3 >Unit {unit.unitNumber}, Lesson {uiLesson.lessonNumber}</h3>
                <Link className="btn btn-shaddow min-w-120px btn-primary" to={`/courses/${courseId}`}>List Of Content</Link>
            </div>
            <div className="lesson-study-content-container p-3">
                {blocks.map((block, index) => <LessonBlockSwitch key={index} block={block} handleClozeAnswerChange={handleClozeAnswerChange} handleMultipleChoiceAnswerChange={handleMultipleChoiceAnswerChange} />)}
                <div className="container-fluid d-flex justify-content-around mb-4">
                    <button className="btn btn-shaddow min-w-120px btn-primary"><FaArrowLeft /> Previous</button>
                    <button className="btn btn-shaddow min-w-120px btn-primary"> Check</button>
                    <button className="btn btn-shaddow min-w-120px btn-primary">Next <FaArrowRight /></button>
                </div>
            </div>

        </>
    )
}

export default StudyLessonPage;