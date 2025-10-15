import { useEffect, useState } from "react";
import { Outlet, useParams } from "react-router-dom";
import type { CourseResponse } from "../types/course";
import { useAuth } from "../context/AuthContext";
import axios, { HttpStatusCode } from "axios";
import { BACKEND_BASE_URL } from "../constants";

function CourseLayout() {
    const { courseId } = useParams<{ courseId: string }>();
    const [course, setCourse] = useState<CourseResponse | null>(null);
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

            setCourse(null);
            const courseResponse = await axios.get<CourseResponse>(`${BACKEND_BASE_URL}/courses/${courseId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setCourse(courseResponse.data);

        } catch (error) {
            console.error(`Axios Error while getting enrollment for course with id ${courseId}:`, error);

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

    if (!course) {
        return <div>It should never happen actually :/</div>
    }

    return <Outlet context={{ course }} />
}

export default CourseLayout