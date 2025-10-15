import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext"
import { SlMagnifier } from "react-icons/sl";
import { BsToggleOff } from "react-icons/bs";
import { BsToggleOn } from "react-icons/bs";
import type { DeckOwnerResponseDto } from "../../types/deck";
import axios from "axios";
import DeckCardList from "../../components/decks/DeckCardList";
import { BACKEND_BASE_URL } from "../../constants";


function MyDecksPage() {
    const [showArchived, setShowArchived] = useState(false);
    const [decks, setDecks] = useState<DeckOwnerResponseDto[]>([]);
    const { token } = useAuth();
    const handleShowArchivedClick = () => {
        setShowArchived(prev => !prev);
    };

    useEffect(() => {
        axios
            .get(`${BACKEND_BASE_URL}/decks`, {
                headers: {
                    Authorization: "Bearer " + token,
                },
            })
            .then((response) => {
                setDecks(response.data);
            })
            .catch((error) => {
                console.error("Error fetching decks:", error);
            });
    }, []);

    return (
        <>
            <header className="d-flex flex-row justify-content-between align-items-end my-decks-header-container">
                <h2>My Decks</h2>
                <form className="d-flex flex-row align-items-center search-deck-form" onSubmit={(e) => {
                    e.preventDefault(); console.log("Form submitted");
                }}>
                    <input
                        type="text"
                        placeholder="Find Deck"
                        className="search-deck-input"
                    />
                    <button type="submit" className="search-deck-icon-btn">
                        <SlMagnifier size={18} />
                    </button>
                </form>
                <div className="d-flex flex-row align-items-center gap-3">
                    <button className="btn btn-success">New</button>
                    <div className="dropdown">
                        <button className="btn dropdown-toggle sort-by-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Sort By
                        </button>
                        <ul className="dropdown-menu">
                            <li><a className="dropdown-item" href="#">Name</a></li>
                            <li><a className="dropdown-item" href="#">Date</a></li>
                            <li><a className="dropdown-item" href="#">Cards To Learn</a></li>
                            <li><a className="dropdown-item" href="#">Total Number Of Cards</a></li>
                        </ul>
                    </div>
                </div>
            </header >
            <div className="d-flex align-items-center my-decks-header-container mt-3">
                <h4>Show Archived</h4>
                <button onClick={handleShowArchivedClick} className="toggle-btn">
                    {showArchived ? <BsToggleOn size={24} className="icon" /> : <BsToggleOff className="icon" size={24} />}
                </button>
            </div>
            <DeckCardList decks={decks} showArchived={showArchived} />
        </>
    )
}

export default MyDecksPage