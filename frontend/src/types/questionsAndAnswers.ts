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

export interface AnswerThreadDto {
    id: number;
    content: string;
    author: AppUserResponseDto;
    upvotes: number;
    downvotes: number;
    accepted: boolean;
    createdAt: string;
    updatedAt: string;
    replies: AnswerThreadDto[];
}

export interface QuestionWithAnswersDto {
    id: number;
    title: string;
    content: string;
    targetLanguage: string;
    baseLanguage: string;
    author: AppUserResponseDto;
    views: number;
    solved: boolean;
    createdAt: string;
    updatedAt: string;
    upvotes: number;
    downvotes: number;
    tags: string[];
    answers: AnswerThreadDto[];
}

export interface AnswerCreateRequestDto {
    content: string;
    parentAnswerId?: number;
}