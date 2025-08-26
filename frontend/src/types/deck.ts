import type { CardOwnerResponseDto, CardVisitorResponseDto } from "./card";
import type { DeckCreatorDto } from "./user";



export interface DeckOwnerResponseDto {
    id: number;
    name: string;
    creator: DeckCreatorDto;
    archived: boolean;
    targetLanguage: string;
    learningLanguage: string;
    public: boolean;

    cards: CardOwnerResponseDto[];
}


export interface DeckVisitorResponseDto {
    id: number;
    name: string;
    creator: DeckCreatorDto;
    archived: boolean;
    targetLanguage: string;
    learningLanguage: string;
    public: boolean;

    cards: CardVisitorResponseDto[];
}
