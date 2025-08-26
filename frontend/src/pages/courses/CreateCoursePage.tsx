import { useState } from "react";
import { FaArrowLeft } from "react-icons/fa";
import { FaArrowRight } from "react-icons/fa";
import InfoStep from "../../components/courses/CreateCourseForm/InfoStep";
import MaterialsStep from "../../components/courses/CreateCourseForm/MaterialsStep/MaterialsStep";
import type { Course } from "../../types/course";
import { v4 as uuid } from "uuid";

function CreateCoursePage() {
    const [course, setCourse] = useState<Course>({ id: uuid(), title: "", description: "", goals: [], prerequisiteLevel: "None", outcomeLevel: "None", baseLanguage: "English", targetLanguage: "English", units: [] });
    const [step, setStep] = useState<"info" | "materials">("info");
    const [openedUnitId, setOpenedUnitId] = useState<null | string>(null);
    const [openedLessonId, setOpenedLessonId] = useState<null | string>(null);

    const publishCourse = () => {
        // console.log(course);
        console.log(JSON.parse(JSON.stringify(course, null, 2)));
    }

    return (
        <div className='d-flex flex-column  new-course-container'>
            <div className='new-course-header d-flex justify-content-between pb-1 mt-3 mb-3'>
                <h3 className="fw-semibold">
                    Create Course
                </h3>
                <h4>
                    {
                        step === "info" ? "Basic Information" : "Adding Materials"
                    }
                </h4>
            </div>
            <div className="flex-fill">
                {
                    step === "info" ? <InfoStep
                        course={course}
                        setCourse={setCourse}
                    /> :
                        <MaterialsStep
                            course={course}
                            setCourse={setCourse}
                            openedUnitId={openedUnitId}
                            setOpenedUnitId={setOpenedUnitId}
                            openedLessonId={openedLessonId}
                            setOpenedLessonId={setOpenedLessonId}
                        />
                }
            </div>
            <div className="d-flex justify-content-between mt-3 mb-3">
                <button className="btn btn-shaddow btn-danger">Delete Forever</button>
                <button className="btn btn-shaddow btn-secondary">Finish Later</button>
                <button className="btn btn-shaddow btn-primary" onClick={() => setStep("info")} disabled={step === 'info'} ><FaArrowLeft /> Go Back  </button>
                <button onClick={() => { step === "info" ? setStep("materials") : publishCourse() }} className={`btn btn-shaddow min-w-120px ${step === "info" ? "btn-primary" : "btn-success"}`}>{step === "info" ? <>Next Step <FaArrowRight /> </> : "Publish"}</button>
            </div>
        </div>
    )
}

export default CreateCoursePage