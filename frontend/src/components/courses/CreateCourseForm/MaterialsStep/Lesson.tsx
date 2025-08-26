import { IoMdArrowDropdown, IoMdArrowDropright } from "react-icons/io";
import type { LessonBlock, Lesson as LessonType } from "../../../../types/course";
import { FaTrashAlt } from "react-icons/fa";
import LessonBlockEditor from "./BlockEdotors/Lesson";

type LessonProps = {
    lessonNumber: number;
    lesson: LessonType;
    updateLesson: (updated: LessonType) => void;
    removeCurrentLesson: () => void;
    openedLessonId: string | null;
    setOpenedLessonId: React.Dispatch<React.SetStateAction<string | null>>;

}
function Lesson({ lesson, lessonNumber, openedLessonId, setOpenedLessonId, removeCurrentLesson, updateLesson }: LessonProps) {
    const updateBlock = (updated: LessonBlock) => {
        updateLesson({
            ...lesson,
            blocks: lesson.blocks.map((b) => (b.id === updated.id ? updated : b)),
        });
    };
    const removeBlock = (id: string) => {
        const updatedLesson = {
            ...lesson,
            blocks: lesson.blocks.filter((b) => b.id !== id),
        } satisfies LessonType;

        updateLesson(updatedLesson);
    };

    if (lesson.id !== openedLessonId) {
        return (
            <div>
                <div className="d-flex justify-content-between p-2 align-items-center lesson-item-list rounded mb-2">

                    <div className="d-flex align-items-center gap-1">
                        <IoMdArrowDropright size={30} className="clickable" onClick={() => setOpenedLessonId(lesson.id)} />
                        <h4> Lesson {lessonNumber} {lesson.title && <> - {lesson.title}</>} </h4>
                    </div>
                    <FaTrashAlt size={24} className="clickable" onClick={removeCurrentLesson} />
                </div>
            </div>
        )
    }
    return (
        <div className="p-2 rounded mb-2">

            <div className="d-flex justify-content-between align-items-center unit-item-list rounded mb-2">
                <div className="d-flex align-items-center gap-1">
                    <IoMdArrowDropdown size={30} className="clickable" onClick={() => setOpenedLessonId(null)} />
                    <h4> Lesson {lessonNumber} {lesson.title && <> - {lesson.title}</>} </h4>
                </div>
                <FaTrashAlt size={24} className="clickable" onClick={removeCurrentLesson} />

            </div>
            <h3 className="mb-2">Blocks</h3>
            {lesson.blocks.map((block, index) => <LessonBlockEditor key={block.id} blockNumber={index + 1} block={block} updateBlock={updateBlock} removeCurrentBlock={() => removeBlock(block.id)} />)}
        </div>
    )
}

export default Lesson