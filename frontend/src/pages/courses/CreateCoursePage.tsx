import { useState } from "react";
import { FaArrowLeft } from "react-icons/fa";
import { FaArrowRight } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import InfoStep from "../../components/courses/CreateCourseForm/InfoStep";
import MaterialsStep from "../../components/courses/CreateCourseForm/MaterialsStep/MaterialsStep";
import type { Course } from "../../types/course";
import { v4 as uuid } from "uuid";
import { useAuth } from "../../context/AuthContext";
import axios from "axios";

function CreateCoursePage() {
    const navigate = useNavigate();
    const [course, setCourse] = useState<Course>({ id: uuid(), title: "", description: "", goals: [], prerequisiteLevel: "None", outcomeLevel: "None", baseLanguage: "English", targetLanguage: "English", units: [] });
    const [step, setStep] = useState<"info" | "materials">("info");
    const [openedUnitId, setOpenedUnitId] = useState<null | string>(null);
    const [openedLessonId, setOpenedLessonId] = useState<null | string>(null);
    const { token } = useAuth();

    const publishCourse = async () => {
        // console.log(course);
        // TODO add proper unit numbers and lesson numbers before making request
        // console.log(JSON.parse(JSON.stringify(course, null, 2)));
        try {
            const response = await axios.post(
                "http://localhost:8080/api/v1/courses",
                course,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                }
            )
            console.log(response.data);
            navigate("/profile/courses/");
        } catch (error) {
            console.error("Error while posting data:", error);
            // throw error;
        }
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
                <button onClick={async () => { step === "info" ? setStep("materials") : await publishCourse() }} className={`btn btn-shaddow min-w-120px ${step === "info" ? "btn-primary" : "btn-success"}`}>{step === "info" ? <>Next Step <FaArrowRight /> </> : "Publish"}</button>
            </div>
        </div>
    )
}

export default CreateCoursePage