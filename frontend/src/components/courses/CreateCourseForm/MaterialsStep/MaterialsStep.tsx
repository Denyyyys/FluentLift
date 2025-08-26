import { v4 as uuid } from "uuid";
import { BlockType, type BigHeadingBlock, type ClozeBlock, type Course, type CourseUnit, type Lesson, type LessonBlock, type MultipleChoiceBlock, type ParagraphBlock, type SmallHeadingBlock } from "../../../../types/course";
import { LuHeading1 } from "react-icons/lu";
import { LuHeading2 } from "react-icons/lu";
import { BsParagraph } from "react-icons/bs";
import { RiInputField } from "react-icons/ri";
import { RiListCheck3 } from "react-icons/ri";
import Unit from "./Unit";

type CourseMaterialsProps = {
    course: Course;
    setCourse: React.Dispatch<React.SetStateAction<Course>>;
    openedUnitId: string | null;
    setOpenedUnitId: React.Dispatch<React.SetStateAction<string | null>>;
    openedLessonId: string | null;
    setOpenedLessonId: React.Dispatch<React.SetStateAction<string | null>>;
}

function MaterialsStep({ course, setCourse, openedUnitId, setOpenedUnitId, openedLessonId, setOpenedLessonId }: CourseMaterialsProps) {

    const updateUnit = (updatedUnit: CourseUnit) => {
        setCourse({ ...course, units: course.units.map(u => u.id === updatedUnit.id ? updatedUnit : u) });
    };

    const addUnit = () => {
        const newUnitId = uuid();
        setCourse(prev => ({ ...prev, units: [...prev.units, { id: newUnitId, lessons: [], overview: "", title: "" }] }))
        setOpenedUnitId(newUnitId);
    }

    const removeUnit = (id: string) => {
        if (openedUnitId === id) {
            setOpenedUnitId(null);
        }

        setCourse(prev => ({ ...prev, units: prev.units.filter(unit => unit.id !== id) }))
    }

    const addBlock = (newBlock: LessonBlock) => {
        const openedUnit = course.units.find(unit => unit.id === openedUnitId);
        if (openedUnit) {
            const openedLesson = openedUnit.lessons.find(lesson => lesson.id === openedLessonId);
            if (openedLesson) {
                const updatedLesson: Lesson = {
                    ...openedLesson,
                    blocks: [...openedLesson.blocks, newBlock],
                };

                const updatedUnit: CourseUnit = {
                    ...openedUnit,
                    lessons: openedUnit.lessons.map(lesson =>
                        lesson.id === openedLessonId ? updatedLesson : lesson
                    ),
                };

                updateUnit(updatedUnit);
            }
        }
    }

    return (
        <div className="create-course-material-container row">
            <div className="lesson-elements-container container-md col-3">
                <h3 className="mb-4">Lesson Elements</h3>
                <h4 className="mb-2">Information</h4>
                <div className="d-flex gap-3 flex-wrap mb-4">
                    <button className="col btn btn-secondary flex-fill" onClick={() => addBlock({
                        id: uuid(),
                        type: BlockType.Text,
                        textType: BlockType.BigHeading,
                        text: ""
                    } satisfies BigHeadingBlock)}><LuHeading1 /> Big Heading</button>
                    <button className="col btn btn-secondary flex-fill" onClick={() => addBlock({
                        id: uuid(),
                        type: BlockType.Text,
                        textType: BlockType.SmallHeading,
                        text: ""
                    } satisfies SmallHeadingBlock)}><LuHeading2 /> Small Heading</button>
                    <button className="col btn btn-secondary flex-fill" onClick={() => addBlock({
                        id: uuid(),
                        type: BlockType.Text,
                        textType: BlockType.ParagraphBlock,
                        text: ""
                    } satisfies ParagraphBlock)}><BsParagraph /> Paragraph</button>
                </div>
                <h4 className="mb-2">Tasks</h4>
                <div className="d-flex gap-3 flex-wrap">
                    <button className="btn btn-secondary flex-fill" onClick={() => addBlock({
                        id: uuid(),
                        answers: [],
                        question: "",
                        template: "",
                        type: BlockType.Cloze
                    } satisfies ClozeBlock)}><RiInputField /> Cloze</button>
                    {/* <button className="btn btn-secondary flex-fill"><FaExpandArrowsAlt /> Matching</button> */}
                    <button className="btn btn-secondary flex-fill" onClick={() => addBlock({
                        id: uuid(),
                        question: "",
                        choices: [],
                        type: BlockType.MultipleChoice
                    } satisfies MultipleChoiceBlock)}> <RiListCheck3 /> Multiple Choice</button>
                </div>
            </div>
            <div className="units-container col">
                <h3 className="mb-4">Units</h3>

                {course.units.map((unit, index) => <Unit key={unit.id} unitNumber={index + 1} unit={unit} updateUnit={updateUnit} openedUnitId={openedUnitId} setOpenedUnitId={setOpenedUnitId} removeCurrentUnit={() => removeUnit(unit.id)} openedLessonId={openedLessonId} setOpenedLessonId={setOpenedLessonId} />)}

                <button onClick={addUnit} className="btn btn-shaddow btn-primary fw-semibold" style={{ minWidth: "120px" }}>Add Unit</button>
                <button onClick={() => console.log(course.units)} className="btn btn-shaddow btn-danger fw-semibold" style={{ minWidth: "120px" }}>Log units</button>
            </div>


        </div>
    )
}

export default MaterialsStep