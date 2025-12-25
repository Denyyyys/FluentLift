import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
// import { QuestionWithAnswersDto } from "../types/question";
// import { QuestionDetails } from "../components/QuestionDetails";
// import { AnswerItem } from "../components/AnswerItem";
import { Button } from "react-bootstrap";
import type { AnswerCreateRequestDto, QuestionWithAnswersDto } from "../../types/questionsAndAnswers";
import { QuestionDetails } from "../../components/questionsAndAnswers/QuestionDetails";
import { AnswerItem } from "../../components/questionsAndAnswers/AnswerItem";
import UnreachableState from "../../components/common/UnreachableState";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { useAuth } from "../../context/AuthContext";
import ErrorWrapper from "../../components/common/ErrorWrapper";
import LoadingSpinner from "../../components/common/LoadingSpinner";
import { AnswerForm } from "../../components/questionsAndAnswers/AnswerForm";
import { toast } from "react-toastify";

export const SingleQuestionPage = () => {
    const { questionId } = useParams();
    const navigate = useNavigate();

    const [question, setQuestion] = useState<QuestionWithAnswersDto | null>(null);
    const [replyingToId, setReplyingToId] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { token, userId } = useAuth();

    if (questionId === undefined || token === null) {
        return <UnreachableState />
    }

    const scrollToAccepted = () => {
        const el = document.getElementById("accepted-answer");
        el?.scrollIntoView({ behavior: "smooth", block: "center" });
    };

    const fetchQuestionById = async (questionId: string) => {
        try {
            setLoading(true);
            setError(null);
            const questionResponse = await axios.get<QuestionWithAnswersDto>(`${BACKEND_BASE_URL}/qa/questions/${questionId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            setQuestion(questionResponse.data);

        } catch (error) {
            console.error(`Axios Error while getting question:`, error);
            setError("Error while getting question");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchQuestionById(questionId);
    }, [questionId]);

    const addView = async () => {
        await axios.post(
            `${BACKEND_BASE_URL}/qa/questions/${questionId}/addView`,
            null,
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );
    };

    useEffect(() => {
        addView();
    }, []);

    const submitAnswer = async (questionId: string, content: string, parentAnswerId: number | null = null) => {
        try {

            let answer: AnswerCreateRequestDto = { content };

            if (parentAnswerId !== null) {
                answer.parentAnswerId = parentAnswerId;
            }

            await axios.post(`${BACKEND_BASE_URL}/qa/questions/${questionId}/answers`, answer, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })

            toast.success("Added your answer!");

            await fetchQuestionById(questionId);

        } catch (e) {
            console.error("Failed to add answer:", error);
            toast.error("Failed to add answer");
        }

    }

    const acceptAnswer = async (questionId: string, answerId: number) => {
        try {
            await axios.post(
                `${BACKEND_BASE_URL}/qa/questions/${questionId}/answers/${answerId}/accept`,
                null,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            toast.success("Accepted answer!");

            await fetchQuestionById(questionId);

        } catch (e) {
            console.error("Failed to accept answer:", error);
            toast.error("Failed to accept answer");
        }
    }

    if (loading) return <LoadingSpinner />;

    if (error !== null) return <ErrorWrapper content={`There was a problem while fetching qeustion with id: ${questionId}`} />

    if (!question) return <UnreachableState />;

    const isQuestionAuthor = question.author.id === userId;

    return (
        <div className="container mt-4">
            <Button
                variant="link"
                className="mb-3"
                onClick={() => navigate("/questions")}
            >
                ← Back to all questions
            </Button>

            <QuestionDetails question={question} />
            <hr />

            <h5>Your Answer</h5>

            <AnswerForm
                placeholder="Write your answer..."
                parentAnswerId={null}
                questionId={questionId}
                onSubmit={submitAnswer}
            />
            <h4 className="mt-4 mb-3">Answers</h4>
            {question.solved && (
                <Button
                    size="sm"
                    variant="outline-success"
                    className="mb-3 ms-3"
                    onClick={scrollToAccepted}
                >
                    Jump to accepted answer
                </Button>
            )}

            {question.answers.length === 0 ? (
                <p className="text-muted">No answers yet. Be the first to answer!</p>
            ) : (
                question.answers.map(answer => (
                    <AnswerItem
                        key={answer.id}
                        answer={answer}
                        isQuestionAuthor={isQuestionAuthor}
                        questionSolved={question.solved}
                        questionAuthorId={question.author.id}
                        replyingToId={replyingToId}
                        setReplyingToId={setReplyingToId}
                        submitAnswer={submitAnswer}
                        questionId={questionId}
                        token={token}
                        acceptAnswer={acceptAnswer}
                    />
                ))
            )}

        </div>
    );
};
