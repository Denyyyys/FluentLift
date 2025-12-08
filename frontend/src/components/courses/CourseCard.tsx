import { Link } from "react-router-dom"
import type { CourseResponse } from "../../types/course"

type CourseCardProps = {
    course: CourseResponse
}

function CourseCard({ course }: CourseCardProps) {
    return (
        <div className="col-md-4 mb-3">
            <div className="card" style={{ width: "18rem;" }}>
                <div className="card-top-color"></div>
                <div className="card-body">
                    <h4 className="card-title ">{course.title}</h4>
                    <p className="card-text">{course.description.length > 150 ? course.description.slice(0, 150) + "..." : course.description}</p>
                </div>
                <ul className="list-group list-group-flush">
                    <li className="list-group-item">Base Language: <span className="fw-bold">{course.baseLanguage} </span></li>
                    <li className="list-group-item">Target Language: <span className="fw-bold"> {course.targetLanguage} </span> </li>
                    <li className="list-group-item">Prerequisite Level: {course.prerequisiteLevel} </li>
                    <li className="list-group-item">Outcome Level:  {course.outcomeLevel}  </li>
                    {/* <li className="list-group-item">Course goals:</li>
                    {course.goals.map(goal => <li className="list-group-item">{goal}</li>)} */}
                </ul>
                <div className="card-body">
                    <Link className="card-link" to={`/courses/${course.id}`} > Start Course!</Link>
                </div>
            </div>
        </div>
    )
}

export default CourseCard