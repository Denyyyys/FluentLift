import { useNavigate } from "react-router-dom";
import type { UiLesson } from "../../types/course";
import { IoIosCheckmarkCircle } from "react-icons/io";
import { useCourse } from "../../utils/utils";

interface RequireLessonRowProps {
    uiLesson: UiLesson;
}

function LessonRow({ uiLesson }: RequireLessonRowProps) {
    let navigate = useNavigate();
    const { uiCourse } = useCourse();
    return (
        <div
            className="d-flex align-items-stretch gap-2 mb-2 clickable p-2"
            onClick={() => navigate(`/courses/${uiCourse.id}/lessons/${uiLesson.id}`)}
        >
            <h5>
                Lesson {uiLesson.lessonNumber} {uiLesson.title && ` - ${uiLesson.title}`}
            </h5>
            {uiLesson.progressInfo.progress === 100 && <IoIosCheckmarkCircle size={24} />}
        </div>
    )
}

export default LessonRow