import type { CourseUnit, Lesson as LessonType } from "../../../../types/course";
import { IoMdArrowDropright } from "react-icons/io";
import { IoMdArrowDropdown } from "react-icons/io";

import { FaTrashAlt } from "react-icons/fa";
import { useState } from "react";
import { v4 as uuid } from "uuid";

import LessonComponent from "./Lesson";

type UnitProps = {
    unit: CourseUnit;
    updateUnit: (updated: CourseUnit) => void;
    openedUnitId: string | null;
    setOpenedUnitId: React.Dispatch<React.SetStateAction<string | null>>;
    unitNumber: number;
    removeCurrentUnit: () => void;
    openedLessonId: string | null;
    setOpenedLessonId: React.Dispatch<React.SetStateAction<string | null>>;

}


function Unit({ unit, updateUnit, openedUnitId, setOpenedUnitId, unitNumber, removeCurrentUnit, openedLessonId, setOpenedLessonId }: UnitProps) {
    const [totalLessonNumber, setTotalLessonNumber] = useState(0);
    const [overview, setOverview] = useState(unit.overview);
    const [title, setTitle] = useState(unit.title);

    const updateMainUnit = () => {
        const updatedUnit: CourseUnit = {
            ...unit,
            overview,
            title,
        };
        updateUnit(updatedUnit);
    };

    const addLesson = () => {
        const newLesson = { id: uuid(), title: "", blocks: [], lessonNumber: totalLessonNumber + 1 } satisfies LessonType;
        const updatedUnit = { ...unit, lessons: [...unit.lessons, newLesson] };
        updateUnit(updatedUnit);
        setTotalLessonNumber(prev => prev += 1)
    };

    const updateLesson = (updatedLesson: LessonType) => {
        const updatedUnit = {
            ...unit,
            lessons: unit.lessons.map((l) => (l.id === updatedLesson.id ? updatedLesson : l)),
        };
        updateUnit(updatedUnit);
    };

    const removeLesson = (id: string) => {
        const updatedUnit = {
            ...unit,
            lessons: unit.lessons.filter((l) => l.id !== id),
        };
        updateUnit(updatedUnit);
    };

    if (unit.id !== openedUnitId) {
        return (
            <div>
                <div className="d-flex justify-content-between p-2 align-items-center unit-item-list rounded mb-2">

                    <div className="d-flex align-items-center gap-1">
                        <IoMdArrowDropright size={30} className="clickable" onClick={() => setOpenedUnitId(unit.id)} />
                        <h4> Unit {unitNumber} {unit.title && <> - {unit.title}</>} </h4>
                    </div>
                    <FaTrashAlt size={24} className="clickable" onClick={removeCurrentUnit} />
                </div>
            </div>
        )
    }

    return (
        <div className="p-2 rounded mb-2">

            <div className="d-flex justify-content-between align-items-center unit-item-list rounded mb-2">
                <div className="d-flex align-items-center gap-1">
                    <IoMdArrowDropdown size={30} className="clickable" onClick={() => setOpenedUnitId(null)} />
                    <h4> Unit {unitNumber} {unit.title && <> - {unit.title}</>} </h4>
                </div>
                <FaTrashAlt size={24} className="clickable" onClick={removeCurrentUnit} />

            </div>
            <div className="row">
                <div className="col">
                    <h4 className="mb-2">Title</h4>
                    <input type="text" className="form-control" placeholder="First name" value={title} onChange={(e) => setTitle(e.target.value)} onBlur={updateMainUnit} />
                </div>
                <div className="col">
                    <h4 className="mb-2">Overview</h4>
                    <textarea className="form-control" placeholder="Add overview for this unit" style={{ height: "100px" }} value={overview} onChange={(e) => setOverview(e.target.value)} onBlur={updateMainUnit}></textarea>
                </div>
            </div>
            <h3>Lessons</h3>
            <div className="lessons-container">

                {unit.lessons.map((lesson, index) => <LessonComponent key={lesson.id} lessonNumber={index + 1} lesson={lesson} updateLesson={updateLesson} removeCurrentLesson={() => removeLesson(lesson.id)} openedLessonId={openedLessonId} setOpenedLessonId={setOpenedLessonId} />)}
            </div>
            <button onClick={addLesson} className="btn btn-shaddow btn-primary fw-semibold" style={{ minWidth: "120px" }}>Add Lesson</button>

        </div>
    )
}

export default Unit