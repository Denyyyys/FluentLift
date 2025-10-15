import { useEffect, useState } from "react";
import axios, { HttpStatusCode } from "axios";
import type { UserEnrollmentResponse } from "../types/course";
import { BACKEND_BASE_URL } from "../constants";
import { useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import CoursePreviewPage from "../pages/courses/CoursePreviewPage";
import CourseContentPage from "../pages/courses/CourseContentPage";
import { useCourse } from "../utils/utils";

function CourseOverview() {
    const { courseId } = useParams<{ courseId: string }>();
    const { course } = useCourse();
    const [userIsEnrolled, setUserIsEnrolled] = useState<boolean | null>(null);
    const [loadingEnrollment, setLoadingEnrollment] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const { token } = useAuth();

    const handleEnrollmentError = (error: unknown, courseId: string) => {
        if (axios.isAxiosError(error)) {
            console.error(`Axios Error while getting enrollment for course with id ${courseId}:`, error);
            if (error.status === HttpStatusCode.NotFound) {
                return "enrollment-not-found";
            } else {
                return "axios-general-error";
            }
        } else {
            console.error(`Something went wrong while getting course with id ${courseId}:`, error);
            return "general-fetching-error";
        }
    }

    const fetchEnrollment = async (courseId: string) => {
        try {
            setLoadingEnrollment(true);
            setError(null);
            setUserIsEnrolled(null)

            const enrollmentResponse = await axios.get<UserEnrollmentResponse>(`${BACKEND_BASE_URL}/courses/${courseId}/enrollment`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            console.log("enrollmentResponse: ");
            console.log(enrollmentResponse);

            setUserIsEnrolled(enrollmentResponse.data.enrolledStatus === "enrolled");
        } catch (error) {
            setError(handleEnrollmentError(error, courseId));
        } finally {
            setLoadingEnrollment(false);
        }
    }
    useEffect(() => {
        console.log("useEffect for fetching course");

        if (courseId) {
            fetchEnrollment(courseId);
        }
    }, [courseId])

    if (loadingEnrollment) {
        return <div>Loading...</div>
    }

    if (error !== null) {
        switch (error) {
            case "enrollment-not-found":
                return <div>
                    <h2>Ooops it seems like course which you are looking for doesn't exist or was deleted or archived.</h2>
                </div>
            default:
                return <div>
                    <h2>Some unexpected error happened. See console logs for more info.</h2>
                </div>
        }
    }

    if (userIsEnrolled === null) {
        return <div>It should never happen actually :/</div>
    }

    if (userIsEnrolled) {
        return <CourseContentPage course={course} />
    } else {
        return <CoursePreviewPage course={course} onEnroll={() => setUserIsEnrolled(true)} />
    }

}

export default CourseOverview