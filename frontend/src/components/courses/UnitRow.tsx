import { useState } from "react"
import type { CourseUnitResponse, UnitProgress } from "../../types/course"
import { IoMdArrowDropright } from "react-icons/io";
import { IoMdArrowDropdown } from "react-icons/io";
import LessonRow from "./LessonRow";
import { getLessonProgress } from "../../utils/utils";

interface RequireUnitRowProps {
    unit: CourseUnitResponse;
    unitProgress: UnitProgress;
    courseId: number;
}

function UnitRow({ unit, unitProgress, courseId }: RequireUnitRowProps) {
    const [isOpened, setIsOpened] = useState(false);
    return (
        <div className="unit-row-container rounded mb-2 p-2">
            <div className="mb-2 d-flex justify-content-between align-items-center">
                <h3>{unit.title}</h3>
                <div className="d-flex align-items-center">
                    <div className="progress" style={{ width: "200px", height: "15px" }}>
                        <div className="progress-bar progress-bar-striped bg-success" role="progressbar" style={{ "width": `${unitProgress.progress}%` }} aria-valuenow={unitProgress.progress} aria-valuemin={0} aria-valuemax={100}></div>
                    </div>
                    {isOpened ?
                        <IoMdArrowDropright size={30} className="clickable" onClick={() => setIsOpened(false)} /> :
                        <IoMdArrowDropdown size={30} className="clickable" onClick={() => setIsOpened(true)} />
                    }

                </div>
            </div>
            {isOpened &&
                <>
                    <div className="unit-row-body">
                        <h4>{unit.overview}</h4>
                    </div>
                    <h3 className="mt-3 mb-1">Lessons</h3>
                    <div className="stripped-container">
                        {unit.lessons.map(lesson => {
                            const lessonProgress = getLessonProgress(lesson.id, unitProgress.lessonProgresses);
                            return lessonProgress ? <LessonRow key={lesson.id} courseId={courseId} lesson={lesson} lessonProgress={lessonProgress} /> : <h1>It should never happen</h1>
                        })}
                    </div>
                </>
            }
        </div>
    )
}

export default UnitRow