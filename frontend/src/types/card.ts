
export interface CardOwnerResponseDto {
    id: number;
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

