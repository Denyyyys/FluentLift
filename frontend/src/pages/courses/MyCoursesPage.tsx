import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext';
import { useEffect, useState } from 'react';
import type { CourseResponse } from '../../types/course';
import axios from 'axios';
import { BACKEND_BASE_URL } from '../../constants';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import CourseCard from '../../components/courses/CourseCard';

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
                <h3 className='mb-3 pb-1 fw-semibold header-with-line-bottom'>Created Courses</h3>
                <div className='courses-container container'>
                    {loadingCourses ? <LoadingSpinner /> : <div className="row ">
                        {createdByMeCourses.map(course => {
                            return <CourseCard key={course.id} course={course} />
                        })}
                    </div>}
                </div>
                <div className="d-flex justify-content-center m-2">
                    <button className='btn btn-lg btn-success btn-block' onClick={() => { navigate('/profile/courses/new') }}> Create New Course</button>
                </div>

            </div>

            <div className='enroled-courses-container'>
                <h3 className='mb-3 pb-1 fw-semibold header-with-line-bottom'>Enrolled Courses</h3>
                <div className='courses-container container'>

                    {loadingCourses ? <LoadingSpinner /> : <div className="row ">
                        {enrolledCourses.map(course => {
                            return <CourseCard key={course.id} course={course} />
                        })}
                    </div>}
                </div>

            </div>
        </div >
    )
}

export default MyCoursesPage