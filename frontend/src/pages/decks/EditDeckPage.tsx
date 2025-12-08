import { useEffect, useState, type FormEvent } from "react";
import { useAuth } from "../../context/AuthContext"
import type { DeckOwnerResponseDto } from "../../types/deck";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { useParams } from "react-router-dom";
import LoadingSpinner from "../../components/common/LoadingSpinner";
import UnreachableState from "../../components/common/UnreachableState";
import { AiOutlineGlobal } from "react-icons/ai";
import { AiOutlineEyeInvisible } from "react-icons/ai";
import { AiOutlineEye } from "react-icons/ai";
import { IoIosLock } from "react-icons/io";
import type { CardOwnerResponseDto } from "../../types/card";
import { v4 as uuid } from "uuid";
import UpdateCardForm from "../../components/decks/UpdateCardForm";

function EditDeckPage() {
    const possibleLanguageOptions = ["English", "Polish", "Ukrainian", "German"];

    const [deck, setDeck] = useState<null | DeckOwnerResponseDto>(null)
    const [loadingDeck, setLoadingDeck] = useState(true);

    const { token, userId } = useAuth();

    const { deckId } = useParams<{ deckId: string }>();


    if (deckId === undefined) {
        return <UnreachableState />
    }

    const submitEditDeck = async (e: FormEvent) => {
        e.preventDefault();
        if (deck === null) {
            console.error("it should never happen");
            return
        }
        console.log("submit form");
        console.log(deck);
        const updatedDeckResponse = await axios.put<DeckOwnerResponseDto>(`${BACKEND_BASE_URL}/decks/${deck.id}`, deck, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        console.log("response:");

        console.log(updatedDeckResponse);

    }

    const updateCard = (updatedNewCard: CardOwnerResponseDto) => {
        setDeck(prev => {
            if (!prev) {
                return prev;
            }
            return {
                ...prev,
                cards: prev.cards.map(newCard =>
                    newCard.tempId === updatedNewCard.tempId ? updatedNewCard : newCard
                )
            }
        });
    }

    const deleteCard = (tempId: string) => {
        setDeck((prev) => {
            if (!prev) {
                return prev;
            }
            return { ...prev, cards: prev.cards.filter((newCard) => newCard.tempId !== tempId) }
        })
    }

    const fetchDeck = async () => {
        try {
            setLoadingDeck(true);
            const fetchedDeck = await axios.get<DeckOwnerResponseDto>(`${BACKEND_BASE_URL}/decks/${deckId}`, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });
            const tempDeck = fetchedDeck.data;

            tempDeck.cards = tempDeck.cards.map(card => { return { ...card, tempId: uuid() } })
            setDeck(tempDeck)
        } catch (e) {
            console.error(e);
            console.log("Failed to fetch deck :(");
        } finally {
            setLoadingDeck(false)
        }
    }

    const handleAddCard = () => {
        setDeck((prev) => {
            if (!prev) {
                return prev;
            }
            return {
                ...prev,
                cards: [...prev.cards, {
                    id: null, backText: "", frontText: "", tempId: uuid(), archived: false, consecutiveCorrect: 0, firstReviewDate: new Date().toString(), intervalDays: 1, nextReviewDate: new Date().toString()
                } as CardOwnerResponseDto]
            }
        })
    }

    useEffect(() => {
        fetchDeck()
    }, [])

    if (loadingDeck) {
        return <LoadingSpinner />
    }

    console.log(deck);
    if (deck === null) {
        return <UnreachableState />
    }

    if (deck.creator.id !== userId) {
        return <div>You can edit only your decks! </div>
    }

    return (
        <>
            <h3 className='mb-3 pb-1 fw-semibold header-with-line-bottom'>Edit Deck</h3>
            <form onSubmit={async (e) => await submitEditDeck(e)}>
                <div className='mb-3'>
                    <button type='button' className='btn btn-primary' onClick={() => setDeck((prev) => {
                        if (!prev) {
                            return prev;
                        } return {
                            ...prev,
                            public: !prev.public
                        }
                    }
                    )} >
                        {
                            deck.public ?
                                <>  <AiOutlineGlobal /> <span>Public</span></> :
                                <>  <IoIosLock /> Private </>
                        }
                    </button>
                </div>
                <div className='mb-3'>
                    <button type='button' className='btn btn-primary' onClick={() => setDeck((prev) => {
                        if (!prev) {
                            return prev;
                        } return {
                            ...prev,
                            archived: !prev.archived
                        }
                    }
                    )} >
                        {
                            deck.archived ?
                                <>  <AiOutlineEyeInvisible /> <span>Archived</span></> :
                                <>  <AiOutlineEye /> Not Archived </>
                        }
                    </button>
                </div>

                <div className="mb-3">
                    <label htmlFor="deckName" className="form-label">Deck Name</label>
                    <input type="text" className="form-control" id="deckName" value={deck.name} onChange={(e) => setDeck((prev) => {
                        if (!prev) {
                            return prev;
                        } return {
                            ...prev,
                            name: e.target.value
                        }
                    }
                    )} />
                </div>


                <div className="mb-3">
                    <label htmlFor="baseLanguage" className="form-label">Base Language <small>(Language You know)</small></label>

                    <div className="dropwodn">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="baseLanguage"
                            aria-expanded="false">{deck.baseLanguage}
                        </button>
                        <ul className="dropdown-menu">
                            {possibleLanguageOptions.map((option) => (
                                <li key={option}>
                                    <button type='button' className="dropdown-item" onClick={() => setDeck((prev) => {
                                        if (!prev) {
                                            return prev;
                                        } return {
                                            ...prev,
                                            baseLanguage: option
                                        }
                                    }
                                    )}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>

                <div className="mb-3">
                    <label htmlFor="targetLanguage" className="form-label">Target Language <small>(Language You will learn)</small></label>

                    <div className="dropwodn ">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="targetLanguage"
                            aria-expanded="false">{deck.targetLanguage}
                        </button>
                        <ul className="dropdown-menu">
                            {possibleLanguageOptions.map((option) => (
                                <li key={option}>
                                    <button type='button' className="dropdown-item" onClick={() => setDeck((prev) => {
                                        if (!prev) {
                                            return prev;
                                        } return {
                                            ...prev,
                                            targetLanguage: option
                                        }
                                    }
                                    )}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>


                <div className="new-cards-container mb-3 stripped-container">
                    {deck.cards.map(newCard => <UpdateCardForm key={newCard.id} newCard={newCard} updateCard={updateCard} deleteCard={deleteCard} />
                    )}
                </div>

                <button type='button' className='btn btn-primary mt-3 mb-3' onClick={handleAddCard}>Add New Card</button>
                <div></div>
                <button type='submit' className='btn btn-success'>Update Deck</button>
            </form >
        </>
    )
}

export default EditDeckPage