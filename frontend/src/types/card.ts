
export interface CardOwnerResponseDto {
    id: number | null;
    tempId: string;
    frontText: string;
    backText: string;
    archived: boolean;
    nextReviewDate: string; // ISO date string from backend
    firstReviewDate: string; // ISO date string from backend
    intervalDays: number;
    consecutiveCorrect: number;
}

export interface CardVisitorResponseDto {
    id: number;
    frontText: string;
    backText: string;
    archived: boolean;
}

export interface CardCreateDto {
    id: string;
    frontText: string;
    backText: string;
}
