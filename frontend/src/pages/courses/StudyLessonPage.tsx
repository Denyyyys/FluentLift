import { Link, useNavigate, useParams } from "react-router-dom"
import { BlockType, type ClozeBlockUserAnswer, type ClozeBlockUserAnswerRequestDto, type CourseAnswers, type Lesson, type LessonAnswers, type LessonResponse, type UiClozeBlock, type UiClozeBlockAnswer, type UiLesson, type UiMultipleChoiceBlock, type UiMultipleChoiceOption, type UiUnit, type UnitAnswers, type UserAnswersRequestDto } from "../../types/course";
import { useEffect, useMemo, useRef, useState, type JSX, type ReactElement } from "react";
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
import type { Synset } from "../../types/synset";
import SynsetTooltip from "../../components/courses/SynsetTooltip";
import { textByLanguage } from "../../assets/translations";
import { useLanguage } from "../../hooks/useLanguage";

function StudyLessonPage() {
    const { courseId, lessonId } = useParams();
    const { uiCourse, updateLesson } = useCourse();
    const { language } = useLanguage();

    if (!lessonId || !courseId) {
        return <div>How did you get there?</div>
    }

    const [localUiLesson, setLocalUiLesson] = useState<UiLesson | null>(null);
    const [uiUnit, setUiUnit] = useState<UiUnit | null>(null);
    const [loadingLesson, setLoadingLesson] = useState(true);
    const [savingAnswers, setSavingAnswers] = useState(false);

    const [selectedText, setSelectedText] = useState<string | null>(null);
    const [selectedTextPosition, setSelectedTextPosition] = useState<{ x: number; y: number } | null>(null);
    const [synsetSelectedText, setSynsetSelectedText] = useState<Synset | null>(null);

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

    const handleLessonContainerMouseUp = async (e: React.MouseEvent<HTMLDivElement>) => {
        try {

            const selection = window.getSelection();
            if (selection && selection.toString().trim() !== "") {
                const text = selection.toString();
                setSelectedText(text);

                const synset = await axios.get<Synset>(`${BACKEND_BASE_URL}/synsets/searchByAny?word=${text}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                setSynsetSelectedText(synset.data);
                console.log(synset.data);

                const range = selection.getRangeAt(0);
                const rect = range.getBoundingClientRect();

                // set position a bit below selection
                setSelectedTextPosition({
                    x: rect.left + window.scrollX,
                    y: rect.bottom + window.scrollY + 5, // 5px below
                });
            } else {
                setSelectedText(null);
                setSelectedTextPosition(null);
            }
        } catch (e) {
            console.error("error while fetching synset");
            console.error(e);
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
                <h3 >{textByLanguage[language]["singleEnrolledCourse"]["unitText"]} {uiUnit.unitNumber}, {textByLanguage[language]["singleEnrolledCourse"]["lessonText"]} {localUiLesson.lessonNumber}</h3>
                <button className="btn btn-shaddow min-w-120px btn-primary" onClick={async () => {
                    await saveAnswers()
                    navigate(`/courses/${courseId}`);
                }}>{textByLanguage[language]["singleEnrolledCourse"]["listOfContentText"]}</button>
            </div>
            <div className="lesson-study-content-container p-3" onMouseUp={async (e) => await handleLessonContainerMouseUp(e)}>
                {selectedText && selectedTextPosition && (
                    // <div
                    //     style={{
                    //         position: "absolute",
                    //         top: selectedTextPosition.y,
                    //         left: selectedTextPosition.x,
                    //         backgroundColor: "var(--clr-primary-300)",
                    //         border: "1px solid black",
                    //         padding: "5px 10px",
                    //         borderRadius: "4px",
                    //         boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
                    //         zIndex: 1000,
                    //     }}
                    // >
                    //     {selectedText}
                    // </div>
                    <SynsetTooltip selectedText={selectedText} synset={synsetSelectedText} sourceLang={uiCourse.baseLanguage} targetLang={uiCourse.targetLanguage} selectedTextPosition={selectedTextPosition} />
                )}
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
                        <span> {textByLanguage[language]["pagination"]["previousText"]} </span>
                    </button>
                    <button
                        className="btn btn-shaddow min-w-120px btn-primary"
                        onClick={() => setIsChecked(prev => !prev)}
                    >
                        {isChecked ? textByLanguage[language]["singleEnrolledCourse"]["editText"] : textByLanguage[language]["singleEnrolledCourse"]["checkButtonText"]}
                    </button>
                    <button
                        className="btn btn-shaddow min-w-120px btn-primary d-flex align-items-center justify-content-between gap-1"
                        onClick={() => setShowCorrect(prev => !prev)}>
                        {
                            showCorrect
                                ? <><LuEyeClosed /> <span>{textByLanguage[language]["singleEnrolledCourse"]["hideAnswersButtonText"]}</span>  </>
                                : <><LuEye /> <span>{textByLanguage[language]["singleEnrolledCourse"]["showAnswersButtonText"]}</span></>
                        }
                    </button>
                    <button
                        className="btn btn-shaddow btn-primary d-flex align-items-center justify-content-between gap-1"
                        disabled={nextLesson === undefined}
                        onClick={async () => await saveAnswersAndGoToLesson(localUiLesson.lessonNumber + 1)}
                    >
                        <span>{textByLanguage[language]["pagination"]["nextText"]}</span>
                        <FaArrowRight />
                    </button>
                </div>
            </div>

        </>
    )
}

export default StudyLessonPage;