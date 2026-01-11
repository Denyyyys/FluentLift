import { useNavigate } from "react-router-dom";
import type { DeckOwnerResponseDto } from "../../types/deck";
import { IoMdSettings } from "react-icons/io";

interface DeckOwnerCardListItemProp {
    deck: DeckOwnerResponseDto;
}


function DeckOwnerCardListItem({ deck }: DeckOwnerCardListItemProp) {
    const navigate = useNavigate();
    const unseenCount = deck.cards.filter(card => card.firstReviewDate === null && !card.archived).length;
    const toLearnCount = deck.cards.filter(card => {
        const now = new Date();
        const nextReviewDate = new Date(card.nextReviewDate);
        return (nextReviewDate <= now) && !card.archived
    }).length;
    const totalCount = deck.cards.filter(card => !card.archived).length;

    return (
        <tr key={deck.id} className='deck-card' onClick={() => navigate(`/profile/decks/${deck.id}/study`)}
        >
            <th>{deck.name}</th>
            <td>{unseenCount}</td>
            <td>{toLearnCount}</td>
            <td>{totalCount}</td>
            <td>
                <button className='toggle-btn' onClick={(e) => {
                    e.stopPropagation();
                    navigate(`/profile/decks/${deck.id}/settings`);
                }}>
                    <IoMdSettings className="icon" size={20} style={{ alignSelf: 'center' }} />
                </button>
            </td>
        </tr>
    )
}

export default DeckOwnerCardListItem