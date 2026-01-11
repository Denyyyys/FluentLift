import { textByLanguage } from "../../assets/translations";
import { useLanguage } from "../../hooks/useLanguage";
import type { DeckVisitorResponseDto } from "../../types/deck";
import DeckVisitorCardListItem from "./DeckVisitorCardListItem";


interface DeckVisitorCardListProps {
    decks: DeckVisitorResponseDto[];
    copyDeck: (deckId: number) => void;
}

function DeckVisitorCardList({ decks, copyDeck }: DeckVisitorCardListProps) {
    const { language } = useLanguage();

    return (
        <table className="table table-striped">
            <thead>
                <tr>
                    <th scope="col">{textByLanguage[language]["browseDecks"]["FindDeckTableHeaderNameText"]}</th>
                    <th scope="col">{textByLanguage[language]["browseDecks"]["FindDeckTableHeaderSourceLangText"]}</th>
                    <th scope="col">{textByLanguage[language]["browseDecks"]["FindDeckTableHeaderTargetLangText"]}</th>
                    <th scope="col">{textByLanguage[language]["browseDecks"]["FindDeckTableHeaderTotalText"]}</th>
                    <th scope="col">{textByLanguage[language]["browseDecks"]["FindDeckTableHeaderOwnedByText"]}</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                {decks.map((deck) =>
                    <DeckVisitorCardListItem deck={deck} copyDeck={copyDeck} />
                )}
            </tbody>
        </table>
    )
}

export default DeckVisitorCardList