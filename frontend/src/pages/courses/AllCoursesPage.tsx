import React, { useEffect, useState } from 'react'
import type { CourseResponse } from '../../types/course'
import axios from 'axios';
import { BACKEND_BASE_URL } from '../../constants';
import { useAuth } from '../../context/AuthContext';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import UnreachableState from '../../components/common/UnreachableState';
import CourseCard from '../../components/courses/CourseCard';

function AllCoursesPage() {
    const [courses, setCourses] = useState<null | CourseResponse[]>(null);
    const { token } = useAuth();
    const [loadingCourse, setLoadingCourse] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchCourses = async () => {
        try {
            setLoadingCourse(true);
            setError(null);

            const coursesResponse = await axios.get<CourseResponse[]>(`${BACKEND_BASE_URL}/courses`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            setCourses(coursesResponse.data)

        } catch (error) {
            console.error(`Axios Error while getting courses:`, error);
            setError("Error while getting courses");
        } finally {
            setLoadingCourse(false);
        }

    }

    useEffect(() => {
        fetchCourses()
    }, [])

    if (loadingCourse) {
        return <LoadingSpinner />
    }

    if (error !== null) {
        return <div>{error}</div>
    }

    if (courses === null) {
        return <UnreachableState />
    }

    console.log(courses);

    return (
        <>
            <h3 className='mb-3 pb-1 fw-semibold header-with-line-bottom'>All Courses</h3>
            <div className='courses-container container'>
                <div className="row ">
                    {courses.map(course => <CourseCard key={course.id} course={course} />)}
                </div>
            </div>
        </>
    )
}

export default AllCoursesPage