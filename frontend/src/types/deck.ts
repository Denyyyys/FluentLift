import type { CardOwnerResponseDto, CardVisitorResponseDto, CardCreateDto } from "./card";
import type { AppUserResponseDto } from "./user";


export interface DeckOwnerResponseDto {
    id: number;
    name: string;
    creator: AppUserResponseDto;
    archived: boolean;
    targetLanguage: string;
    baseLanguage: string;
    public: boolean;
    cards: CardOwnerResponseDto[];
}


export interface DeckVisitorResponseDto {
    id: number;
    name: string;
    creator: AppUserResponseDto;
    archived: boolean;
    targetLanguage: string;
    baseLanguage: string;
    public: boolean;
    cards: CardVisitorResponseDto[];
}

export interface DeckVisitorPageResponseDto {
    decks: DeckVisitorResponseDto[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}

export interface DeckOwnerPageResponseDto {
    decks: DeckOwnerResponseDto[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}

export interface DeckCreateDto {
    name: string;
    targetLanguage: string;
    baseLanguage: string;
    isPublic: boolean;
    cards: CardCreateDto[];
}

