import type { CardOwnerResponseDto, CardVisitorResponseDto, CardCreateDto } from "./card";
import type { DeckCreatorDto } from "./user";


export interface DeckOwnerResponseDto {
    id: number;
    name: string;
    creator: DeckCreatorDto;
    archived: boolean;
    targetLanguage: string;
    baseLanguage: string;
    public: boolean;

    cards: CardOwnerResponseDto[];
}


export interface DeckVisitorResponseDto {
    id: number;
    name: string;
    creator: DeckCreatorDto;
    archived: boolean;
    targetLanguage: string;
    baseLanguage: string;
    public: boolean;

    cards: CardVisitorResponseDto[];
}

export interface DeckCreateDto {
    name: string;
    targetLanguage: string;
    baseLanguage: string;
    isPublic: boolean;
    cards: CardCreateDto[];
}

