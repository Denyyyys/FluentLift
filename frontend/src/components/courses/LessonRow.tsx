import { useNavigate } from "react-router-dom";
import type { UiLesson } from "../../types/course";
import { IoIosCheckmarkCircle } from "react-icons/io";
import { useCourse } from "../../utils/utils";
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";

interface RequireLessonRowProps {
    uiLesson: UiLesson;
}

function LessonRow({ uiLesson }: RequireLessonRowProps) {
    let navigate = useNavigate();
    const { language } = useLanguage();

    const { uiCourse } = useCourse();
    return (
        <div
            className="d-flex align-items-stretch gap-2 mb-2 clickable p-2"
            onClick={() => navigate(`/courses/${uiCourse.id}/lessons/${uiLesson.id}`)}
        >
            <h5>
                {textByLanguage[language]["singleEnrolledCourse"]["lessonText"]} {uiLesson.lessonNumber} {uiLesson.title && ` - ${uiLesson.title}`}
            </h5>
            {uiLesson.progressInfo.progress === 100 && <IoIosCheckmarkCircle size={24} />}
        </div>
    )
}

export default LessonRow