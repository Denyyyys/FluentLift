import { useEffect, useState } from "react";
import axios, { HttpStatusCode } from "axios";
import type { CourseResponse, UserEnrollmentResponse } from "../types/course";
import { BACKEND_BASE_URL } from "../constants";
import { useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import CoursePreviewPage from "../pages/courses/CoursePreviewPage";
import CourseContentPage from "../pages/courses/CourseContentPage";

function CourseOverview() {
    const { courseId } = useParams<{ courseId: string }>();
    const [course, setCourse] = useState<CourseResponse | null>(null);
    const [userIsEnrolled, setUserIsEnrolled] = useState<boolean | null>(null);
    const [loadingCourse, setLoadingCourse] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const { token } = useAuth();

    const handleCourseError = (error: unknown, courseId: string) => {
        if (axios.isAxiosError(error)) {
            console.error(`Axios Error while getting course with id ${courseId}:`, error);
            if (error.status === HttpStatusCode.NotFound) {
                return "course-not-found";
            } else {
                return "axios-general-error";
            }
        } else {
            console.error(`Something went wrong while getting course with id ${courseId}:`, error);
            return "general-fetching-error";
        }
    }

    const fetchCourse = async (courseId: string) => {
        try {
            setLoadingCourse(true);
            setError(null);
            setUserIsEnrolled(null)
            setCourse(null);
            const courseResponse = await axios.get<CourseResponse>(`${BACKEND_BASE_URL}/courses/${courseId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setCourse(courseResponse.data);

            const enrollmentResponse = await axios.get<UserEnrollmentResponse>(`${BACKEND_BASE_URL}/courses/${courseId}/enrollment`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            console.log("enrollmentResponse: ");
            console.log(enrollmentResponse);

            setUserIsEnrolled(enrollmentResponse.data.enrolledStatus === "enrolled");
        } catch (error) {
            setError(handleCourseError(error, courseId));
        } finally {
            setLoadingCourse(false);
        }
    }
    useEffect(() => {
        console.log("useEffect for fetching course");

        if (courseId) {
            fetchCourse(courseId);
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

    if (!course || userIsEnrolled === null) {
        return <div>It should never happen actually :/</div>
    }

    if (userIsEnrolled) {
        return <CourseContentPage course={course} />
    } else {
        return <CoursePreviewPage course={course} onEnroll={() => setUserIsEnrolled(true)} />
    }

}

export default CourseOverview