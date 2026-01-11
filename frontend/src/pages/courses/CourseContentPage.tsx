import { textByLanguage } from "../../assets/translations";
import UnitRow from "../../components/courses/UnitRow";
import { useLanguage } from "../../hooks/useLanguage";
import { useCourse } from "../../utils/utils";

function CourseContentPage() {
    const { language } = useLanguage();

    const { uiCourse } = useCourse();
    let status: string;
    if (uiCourse.progressInfo.progress === 0) {
        status = textByLanguage[language]["singleEnrolledCourse"]["courseStatusNotStartedText"];
    } else if (uiCourse.progressInfo.progress === 100) {
        status = textByLanguage[language]["singleEnrolledCourse"]["courseStatusNotFinishedText"];
    } else {
        status = textByLanguage[language]["singleEnrolledCourse"]["courseStatusNotInProgressText"]
    }

    console.log("course ui");
    console.log(uiCourse);

    return (
        <div>
            <div className="mt-4 d-flex justify-content-between align-items-center">
                <h2 className="fw-bold">{uiCourse.title}</h2>
                <h2>{textByLanguage[language]["singleEnrolledCourse"]["statusText"]}: {status}</h2>
            </div>
            <h4 className="mt-2">{uiCourse.description}</h4>
            <h3 className="mt-2 mb-2">{textByLanguage[language]["singleEnrolledCourse"]["unitsText"]}</h3>
            {uiCourse.units.map(uiUnit => {
                return <UnitRow key={uiUnit.id} uiUnit={uiUnit} />
            })}
        </div>
    )
}
export default CourseContentPage;