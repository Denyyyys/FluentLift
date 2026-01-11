import { useState } from "react"
import type { UiUnit } from "../../types/course"
import { IoMdArrowDropright } from "react-icons/io";
import { IoMdArrowDropdown } from "react-icons/io";
import LessonRow from "./LessonRow";
import { textByLanguage } from "../../assets/translations";
import { useLanguage } from "../../hooks/useLanguage";

interface RequireUnitRowProps {
    uiUnit: UiUnit;
}

function UnitRow({ uiUnit }: RequireUnitRowProps) {
    const [isOpened, setIsOpened] = useState(false);
    const { language } = useLanguage();

    return (
        <div className="unit-row-container rounded mb-2 p-2">
            <div className="mb-2 d-flex justify-content-between align-items-center">
                <h3>{uiUnit.title}</h3>
                <div className="d-flex align-items-center">
                    <div className="progress" style={{ width: "200px", height: "15px" }}>
                        <div
                            className="progress-bar progress-bar-striped bg-success"
                            role="progressbar"
                            style={{ "width": `${uiUnit.progressInfo.progress}%` }}
                            aria-valuenow={uiUnit.progressInfo.progress}
                            aria-valuemin={0}
                            aria-valuemax={100}
                        >
                            {uiUnit.progressInfo.progress}%
                        </div>
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
                        <h4>{uiUnit.overview}</h4>
                    </div>
                    <h3 className="mt-3 mb-1">{textByLanguage[language]["singleEnrolledCourse"]["lessonsText"]}</h3>
                    <div className="stripped-container">
                        {uiUnit.lessons.map(uiLesson => {
                            return <LessonRow key={uiLesson.id} uiLesson={uiLesson} />
                        })}
                    </div>
                </>
            }
        </div>
    )
}

export default UnitRow