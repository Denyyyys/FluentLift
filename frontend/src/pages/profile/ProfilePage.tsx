import { useAuth } from '../../context/AuthContext'
import { Button, Card, Col, Container, Row } from 'react-bootstrap';
import { FaUserCircle } from 'react-icons/fa';
import { Link, useNavigate } from 'react-router-dom';
import { useLanguage } from '../../hooks/useLanguage';
import { textByLanguage } from '../../assets/translations';

export default function ProfilePage() {
    const navigate = useNavigate();
    const { email, userName } = useAuth();
    const { language } = useLanguage()

    const handleLogout = () => {
        localStorage.removeItem("jwtToken");
        navigate("/sign-up");
    };

    return (
        <Container className="py-5" >
            <Row className="justify-content-center">
                <Col md={6}>
                    <Card className="shadow-sm p-4 text-center shadow">
                        <FaUserCircle size={80} className="mb-3" />
                        <h3 className="mb-2">{userName}</h3>
                        <p className="text-muted">{email}</p>

                        <Link to="/change-password" className="d-block mb-3">
                            {textByLanguage[language]["authenticatoin"]["forgotPasswordText"]}
                        </Link>

                        <Button variant="danger" onClick={handleLogout}>
                            {textByLanguage[language]["authenticatoin"]["logOutText"]}
                        </Button>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}