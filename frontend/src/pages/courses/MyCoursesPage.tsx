import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext';
import { useEffect, useState } from 'react';
import type { CourseResponse } from '../../types/course';
import axios from 'axios';
import { BACKEND_BASE_URL } from '../../constants';
import LoadingSpinner from '../../components/common/LoadingSpinner';

function MyCoursesPage() {
    const navigate = useNavigate();
    const { token } = useAuth();
    const [createdByMeCourses, setCreatedByMeCourses] = useState<CourseResponse[]>([]);
    const [enrolledCourses, setEnrolledCourses] = useState<CourseResponse[]>([]);

    const [loadingCourses, setLoadingCourses] = useState(true);

    const fetchCreatedByMeCourses = async () => {
        try {
            const createdCoursesResponse = await axios.get<CourseResponse[]>(`${BACKEND_BASE_URL}/courses/me/created`, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            const enrolledCoursesResponse = await axios.get<CourseResponse[]>(`${BACKEND_BASE_URL}/courses/me/enrolled`, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            setCreatedByMeCourses(createdCoursesResponse.data);
            setEnrolledCourses(enrolledCoursesResponse.data);

        } catch (e) {
            console.error(e);
            console.log("Failed to fetch courses :(");
        } finally {
            setLoadingCourses(false);
        }
    }

    useEffect(() => {
        fetchCreatedByMeCourses();
    }, []);

    return (
        <div className='profile-courses-container'>
            <div className='created-courses-container'>
                <h3 className='mb-3'>
                    Created Courses
                </h3>
                {loadingCourses ? <LoadingSpinner /> : <ul>
                    {createdByMeCourses.map(course => {
                        return <li>
                            <Link to={`/courses/${course.id}`}> {course.title}</Link>
                        </li>

                    })}
                </ul>}

                <button className='btn btn-success' onClick={() => { navigate('/profile/courses/new') }}> Create New Course</button>
            </div>

            <div className='enroled-courses-container'>
                <h3>
                    Enrolled Courses
                </h3>
                {loadingCourses ? <LoadingSpinner /> : <ul>
                    {enrolledCourses.map(course => {
                        return <li>
                            <Link to={`/courses/${course.id}`}> {course.title}</Link>
                        </li>

                    })}
                </ul>}
            </div>
        </div>
    )
}

export default MyCoursesPage