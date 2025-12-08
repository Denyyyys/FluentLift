import { useNavigate, } from "react-router-dom"
import type { UserEnrollmentResponse } from "../../types/course";
import { useAuth } from "../../context/AuthContext";
import { countNumberOfLessons, useCourse } from "../../utils/utils";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { toast } from 'react-toastify';

interface RequireCoursePreviewProps {
    onEnroll: () => void;
}

function CoursePreviewPage({ onEnroll }: RequireCoursePreviewProps) {
    let navigate = useNavigate();

    const { userId, token } = useAuth();

    const { uiCourse } = useCourse();

    const enroll = async () => {
        try {
            const enrollResponse = await axios.post<UserEnrollmentResponse>(`${BACKEND_BASE_URL}/courses/${uiCourse.id}/users`, undefined, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (enrollResponse.data.enrolledStatus === "enrolled") {
                toast.success("You enrolled for a course!")
                onEnroll();
            }

        }
        catch (error) {
            console.error(error);
            toast.error("Couldn't enroll for course. Check logs")
        }
    }
    return (
        <div>
            <h2 className="mt-2">{uiCourse.title}</h2>
            <h4 className="mt-2">{uiCourse.description}</h4>
            <h4 className="mt-4">Course Base Language: {uiCourse.baseLanguage}</h4>
            <h4 className="mt-4">Course Target Language: {uiCourse.targetLanguage}</h4>
            <h4 className="mt-4">Course Prerequisite Level: {uiCourse.prerequisiteLevel}</h4>
            <h4 className="mt-4">Course Outcome Level: {uiCourse.outcomeLevel}</h4>
            <h4 className="mt-4">Total Number Of Lessons: {countNumberOfLessons(uiCourse)}</h4>

            {userId === uiCourse.creator.id ?
                <button className="btn btn-warning" onClick={() => navigate(`/courses/${uiCourse.id}/edit`)}>Edit Course</button> : <button className="btn btn-success" onClick={async () => {
                    await enroll();
                }}>Enroll</button>
            }

        </div>
    )
}

export default CoursePreviewPage