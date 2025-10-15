import { useNavigate, useParams } from "react-router-dom"
import type { CourseResponse, UserEnrollmentResponse } from "../../types/course";
import { useAuth } from "../../context/AuthContext";
import { countNumberOfLessons } from "../../utils/utils";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { toast } from 'react-toastify';

interface RequireCoursePreviewProps {
    course: CourseResponse;
    onEnroll: () => void;
}

function CoursePreviewPage({ course, onEnroll }: RequireCoursePreviewProps) {
    let navigate = useNavigate();

    const courseId = course.id;
    const { userId, token } = useAuth();

    const enroll = async () => {
        try {
            const enrollResponse = await axios.post<UserEnrollmentResponse>(`${BACKEND_BASE_URL}/courses/${courseId}/users`, undefined, {
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
            <h2 className="mt-2">{course.title}</h2>
            <h4 className="mt-2">{course.description}</h4>
            <h4 className="mt-4">Course Base Language: {course.baseLanguage}</h4>
            <h4 className="mt-4">Course Target Language: {course.targetLanguage}</h4>
            <h4 className="mt-4">Course Prerequisite Level: {course.prerequisiteLevel}</h4>
            <h4 className="mt-4">Course Outcome Level: {course.outcomeLevel}</h4>
            <h4 className="mt-4">Total Number Of Lessons: {countNumberOfLessons(course)}</h4>

            {userId === course.creator.id ?
                <button className="btn btn-warning" onClick={() => navigate(`/courses/${courseId}/edit`)}>Edit Course</button> : <button className="btn btn-success" onClick={async () => {
                    await enroll();
                }}>Enroll</button>
            }

        </div>
    )
}

export default CoursePreviewPage