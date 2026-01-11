import { useNavigate } from "react-router-dom";
import type { DeckVisitorResponseDto } from "../../types/deck";
import { FaCopy } from "react-icons/fa";

interface DeckVisitorCardListItemProps {
    deck: DeckVisitorResponseDto;
    copyDeck: (deckId: number) => void;
}

function DeckVisitorCardListItem({ deck, copyDeck }: DeckVisitorCardListItemProps) {
    const navigate = useNavigate();

    return (
        <tr key={deck.id} className='deck-card' onClick={() => navigate(`/profile/decks/${deck.id}/study`)}
        >
            <th>{deck.name}</th>
            <td>{deck.baseLanguage}</td>
            <td>{deck.targetLanguage}</td>
            <td>{deck.cards.length}</td>
            <td>{deck.creator.name}</td>
            <td>
                <button className='toggle-btn' onClick={(e) => {
                    e.stopPropagation();
                    copyDeck(deck.id)
                }}>
                    <FaCopy className="icon" size={20} style={{ alignSelf: 'center' }} />
                </button>
            </td>
        </tr>
    )
}

export default DeckVisitorCardListItem