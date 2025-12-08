import UnitRow from "../../components/courses/UnitRow";
import { useCourse } from "../../utils/utils";

function CourseContentPage() {
    const { uiCourse } = useCourse();
    let status: string;
    if (uiCourse.progressInfo.progress === 0) {
        status = "Not Started";
    } else if (uiCourse.progressInfo.progress === 100) {
        status = "Finished";
    } else {
        status = "In Progress"
    }

    console.log("course ui");
    console.log(uiCourse);

    return (
        <div>
            <div className="mt-4 d-flex justify-content-between align-items-center">
                <h2 >{uiCourse.title}</h2>
                <h2>Status: {status}</h2>
            </div>
            <h4 className="mt-2">{uiCourse.description}</h4>
            <h3 className="mt-2">Units</h3>
            {uiCourse.units.map(uiUnit => {
                return <UnitRow key={uiUnit.id} uiUnit={uiUnit} />
            })}
        </div>
    )
}
export default CourseContentPage;