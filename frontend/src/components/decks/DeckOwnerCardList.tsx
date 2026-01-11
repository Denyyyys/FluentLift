import { textByLanguage } from '../../assets/translations';
import { useLanguage } from '../../hooks/useLanguage';
import type { DeckOwnerResponseDto } from '../../types/deck';
import DeckOwnerCardListItem from './DeckOwnerCardListItem';

interface DeckOwnerCardListProps {
    decks: DeckOwnerResponseDto[];
    showArchived: boolean;
}

function DeckOwnerCardList({ decks, showArchived }: DeckOwnerCardListProps) {
    const { language } = useLanguage();

    const filteredDecks = showArchived
        ? decks
        : decks.filter(deck => !deck.archived);


    return (
        <table className="table table-striped">
            <thead>
                <tr>
                    <th scope="col">{textByLanguage[language]["myDecks"]["DeckTableHeaderNameText"]}</th>
                    <th scope="col">
                        {textByLanguage[language]["myDecks"]["DeckTableHeaderNewCardsText"]}</th>
                    <th scope="col">{textByLanguage[language]["myDecks"]["DeckTableHeaderToLearnText"]}</th>
                    <th scope="col">
                        {textByLanguage[language]["myDecks"]["DeckTableHeaderTotalText"]}</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                {filteredDecks.map((deck) =>
                    <DeckOwnerCardListItem deck={deck} key={deck.id} />
                )}
            </tbody>
        </table>
    )
}

export default DeckOwnerCardList