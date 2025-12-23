import { useEffect, useState } from "react";

import { FaSearch } from "react-icons/fa";
import { Form, Button } from "react-bootstrap";
import type { QuestionsPageResponseDto } from "../../types/questionsAndAnswers";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { useAuth } from "../../context/AuthContext";
import LoadingSpinner from "../../components/common/LoadingSpinner";
import { QuestionCard } from "../../components/questionsAndAnswers/QuestionCard";
import ErrorWrapper from "../../components/common/ErrorWrapper";

function QuestionsPage() {
    const [query, setQuery] = useState("");
    const [sortBy, setSortBy] = useState("createdAt");
    const [onlySolved, setOnlySolved] = useState(false);
    const [page, setPage] = useState(1);

    const [questionsPage, setQuestionsPage] = useState<QuestionsPageResponseDto | null>(null);
    const [isFirstPage, setIsFirstPage] = useState(true);
    const [isLastPage, setIsLastPage] = useState(true);

    const [loadingQuestions, setLoadingQuestions] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const { token } = useAuth();

    const loadQuestions = async () => {
        try {
            setLoadingQuestions(true);
            setError(null);

            const questionsResponse = await axios.get<QuestionsPageResponseDto>(`${BACKEND_BASE_URL}/qa/questions?query=${query}&isSolved=${onlySolved}&sortBy=${sortBy}&page=${page}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            setQuestionsPage(questionsResponse.data);
            setIsFirstPage(questionsResponse.data.page == 1);
            setIsLastPage(questionsResponse.data.page == questionsResponse.data.totalPages);

        } catch (error) {
            console.error(`Axios Error while getting questions:`, error);
            setError("Error while getting questions");
        } finally {
            setLoadingQuestions(false);
        }
    };

    useEffect(() => {
        loadQuestions();
    }, [sortBy, page, onlySolved]);

    const handleSearch = () => {
        setPage(1);
        loadQuestions();
    };

    return (
        <div className="container mt-4">
            <div className="row mb-4 align-items-center">
                <div className="col-md-6">
                    <div className="input-group">
                        <input
                            className="form-control"
                            placeholder="Search by title or content..."
                            value={query}
                            onChange={e => setQuery(e.target.value)}
                        />
                        <Button variant="primary" onClick={handleSearch}>
                            <FaSearch />
                        </Button>
                    </div>
                </div>

                <div className="col-md-2 d-flex align-items-center">
                    <Form.Check
                        type="checkbox"
                        label="Solved only"
                        checked={onlySolved}
                        onChange={e => {
                            setOnlySolved(e.target.checked);
                            setPage(1);
                        }}
                    />
                </div>

                <div className="col-md-3 ms-auto">
                    <Form.Select
                        value={sortBy}
                        onChange={e => {
                            setSortBy(e.target.value);
                            setPage(1);
                        }}
                    >
                        <option value="createdAt">Newest</option>
                        <option value="upvotes">Most upvotes</option>
                        <option value="downvotes">Most downvotes</option>
                    </Form.Select>
                </div>
            </div>

            {loadingQuestions ? (
                <LoadingSpinner />
            ) : (
                error ? <ErrorWrapper content="There was a problem while fetching qeustions" /> :
                    questionsPage?.questions.map(q => <QuestionCard key={q.id} question={q} />)
            )}

            <div className="d-flex justify-content-between mt-4">
                <Button
                    variant="outline-secondary"
                    disabled={isFirstPage}
                    onClick={() => setPage(p => p - 1)}
                >
                    Previous
                </Button>

                <Button
                    variant="outline-secondary"
                    disabled={isLastPage}
                    onClick={() => setPage(p => p + 1)}
                >
                    Next
                </Button>
            </div>
        </div>
    );
};

export default QuestionsPage;