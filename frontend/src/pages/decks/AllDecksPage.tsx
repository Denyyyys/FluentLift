import { useEffect, useState } from "react"
import type { DeckOwnerResponseDto, DeckVisitorPageResponseDto } from "../../types/deck";
import { useAuth } from "../../context/AuthContext";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import LoadingSpinner from "../../components/common/LoadingSpinner";
import ErrorWrapper from "../../components/common/ErrorWrapper";
import { Button, Form } from "react-bootstrap";
import DeckVisitorCardList from "../../components/decks/DeckVisitorCardList";
import { toast } from "react-toastify";
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";

function AllDecksPage() {
    const { language } = useLanguage();

    const [query, setQuery] = useState("");
    const [debouncedQueryName, setDebouncedQueryName] = useState("");
    const [sortBy, setSortBy] = useState("createdAt");
    const [page, setPage] = useState(1);
    const [decksPage, setDecksPage] = useState<DeckVisitorPageResponseDto | null>(null);
    const [isFirstPage, setIsFirstPage] = useState(true);
    const [isLastPage, setIsLastPage] = useState(true);
    const [loadingDecks, setLoadingDecks] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const { token } = useAuth();
    const debounceDelayMs = 500;

    const loadDecks = async () => {
        try {
            setLoadingDecks(true);
            setError(null);

            const decksResponse = await axios.get<DeckVisitorPageResponseDto>(`${BACKEND_BASE_URL}/decks?name=${debouncedQueryName}&sortBy=${sortBy}&page=${page}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            setDecksPage(decksResponse.data);
            setIsFirstPage(decksResponse.data.page == 1);
            setIsLastPage(decksResponse.data.page == decksResponse.data.totalPages);

        } catch (error) {
            console.error(`Axios Error while getting decks:`, error);
            setError("Error while getting decks");
        } finally {
            setLoadingDecks(false);
        }
    };

    useEffect(() => {
        loadDecks();
    }, [debouncedQueryName, sortBy, page])

    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebouncedQueryName(query);
            setPage(1);
        }, debounceDelayMs);

        return () => clearTimeout(timeout);
    }, [query]);

    const copyDeck = async (deckId: number) => {
        try {
            await axios.post<DeckOwnerResponseDto>(`${BACKEND_BASE_URL}/decks/${deckId}/copy`, undefined, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            toast.success("Successfully copied deck!")
        } catch (e) {
            toast.error("There were error during copying deck")
            console.log(e);
        }
    }

    return (
        <div className="container mt-4">
            <div className="row mb-4 align-items-center">
                <div className="col-md-6">
                    <div className="input-group">
                        <input
                            className="form-control"
                            placeholder={textByLanguage[language]["browseDecks"]["inputFieldText"]}
                            value={query}
                            onChange={e => setQuery(e.target.value)}
                        />
                    </div>
                </div>

                <div className="col-md-3 ms-auto">
                    <Form.Label className="mb-1 text-muted">
                        {textByLanguage[language]["browseDecks"]["sortDecksByText"]}
                    </Form.Label>
                    <Form.Select
                        value={sortBy}
                        onChange={e => {
                            setSortBy(e.target.value);
                            setPage(1);
                        }}
                    >
                        <option value="createdAt">{textByLanguage[language]["browseDecks"]["sortDecksByNewestText"]}</option>
                        <option value="name">{textByLanguage[language]["browseDecks"]["sortDecksByNameText"]}</option>
                    </Form.Select>
                </div>
            </div>

            {loadingDecks ? (
                <LoadingSpinner />
            ) : (
                error ? <ErrorWrapper content="There was a problem while fetching decks" /> :
                    <DeckVisitorCardList decks={decksPage?.decks ?? []} copyDeck={copyDeck} />
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
}

export default AllDecksPage