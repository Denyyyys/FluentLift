import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext"
import { SlMagnifier } from "react-icons/sl";
import { BsToggleOff } from "react-icons/bs";
import { BsToggleOn } from "react-icons/bs";
import type { DeckOwnerPageResponseDto } from "../../types/deck";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { Link } from "react-router-dom";
import LoadingSpinner from "../../components/common/LoadingSpinner";
import ErrorWrapper from "../../components/common/ErrorWrapper";
import { Button } from "react-bootstrap";
import DeckOwnerCardList from "../../components/decks/DeckOwnerCardList";
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";


function MyDecksPage() {
    const { language } = useLanguage();
    const [showArchived, setShowArchived] = useState(false);
    const [decksPage, setDecksPage] = useState<DeckOwnerPageResponseDto | null>(null);
    const [page, setPage] = useState(1);
    const [isFirstPage, setIsFirstPage] = useState(true);
    const [isLastPage, setIsLastPage] = useState(true);

    const [loadingDecks, setLoadingDecks] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const { token } = useAuth();

    const loadDecks = async () => {
        try {
            setLoadingDecks(true);
            setError(null);

            const decksResponse = await axios.get<DeckOwnerPageResponseDto>(`${BACKEND_BASE_URL}/decks/me?&page=${page}`, {
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

    const handleShowArchivedClick = () => {
        setShowArchived(prev => !prev);
    };

    useEffect(() => {
        loadDecks()
    }, [page]);
    console.log(decksPage);

    return (
        <>
            <header className="d-flex flex-row justify-content-between align-items-end my-decks-header-container">
                <h2>{textByLanguage[language]["myDecks"]["titleText"]}</h2>
                <form className="d-flex flex-row align-items-center search-deck-form" onSubmit={(e) => {
                    e.preventDefault(); console.log("Form submitted");
                }}>
                    <input
                        type="text"
                        placeholder={textByLanguage[language]["myDecks"]["findTextHelpText"]}
                        className="search-deck-input"
                    />
                    <button type="submit" className="search-deck-icon-btn">
                        <SlMagnifier size={18} />
                    </button>
                </form>
                <div className="d-flex flex-row align-items-center gap-3">
                    <Link className="btn btn-success" to="/decks/new/">{textByLanguage[language]["myDecks"]["NewDeckText"]}</Link>
                    <div className="dropdown">
                        <button className="btn dropdown-toggle sort-by-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            {textByLanguage[language]["myDecks"]["SortByText"]}
                        </button>
                        <ul className="dropdown-menu">
                            <li><a className="dropdown-item" href="#">{textByLanguage[language]["myDecks"]["SortByNameText"]}</a></li>
                            <li><a className="dropdown-item" href="#">{textByLanguage[language]["myDecks"]["SortByDateText"]}</a></li>
                            <li><a className="dropdown-item" href="#">{textByLanguage[language]["myDecks"]["SortByCardsToLearnText"]}</a></li>
                            <li><a className="dropdown-item" href="#">{textByLanguage[language]["myDecks"]["SortByTotalNumberOfCardsText"]}</a></li>
                        </ul>
                    </div>
                </div>
            </header >
            <div className="d-flex align-items-center my-decks-header-container mt-3">
                <h4>{textByLanguage[language]["myDecks"]["ShowArchivedText"]}</h4>
                <button onClick={handleShowArchivedClick} className="toggle-btn">
                    {showArchived ? <BsToggleOn size={24} className="icon" /> : <BsToggleOff className="icon" size={24} />}
                </button>
            </div>
            {loadingDecks ? (
                <LoadingSpinner />
            ) : (
                error ? <ErrorWrapper content="There was a problem while fetching qeustions" /> :
                    <DeckOwnerCardList decks={decksPage?.decks ?? []} showArchived={showArchived} />
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
        </>
    )
}

export default MyDecksPage