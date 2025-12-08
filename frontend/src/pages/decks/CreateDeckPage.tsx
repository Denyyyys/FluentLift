import { useState, type FormEvent } from 'react'
import { AiOutlineGlobal } from "react-icons/ai";
import { IoIosLock } from "react-icons/io";
import type { CardCreateDto } from '../../types/card';
import CardForm from '../../components/decks/CardForm';
import { v4 as uuid } from "uuid";
import type { DeckCreateDto } from '../../types/deck';
import axios from 'axios';
import { BACKEND_BASE_URL } from '../../constants';
import { useAuth } from '../../context/AuthContext';
import { toast } from "react-toastify";
import { useNavigate } from 'react-router-dom';

function CreateDeckPage() {
    const possibleLanguageOptions = ["English", "Polish", "Ukrainian", "German"];

    const [isPublic, setIsPublic] = useState(true);
    const [deckName, setDeckName] = useState("");
    const [baseLanguage, setBaseLanguage] = useState(possibleLanguageOptions[1]);
    const [targetLanguage, setTargetLanguage] = useState(possibleLanguageOptions[0]);
    const [newCards, setNewCards] = useState([] as CardCreateDto[])

    const { token } = useAuth();
    const navigate = useNavigate();

    const submitCreateDeck = async (e: FormEvent) => {
        e.preventDefault();
        const newDeck: DeckCreateDto = {
            isPublic,
            targetLanguage,
            baseLanguage,
            name: deckName,
            cards: newCards
        }
        try {
            const response = await axios.post(
                `${BACKEND_BASE_URL}/decks`,
                newDeck,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                }
            )

            toast.success("Successfully created new deck!")
            console.log(response.data);
            navigate("/profile/decks/");
        } catch (error) {
            toast.error("Something went wrong. Please check your logs")
            console.error("Error while posting data with new deck:", error);
        }
    }

    const handleAddCard = () => {
        setNewCards((prev) => {
            return [...prev, { id: uuid(), backText: "", frontText: "" }]
        })
    }

    const updateCard = (updatedNewCard: CardCreateDto) => {
        setNewCards(prev =>
            prev.map(newCard =>
                newCard.id === updatedNewCard.id ? updatedNewCard : newCard
            )
        );
    }

    const deleteCard = (id: string) => {
        setNewCards(prev => {
            return prev.filter((newCard) => newCard.id !== id)
        })
    }

    return (
        <>
            <h3 className='mb-3 pb-1  fw-semibold header-with-line-bottom'>Create New Deck</h3>
            <form onSubmit={(e) => submitCreateDeck(e)}>
                <div className='mb-3'>
                    <button type='button' className='btn btn-primary' onClick={() => setIsPublic(!isPublic)} >
                        {
                            isPublic ?
                                <>  <AiOutlineGlobal /> <span>Public</span></> :
                                <>  <IoIosLock /> Private </>
                        }
                    </button>
                </div>

                <div className="mb-3">
                    <label htmlFor="deckName" className="form-label">Deck Name</label>
                    <input type="text" className="form-control" id="deckName" value={deckName} onChange={(e) => setDeckName(e.target.value)} />
                </div>


                <div className="mb-3">
                    <label htmlFor="baseLanguage" className="form-label">Base Language <small>(Language You know)</small></label>

                    <div className="dropwodn">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="baseLanguage"
                            aria-expanded="false">{baseLanguage}
                        </button>
                        <ul className="dropdown-menu">
                            {possibleLanguageOptions.map((option) => (
                                <li key={option}>
                                    <button type='button' className="dropdown-item" onClick={() => setBaseLanguage(option)}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>

                <div className="mb-3">
                    <label htmlFor="targetLanguage" className="form-label">Target Language <small>(Language You will learn)</small></label>

                    <div className="dropwodn ">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="targetLanguage"
                            aria-expanded="false">{targetLanguage}
                        </button>
                        <ul className="dropdown-menu">
                            {possibleLanguageOptions.map((option) => (
                                <li key={option}>
                                    <button type='button' className="dropdown-item" onClick={() => setTargetLanguage(option)}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>


                <div className="new-cards-container mb-3 stripped-container">
                    {newCards.map(newCard => <CardForm key={newCard.id} newCard={newCard} updateCard={updateCard} deleteCard={deleteCard} />
                    )}
                </div>

                <button type='button' className='btn btn-primary mt-3 mb-3' onClick={handleAddCard}>Add New Card</button>
                <div></div>
                <button type='submit' className='btn btn-success'>Create New Deck</button>
            </form>
        </>
    )
}

export default CreateDeckPage