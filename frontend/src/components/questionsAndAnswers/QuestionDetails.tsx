
import { Badge, Button } from "react-bootstrap";
import type { QuestionWithAnswersDto } from "../../types/questionsAndAnswers";
import { FaRegEye } from "react-icons/fa";
import { FaArrowDown, FaArrowUp } from "react-icons/fa6";
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";

interface Props {
    question: QuestionWithAnswersDto;
}

export const QuestionDetails = ({ question }: Props) => {
    const { language } = useLanguage();

    return (
        <div className="card mb-4">
            <div className="card-body">
                <h3 className="mb-2">
                    {question.title}
                    {question.solved && (
                        <Badge bg="success" className="ms-2">
                            {textByLanguage[language]["singleQuestion"]["solvedStatusText"]}
                        </Badge>
                    )}
                </h3>

                <p className="text-muted mb-1">
                    {textByLanguage[language]["singleQuestion"]["aksedByText"]} <strong>{question.author.name}</strong> ·{" "}
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
                    <Button size="sm" variant="outline-success"><FaArrowUp /></Button>
                    <span>{question.upvotes}</span>

                    <Button size="sm" variant="outline-danger"><FaArrowDown /></Button>
                    <span>{question.downvotes}</span>

                    <span className="text-muted ms-auto">
                        <FaRegEye /> {question.views}
                    </span>
                </div>
            </div>
        </div>
    );
};
