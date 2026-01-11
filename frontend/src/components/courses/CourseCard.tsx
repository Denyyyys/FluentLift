import { Link } from "react-router-dom"
import type { CourseResponse } from "../../types/course"
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";
import { translateLanguageName } from "../../utils/utils";
import type { ALL_LANGUAGES_TYPE } from "../../constants";

type CourseCardProps = {
    course: CourseResponse
}

function CourseCard({ course }: CourseCardProps) {
    const { language } = useLanguage();
    return (
        <div className="col-md-4 mb-3">
            <div className="card" style={{ width: "18rem;" }}>
                <div className="card-top-color"></div>
                <div className="card-body">
                    <h4 className="card-title ">{course.title}</h4>
                    <p className="card-text">{course.description.length > 150 ? course.description.slice(0, 150) + "..." : course.description}</p>
                </div>
                <ul className="list-group list-group-flush">
                    <li className="list-group-item">{textByLanguage[language]["allCourses"]["baseLanguageText"]}: <span className="fw-bold">{translateLanguageName(language, course.baseLanguage as ALL_LANGUAGES_TYPE)} </span></li>
                    <li className="list-group-item">{textByLanguage[language]["allCourses"]["targetLanguageText"]}: <span className="fw-bold"> {translateLanguageName(language, course.targetLanguage as ALL_LANGUAGES_TYPE)} </span> </li>
                    <li className="list-group-item">{textByLanguage[language]["allCourses"]["prerequisiteLevelText"]}: {course.prerequisiteLevel} </li>
                    <li className="list-group-item">{textByLanguage[language]["allCourses"]["outcomeLevelText"]}:  {course.outcomeLevel}  </li>
                    {/* <li className="list-group-item"><b> Course goals</b></li>
                    {course.goals.map(goal => <li className="list-group-item">{goal}</li>)} */}
                </ul>
                <div className="card-body">
                    <Link className="card-link" to={`/courses/${course.id}`} > {textByLanguage[language]["allCourses"]["goToCourseActionText"]}</Link>
                </div>
            </div>
        </div>
    )
}

export default CourseCard