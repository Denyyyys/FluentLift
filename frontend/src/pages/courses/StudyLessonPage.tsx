import { Link, useNavigate, useParams } from "react-router-dom"
import { BlockType, type ClozeBlockUserAnswer, type ClozeBlockUserAnswerRequestDto, type CourseAnswers, type Lesson, type LessonAnswers, type LessonResponse, type UiClozeBlock, type UiClozeBlockAnswer, type UiLesson, type UiMultipleChoiceBlock, type UiMultipleChoiceOption, type UiUnit, type UnitAnswers, type UserAnswersRequestDto } from "../../types/course";
import { useEffect, useMemo, useState, type JSX, type ReactElement } from "react";
import axios, { HttpStatusCode } from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { useAuth } from "../../context/AuthContext";
import { getNextLesson, getPreviousLesson, getSortedBlocksForUiLesson, useCourse } from "../../utils/utils";
import LessonBlockSwitch from "../../components/courses/LessonBlockSwitch";
import { FaArrowLeft } from "react-icons/fa";
import { FaArrowRight } from "react-icons/fa";
import { LuEye } from "react-icons/lu";
import { LuEyeClosed } from "react-icons/lu";
import { NotFoundError, NotFoundName } from "../../errors";
import { toast } from "react-toastify";
import ErrorWrapper from "../../components/common/ErrorWrapper";
import LoadingSpinner from "../../components/common/LoadingSpinner";
import UnreachableState from "../../components/common/UnreachableState";

