
import { Badge, Button } from "react-bootstrap";
import type { QuestionWithAnswersDto } from "../../types/questionsAndAnswers";

interface Props {
    question: QuestionWithAnswersDto;
}

export const QuestionDetails = ({ question }: Props) => {
    return (
        <div className="card mb-4">
            <div className="card-body">
                <h3 className="mb-2">
                    {question.title}
                    {question.solved && (
                        <Badge bg="success" className="ms-2">
                            Solved
                        </Badge>
                    )}
                </h3>

                <p className="text-muted mb-1">
                    Asked by <strong>{question.author.name}</strong> ·{" "}
                    {new Date(question.createdAt).toLocaleDateString()}
                </p>

                <p className="mt-3">{question.content}</p>

                <div className="mb-3">
                    {question.tags.map(tag => (
                        <Badge key={tag} bg="secondary" className="me-1">
                            {tag}
                        </Badge>
                    ))}
                </div>

                <div className="d-flex align-items-center gap-3">
                    <Button size="sm" variant="outline-success">⬆</Button>
                    <span>{question.upvotes}</span>

                    <Button size="sm" variant="outline-danger">⬇</Button>
                    <span>{question.downvotes}</span>

                    <span className="text-muted ms-auto">
                        👁 {question.views}
                    </span>
                </div>
            </div>
        </div>
    );
};
