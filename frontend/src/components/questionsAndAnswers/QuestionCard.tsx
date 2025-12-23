import type { QuestionResponseDto } from "../../types/questionsAndAnswers";
import { useNavigate } from "react-router-dom";
import { Badge } from "react-bootstrap";


interface Props {
    question: QuestionResponseDto;
}

export const QuestionCard = ({ question }: Props) => {
    const navigate = useNavigate();

    return (
        <div
            className="card mb-3 cursor-pointer"
            onClick={() => navigate(`/questions/${question.id}`)}
            style={{ cursor: "pointer" }}
        >
            <div className="card-body">
                <div className="d-flex justify-content-between align-items-start">
                    <h5 className="card-title mb-1">
                        {question.title}
                        {question.solved ? (
                            <Badge bg="success" className="ms-2">
                                Solved
                            </Badge>
                        ) : (
                            <Badge bg="danger" className="ms-2">
                                Not Solved
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
                        <span className="me-3">⬆ {question.upvotes}</span>
                        <span className="me-3">⬇ {question.downvotes}</span>
                        <span className="me-3">👁 {question.views}</span>
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
                    Asked by <strong>{question.author.name}</strong>
                    {" · "}
                    {question.baseLanguage} → {question.targetLanguage}
                </div>
            </div>
        </div>
    );
};
