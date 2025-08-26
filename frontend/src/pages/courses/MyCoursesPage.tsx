import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext';

function MyCoursesPage() {
    const navigate = useNavigate();
    const { token } = useAuth();
    return (
        <div className='profile-courses-container'>
            <div className='created-courses-container'>
                <h3 className='mb-3'>
                    Created Courses
                </h3>
                <button className='btn btn-success' onClick={() => { navigate('/profile/courses/new') }}> Create New Course</button>
                <div>

                </div>
            </div>

            <div className='enroled-courses-container'>
                <h3>
                    Enrolled Courses
                </h3>
            </div>
        </div>
    )
}

export default MyCoursesPage