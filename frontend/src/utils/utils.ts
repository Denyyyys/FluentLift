import type { CardOwnerResponseDto } from "../types/card";

export function sortByNextReviewDate(cards: CardOwnerResponseDto[]) {
    return [...cards].sort(
        (a, b) =>
            new Date(a.nextReviewDate).getTime() - new Date(b.nextReviewDate).getTime()
    );
}