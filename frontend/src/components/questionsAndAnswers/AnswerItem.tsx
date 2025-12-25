import { Badge, Button } from "react-bootstrap";
import type { AnswerThreadDto } from "../../types/questionsAndAnswers";
import { AnswerForm } from "./AnswerForm";

interface Props {
    answer: AnswerThreadDto;
    level?: number;
    isQuestionAuthor: boolean;
    questionSolved: boolean;
    questionAuthorId: number;
    replyingToId: number | null;
    setReplyingToId: (id: number | null) => void;
    submitAnswer: (questionId: string, content: string, parentAnswerId: null | number) => void;
    questionId: string;
    token: string;
    acceptAnswer: (questionId: string, answerId: number) => Promise<void>;
}

export const AnswerItem = ({
    answer,
    level = 0,
    isQuestionAuthor,
    questionSolved,
    questionAuthorId,
    replyingToId,
    setReplyingToId,
    submitAnswer,
    questionId,
    token,
    acceptAnswer
}: Props) => {
    const showReplyForm = replyingToId === answer.id;



    return (
        <div
            id={answer.accepted ? "accepted-answer" : undefined}
            className="mb-3"
            style={{
                marginLeft: level === 1 ? "2rem" : 0,
                borderLeft: level === 1 ? "2px solid #ddd" : undefined,
                paddingLeft: level === 1 ? "1rem" : undefined
            }}
        >
            <div className="card">
                <div className="card-body">
                    {level > 1 && (
                        <strong>@{answer.author.name} </strong>
                    )}

                    <p className="mb-2">{answer.content}</p>

                    <div className="d-flex align-items-center gap-2 flex-wrap">
                        <Button size="sm" variant="outline-success">⬆</Button>
                        <small>{answer.upvotes}</small>

                        <Button size="sm" variant="outline-danger">⬇</Button>
                        <small>{answer.downvotes}</small>

                        {answer.author.id === questionAuthorId && (
                            <Badge bg="info" className="ms-2">
                                Author
                            </Badge>
                        )}

                        {answer.accepted && (
                            <Badge bg="success" className="ms-2">
                                ✓ Accepted
                            </Badge>
                        )}

                        {isQuestionAuthor && !questionSolved && !answer.accepted && (
                            <Button size="sm" variant="outline-primary" className="ms-auto" onClick={() => acceptAnswer(questionId, answer.id)}>
                                Accept answer
                            </Button>
                        )}

                        <Button
                            size="sm"
                            variant="link"
                            onClick={() =>
                                setReplyingToId(showReplyForm ? null : answer.id)
                            }
                        >
                            Reply
                        </Button>
                    </div>

                    {showReplyForm && (
                        <AnswerForm
                            placeholder={`Reply to ${answer.author.name}...`}
                            onSubmit={submitAnswer}
                            onCancel={() => setReplyingToId(null)}
                            parentAnswerId={answer.id}
                            questionId={questionId}

                        />
                    )}

                    <small className="text-muted">
                        Answered by {answer.author.name} ·{" "}
                        {new Date(answer.createdAt).toLocaleDateString()}
                    </small>
                </div>
            </div>

            {answer.replies.map(reply => (
                <AnswerItem
                    key={reply.id}
                    answer={reply}
                    level={level + 1}
                    isQuestionAuthor={isQuestionAuthor}
                    questionSolved={questionSolved}
                    questionAuthorId={questionAuthorId}
                    replyingToId={replyingToId}
                    setReplyingToId={setReplyingToId}
                    questionId={questionId}
                    submitAnswer={submitAnswer}
                    token={token}
                    acceptAnswer={acceptAnswer}
                />
            ))}
        </div>
    );
};
