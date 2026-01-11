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
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";

function QuestionsPage() {
    const { language } = useLanguage();
    const [query, setQuery] = useState("");
    const [debouncedQuery, setDebouncedQuery] = useState("");
    const [sortBy, setSortBy] = useState("createdAt");
    const [onlySolved, setOnlySolved] = useState(false);
    const [page, setPage] = useState(1);

    const [questionsPage, setQuestionsPage] = useState<QuestionsPageResponseDto | null>(null);
    const [isFirstPage, setIsFirstPage] = useState(true);
    const [isLastPage, setIsLastPage] = useState(true);

    const [loadingQuestions, setLoadingQuestions] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const { token } = useAuth();
    const debounceDelayMs = 500;

    const loadQuestions = async () => {
        try {
            setLoadingQuestions(true);
            setError(null);

            const questionsResponse = await axios.get<QuestionsPageResponseDto>(`${BACKEND_BASE_URL}/qa/questions?query=${debouncedQuery}&isSolved=${onlySolved}&sortBy=${sortBy}&page=${page}`, {
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
    }, [debouncedQuery, sortBy, page, onlySolved]);

    // const handleSearch = () => {
    //     setPage(1);
    //     loadQuestions();
    // };
    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebouncedQuery(query);
            setPage(1);
        }, debounceDelayMs);

        return () => clearTimeout(timeout);
    }, [query]);

    return (
        <div className="container mt-4">
            <div className="row mb-4 align-items-center">
                <div className="col-md-6">
                    <div className="input-group">
                        <input
                            className="form-control"
                            placeholder={textByLanguage[language]["allQuestions"]["searchByTitleOrContentText"]}
                            value={query}
                            onChange={e => setQuery(e.target.value)}
                        />
                        {/* <Button variant="primary" onClick={handleSearch}>
                            <FaSearch />
                        </Button> */}
                    </div>
                </div>

                <div className="col-md-2 d-flex align-items-center">
                    <Form.Check
                        type="checkbox"
                        label={textByLanguage[language]["allQuestions"]["solvedOnlyCheckboxText"]}
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
                        <option value="createdAt">{textByLanguage[language]["allQuestions"]["sortByNewestText"]}</option>
                        <option value="upvotes">{textByLanguage[language]["allQuestions"]["sortByMostUpvotesText"]}</option>
                        <option value="downvotes">{textByLanguage[language]["allQuestions"]["sortByMostDownvotesText"]}</option>
                    </Form.Select>
                </div>
            </div>

            {loadingQuestions ? (
                <LoadingSpinner />
            ) : (
                error ? <ErrorWrapper content="There was a problem while fetching qeustions" /> :
                    <div className="stripped-container">
                        {questionsPage?.questions.map(q => <QuestionCard key={q.id} question={q} />)}
                    </div>
            )}

            <div className="d-flex justify-content-between mt-4">
                <Button
                    variant="outline-secondary"
                    disabled={isFirstPage}
                    onClick={() => setPage(p => p - 1)}
                >
                    {textByLanguage[language]["pagination"]["previousText"]}
                </Button>

                <Button
                    variant="outline-secondary"
                    disabled={isLastPage}
                    onClick={() => setPage(p => p + 1)}
                >
                    {textByLanguage[language]["pagination"]["nextText"]}
                </Button>
            </div>
        </div>
    );
};

export default QuestionsPage;