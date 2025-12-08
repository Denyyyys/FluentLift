import { useParams } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useEffect, useState } from "react";
import axios from "axios";
import type { DeckOwnerResponseDto } from "../../types/deck";
import { sortByNextReviewDate } from "../../utils/utils";
import type { CardOwnerResponseDto } from "../../types/card";
import { BsToggleOff } from "react-icons/bs";
import { BsToggleOn } from "react-icons/bs";
import { BACKEND_BASE_URL } from "../../constants";
import LoadingSpinner from "../../components/common/LoadingSpinner";

function DeckStudyPage() {
    const { deckId } = useParams<{ deckId: string }>();
    const { token } = useAuth();

    const [index, setIndex] = useState(0);
    const [loadingDeck, setLoadingDeck] = useState(true);
    const [mode, setMode] = useState("review");
    const [deck, setDeck] = useState<DeckOwnerResponseDto | undefined>(undefined);
    const [cardIsFlipped, setCardIsFlipped] = useState(false);
    const [currentCard, setCurrentCard] = useState<CardOwnerResponseDto | null>(null);

    const fetchDeck = async () => {
        try {
            setLoadingDeck(true);
            const response = await axios.get<DeckOwnerResponseDto>(`${BACKEND_BASE_URL}/decks/${deckId}`, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });
            let fetchedDeck = response.data;
            fetchedDeck.cards = sortByNextReviewDate(fetchedDeck.cards);
            setDeck(fetchedDeck);
        } catch (e) {
            console.error(e);
            console.log("Failed to fetch deck :(");
        } finally {
            setLoadingDeck(false)
        }
    }

    useEffect(() => {
        fetchDeck();
    }, [])


    const activeCards = deck ? deck.cards.filter(card => !card.archived) : [];
    const reviewCards = activeCards.filter(card => new Date(card.nextReviewDate) <= new Date());

    useEffect(() => {
        if (mode === "review") {
            setCurrentCard(reviewCards[index] ?? null);
        } else {
            if (activeCards.length > 0) {
                setCurrentCard(prev => prev ?? activeCards[Math.floor(Math.random() * activeCards.length)]);
            }
        }
    }, [index, mode, activeCards, reviewCards]);

    if (loadingDeck) {
        return <LoadingSpinner />
    }

    if (deck === undefined) {
        return <p>No Deck found!</p>
    }


    // console.log("rerender");
    // console.log(activeCards);
    // console.log(reviewCards);

    const handleKnewIt = async () => {
        await updateCardProgress(token, currentCard, true);
        await fetchDeck();
        if (mode === "review") {
            setIndex(0);
        } else if (mode === "random" && activeCards && activeCards.length > 0) {
            setCurrentCard(activeCards[Math.floor(Math.random() * activeCards.length)]);
        }
        setCardIsFlipped(false);
    }

    const handleDidntKnowIt = async () => {
        await updateCardProgress(token, currentCard, false);
        await fetchDeck();
        if (mode === "review") {
            setIndex(0);
        } else if (mode === "random" && activeCards && activeCards.length > 0) {
            setCurrentCard(activeCards[Math.floor(Math.random() * activeCards.length)]);
        }
        setCardIsFlipped(false);
    }

    const handleSkip = () => {
        if (mode === "review") {
            setIndex(prev => prev + 1);
        } else if (mode === "random" && activeCards && activeCards.length > 0) {
            setCurrentCard(activeCards[Math.floor(Math.random() * activeCards.length)]);
        }
        setCardIsFlipped(false);
    }

    const toggleRandomMode = () => {
        if (mode === "review") {
            setMode("random");
        } else {
            setMode("review");
        }
    }

    const handleFlip = () => {
        setCardIsFlipped(prev => !prev);
    }

    console.log("==========================");

    console.log(reviewCards?.length);
    console.log(mode);
    console.log(reviewCards?.length !== 0 || mode !== "review");
    console.log("==========================");


    return (
        <div>
            <header className="d-flex justify-content-between align-items-center mb-3">
                <h2>{deck.name}</h2>
                <button className="btn btn-success">Add Card</button>
            </header>
            <button onClick={toggleRandomMode} className="btn btn-primary mb-5">
                {mode === "review" ? <> Random Cards < BsToggleOff size={24} className="icon icon-white" /> </> : <>Random Cards <BsToggleOn className="icon icon-white" size={24} /> </>}
            </button>
            <div className="card-container d-flex flex-column">
                <div className="card-top">
                    {activeCards?.length >= 0 && reviewCards?.length === 0 && mode === "review" &&
                        <h5 className="pt-5 centered-paragraph">Congartulations, learning is finished! Please come back later, when it's time to study or continue learning by enabling "Random Cards" mode.</h5>}
                    <h1 className="pt-5">{cardIsFlipped ? currentCard?.backText : currentCard?.frontText}</h1>

                </div>
                <div>
                    {
                        reviewCards !== undefined && reviewCards?.length !== 0 && mode === "review" ? (
                            <p className="mt-5">
                                Card {index + 1} of {reviewCards?.length}
                            </p>
                        ) : (
                            <p className="mt-5">
                                Total number of Cards: {activeCards?.length}
                            </p>
                        )
                    }
                </div>
                {(reviewCards?.length !== 0 || mode !== "review") &&
                    <div className="card-btn-bottom-container mb-5 d-flex justify-content-center gap-3">
                        {cardIsFlipped && <>
                            <button className="btn btn-primary" onClick={handleKnewIt}>Knew it</button>
                            <button className="btn btn-primary" onClick={handleDidntKnowIt}>Didn't know it</button>
                        </>}
                        <button className="btn btn-primary" onClick={handleFlip}>Flip</button>
                        <button className="btn btn-primary" onClick={handleSkip}>Skip</button>
                        {cardIsFlipped && <>


                            <div className="dropdown">
                                <button className="btn dropdown-toggle btn-primary" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    More
                                </button>
                                <ul className="dropdown-menu">
                                    <li><a className="dropdown-item" href="#">Edit</a></li>
                                    <li><a className="dropdown-item" href="#">Delete</a></li>
                                </ul>
                            </div>
                        </>
                        }
                    </div>
                }
            </div>


        </div >
    )
}

async function updateCardProgress(token: string | null, card: CardOwnerResponseDto | null, knewIt: boolean) {
    if (card === null) {
        return
    }
    try {
        await axios.put(
            `${BACKEND_BASE_URL}/cards/${card.id}/progress`,
            { knewIt },
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );
    } catch (err) {
        console.error("Failed to update card progress", err);
    }
}

export default DeckStudyPage;