function StudyLessonPage() {
    const { courseId, lessonId } = useParams();
    const { uiCourse, updateLesson } = useCourse();
    if (!lessonId || !courseId) {
        return <div>How did you get there?</div>
    }

    const [localUiLesson, setLocalUiLesson] = useState<UiLesson | null>(null);
    const [uiUnit, setUiUnit] = useState<UiUnit | null>(null);
    const [loadingLesson, setLoadingLesson] = useState(true);
    const [savingAnswers, setSavingAnswers] = useState(false);

    const [error, setError] = useState<unknown | null>(null);
    const [showCorrect, setShowCorrect] = useState<boolean>(false);
    const [isChecked, setIsChecked] = useState<boolean>(false);
    const { token } = useAuth();
    const navigate = useNavigate();

    // TODO check if works when uiLesson is removed from list of dependencies
    const blocks = useMemo(() => (localUiLesson ? getSortedBlocksForUiLesson(localUiLesson) : []), [localUiLesson]);

    const previousLesson = useMemo(() => (localUiLesson ? getPreviousLesson(uiCourse, localUiLesson.lessonNumber) : undefined), [localUiLesson]);

    const nextLesson = useMemo(() => (localUiLesson ? getNextLesson(uiCourse, localUiLesson.lessonNumber) : undefined), [localUiLesson]);

    const loadLessonFromCourse = () => {
        let lessonIsFound = false;
        uiCourse.units.forEach(unit => {
            let foundLesson = unit.lessons.find(lesson => lesson.id === Number(lessonId))
            if (foundLesson) {
                lessonIsFound = true;
                setLocalUiLesson(foundLesson)
                setUiUnit(unit);
                return;
            }
        })

        if (!lessonIsFound) {
            throw new NotFoundError({ name: NotFoundName.LessonNotFound, message: `Lesson with id ${lessonId} was not found`, cause: null })
        }
    }

    const handleError = (error: unknown) => {
        console.error(error)
        if (error instanceof NotFoundError) {
            toast.error(error.message)
        } else {
            toast.error("Unexpected error happened")
        }
    }

    const handleClozeAnswerChange = (blockId: number, answerId: number, userInput: string) => {
        setLocalUiLesson(prev => {
            if (!prev) {
                return prev
            }

            return {
                ...prev,
                clozeBlocks: prev.clozeBlocks.map(block => {
                    if (block.id !== blockId) {
                        return block;
                    }
                    return {
                        ...block,
                        answers: block.answers.map(answer => {
                            if (answer.id !== answerId) {
                                return answer
                            }
                            return {
                                ...answer,
                                userAnswer: userInput
                            }
                        })
                    }
                })
            }
        })
    }

    const handleMultipleChoiceAnswerChange = (mcBlockId: number, mcOptionId: number, userSelected: boolean) => {
        setLocalUiLesson(prev => {
            if (!prev) {
                return prev;
            }

            return {
                ...prev,
                multipleChoiceBlocks: prev.multipleChoiceBlocks.map(block => {
                    if (block.id !== mcBlockId) {
                        return block
                    }

                    return {
                        ...block,
                        choiceOptions: block.choiceOptions.map(option => {
                            if (option.id !== mcOptionId) {
                                return option
                            }

                            return { ...option, isSelected: userSelected }
                        })
                    }
                })
            }
        })
    }

    const normalizeLessonToUserAnswersRequestDto = (lesson: UiLesson) => {
        let userAnswersRequest = {
            clozeAnswers: [] as ClozeBlockUserAnswerRequestDto[],
            multipleChoiceUserSelectedOptionIds: [] as number[]
        } satisfies UserAnswersRequestDto;

        userAnswersRequest.clozeAnswers = lesson.clozeBlocks.flatMap(clozeBlock => {
            return clozeBlock.answers.map(answer => ({ clozeBlockAnswerId: answer.id, userInput: answer.userAnswer } satisfies ClozeBlockUserAnswerRequestDto))
        })

        userAnswersRequest.multipleChoiceUserSelectedOptionIds = lesson.multipleChoiceBlocks.flatMap(mcBlock => mcBlock.choiceOptions
            .filter(option => option.isSelected)
            .map(selectedOption => selectedOption.id)
        );

        return userAnswersRequest;
    }

    const saveAnswers = async () => {
        console.log("sdougsdgh8");

        if (!localUiLesson) {
            setError(new Error("localUiLesson is null before trying to save answers"))
            return
        }
        const normalizedRequest = normalizeLessonToUserAnswersRequestDto(localUiLesson);

        await axios.put<string>(`${BACKEND_BASE_URL}/courses/${uiCourse.id}/lessons/${localUiLesson.id}/user-answers`, normalizedRequest, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        updateLesson(localUiLesson);
    }

    const saveAnswersAndGoToLesson = async (lessonToDisplayByNumber: number) => {
        if (!localUiLesson) {
            setError(new Error("localUiLesson is null before trying to save answers"))
            return
        }

        await saveAnswers();

        try {
            navigate(`/courses/${uiCourse.id}/lessons/${lessonToDisplayByNumber}`)
        } catch (error) {
            setError(error);
        }
    }

    useEffect(() => {
        setLoadingLesson(true);
        try {
            loadLessonFromCourse()
        } catch (error) {
            setError(error);
        } finally {
            setLoadingLesson(false);
        }

    }, [uiCourse, courseId, lessonId])

    useEffect(() => {
        if (error !== null) {
            handleError(error)
        }
    }, [error])

    if (loadingLesson) {
        return <LoadingSpinner />
    }

    if (error) {
        let content: string | undefined;
        if (error instanceof NotFoundError) {
            content = error.message;
        }

        return (
            <ErrorWrapper content={content} />
        )
    }

    if (uiUnit === null || localUiLesson === null) {
        return <UnreachableState />
    }

    return (
        <>
            <h1 className="mt-4">{uiCourse.title}</h1>
            <div className="d-flex justify-content-between mt-1 mb-1">
                <h3 >Unit {uiUnit.unitNumber}, Lesson {localUiLesson.lessonNumber}</h3>
                <button className="btn btn-shaddow min-w-120px btn-primary" onClick={async () => {
                    await saveAnswers()
                    navigate(`/courses/${courseId}`);
                }}>List Of Content</button>
            </div>
            <div className="lesson-study-content-container p-3">
                {blocks.map((block, index) => (
                    <LessonBlockSwitch
                        key={index}
                        block={block}
                        isChecked={isChecked}
                        showCorrect={showCorrect}
                        handleClozeAnswerChange={handleClozeAnswerChange}
                        handleMultipleChoiceAnswerChange={handleMultipleChoiceAnswerChange}
                    />))}
                <div className="container-fluid d-flex justify-content-around mb-4">
                    <button
                        className="btn btn-shaddow  btn-primary d-flex align-items-center justify-content-between gap-1"
                        disabled={previousLesson === undefined}
                        onClick={async () => {
                            await saveAnswersAndGoToLesson(localUiLesson.lessonNumber - 1);
                        }}
                    >
                        <FaArrowLeft />
                        <span> Previous </span>
                    </button>
                    <button
                        className="btn btn-shaddow min-w-120px btn-primary"
                        onClick={() => setIsChecked(prev => !prev)}
                    >
                        {isChecked ? "Edit" : "Check"}
                    </button>
                    <button
                        className="btn btn-shaddow min-w-120px btn-primary d-flex align-items-center justify-content-between gap-1"
                        onClick={() => setShowCorrect(prev => !prev)}>
                        {
                            showCorrect
                                ? <><LuEyeClosed /> <span>Hide answers</span>  </>
                                : <><LuEye /> <span>Show answers</span></>
                        }
                    </button>
                    <button
                        className="btn btn-shaddow btn-primary d-flex align-items-center justify-content-between gap-1"
                        disabled={nextLesson === undefined}
                        onClick={async () => await saveAnswersAndGoToLesson(localUiLesson.lessonNumber + 1)}
                    >
                        <span>Next</span>
                        <FaArrowRight />
                    </button>
                </div>
            </div>

        </>
    )
}

export default StudyLessonPage;