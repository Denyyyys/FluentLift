import { useNavigate, useParams } from "react-router-dom"
import type { CourseProgress, CourseResponse } from "../../types/course";
import { useAuth } from "../../context/AuthContext";
import UnitRow from "../../components/courses/UnitRow";
import { useEffect, useState } from "react";
import { BACKEND_BASE_URL } from "../../constants";
import axios, { HttpStatusCode } from "axios";
import { getUnitProgress } from "../../utils/utils";

interface RequireCourseContentProps {
    course: CourseResponse;
}

function CourseContentPage({ course }: RequireCourseContentProps) {
    const { token } = useAuth();

    const { courseId } = useParams<{ courseId: string }>();
    const [courseProgress, setCourseProgress] = useState<CourseProgress | null>();
    const [loadingCourse, setLoadingCourse] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const handleCourseProgressError = (error: unknown, courseId: string) => {
        if (axios.isAxiosError(error)) {
            console.error(`Axios Error while getting course progress for course with id ${courseId}:`, error);
            if (error.status === HttpStatusCode.NotFound) {
                return "course-progress-not-found";
            } else {
                return "axios-general-error";
            }
        } else {
            console.error(`Something went wrong while getting course progress for course with id ${courseId}:`, error);
            return "general-fetching-error";
        }
    }

    const fetchCourseProgress = async (courseId: string) => {
        try {
            setLoadingCourse(true);
            setError(null);
            setCourseProgress(null);
            const courseProgressResponse = await axios.get<CourseProgress>(`${BACKEND_BASE_URL}/courses/${courseId}/progress-with-answers`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            console.log("COurse progress: ");
            console.log(courseProgressResponse);

            setCourseProgress(courseProgressResponse.data);
        } catch (error) {
            setError(handleCourseProgressError(error, courseId));
        } finally {
            setLoadingCourse(false);
        }
    }
    useEffect(() => {
        if (courseId) {
            fetchCourseProgress(courseId);
        }

    }, [courseId])

    if (loadingCourse) {
        return <div>Loading...</div>
    }

    if (error !== null) {
        switch (error) {
            case "course-not-found":
                return <div>
                    <h2>Ooops it seems like course which you are looking for doesn't exist or was deleted or archived.</h2>
                </div>
            default:
                return <div>
                    <h2>Some unexpected error happened. See console logs for more info.</h2>
                </div>
        }
    }

    if (!courseProgress) {
        return <div>It should never happen actually :/</div>
    }

    return (
        <div>
            <div className="mt-2 d-flex justify-content-between align-items-center">
                <h2 >{course.title}</h2>
                <div className="progress" style={{ width: "200px", height: "20px" }}>
                    <div className="progress-bar progress-bar-striped bg-success" role="progressbar" style={{ "width": `${courseProgress.progress}%` }} aria-valuenow={courseProgress.progress} aria-valuemin={0} aria-valuemax={100}></div>
                </div>
            </div>
            <h4 className="mt-2">{course.description}</h4>
            <h3 className="mt-2">Units</h3>
            {course.units.map(unit => {
                const unitProgress = getUnitProgress(unit.id, courseProgress.unitProgresses);
                return unitProgress ? <UnitRow key={unit.id} courseId={course.id} unit={unit} unitProgress={unitProgress} /> : <h1>It should never happen</h1>
            })}
        </div>
    )
}
export default CourseContentPage;