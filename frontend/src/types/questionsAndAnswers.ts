import type { AppUserResponseDto } from "./user";

export interface QuestionResponseDto {
    id: number;
    title: string;
    content: string;
    targetLanguage: string;
    baseLanguage: string;
    author: AppUserResponseDto;
    views: number;
    solved: boolean;
    createdAt: string; // ISO string
    updatedAt: string; // ISO string
    upvotes: number;
    downvotes: number;
    tags: string[];
}

export interface QuestionsPageResponseDto {
    questions: QuestionResponseDto[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}