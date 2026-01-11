import type { QuestionResponseDto } from "../../types/questionsAndAnswers";
import { useNavigate } from "react-router-dom";
import { Badge } from "react-bootstrap";
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";
import { translateLanguageName } from "../../utils/utils";
import type { ALL_LANGUAGES_TYPE } from "../../constants";
import { FaArrowDown, FaArrowRight, FaArrowUp } from "react-icons/fa6";
import { FaRegEye } from "react-icons/fa";


interface Props {
    question: QuestionResponseDto;
}

export const QuestionCard = ({ question }: Props) => {
    const navigate = useNavigate();
    const { language } = useLanguage();
    return (
        <div
            className="card mb-3 cursor-pointer shadow"
            onClick={() => navigate(`/questions/${question.id}`)}
            style={{ cursor: "pointer" }}
        >
            <div className="card-body">
                <div className="d-flex justify-content-between align-items-start">
                    <h5 className="card-title mb-1">
                        {question.title}
                        {question.solved ? (
                            <Badge bg="success" className="ms-2">
                                {textByLanguage[language]["allQuestions"]["solvedStatusText"]}
                            </Badge>
                        ) : (
                            <Badge bg="danger" className="ms-2">
                                {textByLanguage[language]["allQuestions"]["notSolvedStatusText"]}
                            </Badge>
                        )}
                    </h5>
                    <small className="text-muted">
                        {new Date(question.createdAt).toLocaleDateString()}
                    </small>
                </div>

                <p className="card-text text-muted mb-2">
                    {question.content.length > 200
                        ? question.content.slice(0, 200) + "..."
                        : question.content}
                </p>

                <div className="d-flex justify-content-between align-items-center">
                    <div>
                        <span className="me-3"><FaArrowUp /> {question.upvotes}</span>
                        <span className="me-3"><FaArrowDown /> {question.downvotes}</span>
                        <span className="me-3"><FaRegEye /> {question.views}</span>
                    </div>

                    <div>
                        {question.tags.map(tag => (
                            <Badge key={tag} bg="secondary" className="me-1">
                                {tag}
                            </Badge>
                        ))}
                    </div>
                </div>

                <div className="mt-2 text-muted small">
                    {textByLanguage[language]["allQuestions"]["aksedByText"]} <strong>{question.author.name}</strong>
                    {" · "}
                    {translateLanguageName(language, question.baseLanguage as ALL_LANGUAGES_TYPE)} <FaArrowRight /> {translateLanguageName(language, question.targetLanguage as ALL_LANGUAGES_TYPE)}
                </div>
            </div>
        </div>
    );
};
