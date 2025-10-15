import { useNavigate } from "react-router-dom";
import type { LessonProgress, LessonResponse } from "../../types/course";
import { IoIosCheckmarkCircle } from "react-icons/io";

interface RequireLessonRowProps {
    lesson: LessonResponse;
    lessonProgress: LessonProgress;
    courseId: number;
}

function LessonRow({ lesson, lessonProgress, courseId }: RequireLessonRowProps) {
    let navigate = useNavigate();

    return (
        <div className="d-flex align-items-stretch gap-2 mb-2 clickable p-2" onClick={() => navigate(`/courses/${courseId}/lessons/${lesson.id}`)}><h5>
            Lesson {lesson.lessonNumber} {lesson.title && ` - ${lesson.title}`}
        </h5>
            {lessonProgress.progress === 100 && <IoIosCheckmarkCircle size={24} />}
        </div>
    )
}

export default LessonRow