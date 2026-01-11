import { useNavigate, } from "react-router-dom"
import type { UserEnrollmentResponse } from "../../types/course";
import { useAuth } from "../../context/AuthContext";
import { countNumberOfLessons, useCourse } from "../../utils/utils";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { toast } from 'react-toastify';
import { Badge, Button, Card, Col, Container, Row } from "react-bootstrap";
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";

interface RequireCoursePreviewProps {
    onEnroll: () => void;
}

function CoursePreviewPage({ onEnroll }: RequireCoursePreviewProps) {
    let navigate = useNavigate();
    const { language } = useLanguage();
    const { userId, token } = useAuth();

    const { uiCourse } = useCourse();

    const enroll = async () => {
        try {
            const enrollResponse = await axios.post<UserEnrollmentResponse>(`${BACKEND_BASE_URL}/courses/${uiCourse.id}/users`, undefined, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (enrollResponse.data.enrolledStatus === "enrolled") {
                toast.success("You enrolled for a course!")
                onEnroll();
            }

        }
        catch (error) {
            console.error(error);
            toast.error("Couldn't enroll for course. Check logs")
        }
    }
    return (
        <Container className="py-5">
            <Row className="mb-4">
                <Col>
                    <h1 className="fw-bold">{uiCourse.title}</h1>
                </Col>
            </Row>

            <Row className="g-4">
                <Col md={8}>
                    <Card className="shadow h-100">
                        <Card.Body>
                            <Card.Title className="mb-3">{textByLanguage[language]["singleCourse"]["courseInformationText"]}</Card.Title>

                            <p>
                                {uiCourse.description}
                            </p>

                            <h6 className="fw-bold"> {textByLanguage[language]["singleCourse"]["courseGoalsText"]}</h6>
                            <ul className="mb-0" >
                                {uiCourse.goals.map(goal => <li>{goal}</li>)}
                            </ul>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={4}>
                    <Card className="shadow">
                        <Card.Body>
                            <Card.Title className="mb-3">{textByLanguage[language]["singleCourse"]["detailsText"]}</Card.Title>

                            <div className="mb-2">
                                <strong>{textByLanguage[language]["allCourses"]["baseLanguageText"]}: </strong>
                                <Badge bg="secondary">{uiCourse.baseLanguage}</Badge>
                            </div>

                            <div className="mb-2">
                                <strong>{textByLanguage[language]["allCourses"]["targetLanguageText"]}: </strong>
                                <Badge bg="primary">{uiCourse.targetLanguage}</Badge>
                            </div>

                            <div className="mb-2">
                                <strong>{textByLanguage[language]["allCourses"]["prerequisiteLevelText"]}: </strong>
                                <Badge bg="warning" text="dark">{uiCourse.prerequisiteLevel}</Badge>
                            </div>

                            <div className="mb-3">
                                <strong>{textByLanguage[language]["allCourses"]["outcomeLevelText"]}: </strong>
                                <Badge bg="success">{uiCourse.outcomeLevel}</Badge>
                            </div>

                            {/* <Button variant="primary" className="w-100">
                                {textByLanguage[language]["allCourses"]["goToCourseActionText"]}
                            </Button> */}
                            {userId === uiCourse.creator.id ?
                                <Button variant="warning" onClick={() => navigate(`/courses/${uiCourse.id}/edit`)}>{textByLanguage[language]["singleCourse"]["editCourseText"]}</Button> : <Button variant="success" onClick={async () => {
                                    await enroll();
                                }}>{textByLanguage[language]["singleCourse"]["enrollToCourseText"]}</Button>
                            }
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container >
    )
}

export default CoursePreviewPage