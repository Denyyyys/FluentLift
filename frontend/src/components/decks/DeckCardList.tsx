import { useNavigate } from 'react-router-dom';
import type { DeckOwnerResponseDto } from '../../types/deck';
import { IoMdSettings } from "react-icons/io";

interface DeckListProps {
    decks: DeckOwnerResponseDto[];
    showArchived: boolean;
}

function DeckCardList({ decks, showArchived }: DeckListProps) {
    const filteredDecks = showArchived
        ? decks
        : decks.filter(deck => !deck.archived);

    const navigate = useNavigate();

    return (
        <table className="table table-striped">
            <thead>
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">New Cards</th>
                    <th scope="col">To Learn</th>
                    <th scope="col">Total</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                {filteredDecks.map((deck) => {
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
                })}
            </tbody>
        </table>
    )
}

export default DeckCardList