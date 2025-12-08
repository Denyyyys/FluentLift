export enum BlockType {
    Text = "text",
    BigHeading = "bigHeading",
    SmallHeading = "smallHeading",
    Cloze = "cloze",
    Matching = "matching",
    MultipleChoice = "multipleChoice",
    ParagraphBlock = "paragraph",
}

export interface BaseBlock {
    id: string;
    type: BlockType;
    blockNumber: number;
}

export interface BigHeadingBlock extends BaseBlock {
    type: BlockType.Text;
    textBlockType: BlockType.BigHeading;
    text: string;
}

export interface SmallHeadingBlock extends BaseBlock {
    type: BlockType.Text;
    textBlockType: BlockType.SmallHeading;
    text: string;
}

export interface ParagraphBlock extends BaseBlock {
    type: BlockType.Text;
    textBlockType: BlockType.ParagraphBlock;
    text: string;
}

export interface ClozeBlockAnswer {
    id: string;
    key: string;
    expected: string;
    caseSensitive: boolean;
}

export interface ClozeBlock extends BaseBlock {
    type: BlockType.Cloze;
    question: string;
    template: string;
    answers: ClozeBlockAnswer[];
    wordBank?: string[]; // TODO add functionality for displaying words from bank
}

export interface MatchingBlock extends BaseBlock {
    type: BlockType.Matching;
    description: string;
    pairs: { left: string; right: string }[];
}

export interface MultipleChoiceOption {
    id: string;
    text: string;
    isCorrect: boolean

}

export interface MultipleChoiceBlock extends BaseBlock {
    type: BlockType.MultipleChoice;
    question: string;
    choices: MultipleChoiceOption[];
}

export type LessonBlock =
    | BigHeadingBlock
    | SmallHeadingBlock
    | ClozeBlock
    | MatchingBlock
    | MultipleChoiceBlock
    | ParagraphBlock;

export interface Lesson {
    id: string;
    title: string;
    lessonNumber: number;
    blocks: LessonBlock[];
}

export interface CourseUnit {
    id: string;
    title: string;
    overview: string;
    unitNumber: number;
    lessons: Lesson[];
}

export interface Course {
    id: string;
    title: string;
    description: string;
    goals: string[];
    prerequisiteLevel: string;
    outcomeLevel: string;
    baseLanguage: string;
    targetLanguage: string;
    units: CourseUnit[];
}

export interface CourseCreatorResponse {
    id: number;
    name: string;
    email: string;
}

export interface UserEnrollmentResponse {
    userId: number;
    courseId: number;
    enrolledStatus: string;
    enrolledAt?: string;
}

export interface BaseCourse {
    id: number;
    creator: CourseCreatorResponse;
    title: string;
    description: string;
    goals: string[];
    prerequisiteLevel: string;
    outcomeLevel: string;
    baseLanguage: string;
    targetLanguage: string;
}

export interface CourseResponse extends BaseCourse {
    units: UnitResponse[];
}

export interface BaseUnit {
    id: number;
    title: string;
    overview: string;
    unitNumber: number;
}

export interface UnitResponse extends BaseUnit {
    lessons: LessonResponse[];
}

export interface BaseLesson {
    id: number;
    title?: string;
    lessonNumber: number;
    textBlocks: TextBlockResponse[];
}

export interface LessonResponse extends BaseLesson {
    clozeBlocks: ClozeBlockResponse[];
    multipleChoiceBlocks: MultipleChoiceBlockResponse[];
}

export interface BaseClozeBlock {
    id: number;
    blockNumber: number;
    // type: BlockType.Cloze;
    question: string;
    template: string;
    // answers: BaseClozeBlockAnswer[];
}

export interface ClozeBlockResponse extends BaseClozeBlock {
    answers: ClozeBlockAnswerResponse[]
}

export interface BaseClozeBlockAnswer {
    id: number;
    key: string;
    expected: string;
    caseSensitive: boolean;
}

export interface ClozeBlockAnswerResponse extends BaseClozeBlockAnswer { }

export interface BaseMultipleChoiceBlock {
    id: number;
    blockNumber: number;
    question: string;
}

export interface MultipleChoiceBlockResponse extends BaseMultipleChoiceBlock {
    // type: BlockType.MultipleChoice;
    choiceOptions: MultipleChoiceOptionResponse[];
}

export interface BaseMultipleChoiceOption {
    id: number;
    text: string;
    isCorrect: boolean;
}

export interface MultipleChoiceOptionResponse extends BaseMultipleChoiceOption { }

export interface UiCourse extends BaseCourse {
    progressInfo: ProgressInfo;
    units: UiUnit[]
}

export interface UiUnit extends BaseUnit {
    progressInfo: ProgressInfo;
    lessons: UiLesson[]
}

export interface UiLesson extends BaseLesson {
    progressInfo: ProgressInfo;
    clozeBlocks: UiClozeBlock[];
    multipleChoiceBlocks: UiMultipleChoiceBlock[];
}



export interface UiClozeBlock extends BaseClozeBlock {
    type: BlockType.Cloze;
    answers: UiClozeBlockAnswer[];
}

export interface UiClozeBlockAnswer extends BaseClozeBlockAnswer {
    userAnswer: string;
}

export interface UiMultipleChoiceBlock extends BaseMultipleChoiceBlock {
    type: BlockType.MultipleChoice;
    choiceOptions: UiMultipleChoiceOption[];
}

export interface UiMultipleChoiceOption extends BaseMultipleChoiceOption {
    isSelected: boolean;
}

export interface TextBlockResponse {
    id: number;
    blockNumber: number;
    type: BlockType.BigHeading | BlockType.SmallHeading | BlockType.ParagraphBlock;
    text: string;
}

export interface MultipleChoiceOptionUserResponse {
    id: number;
    selected: boolean;
}

export interface BlockProps<T extends LessonBlock> {
    block: T;
    blockNumber: number;
    updateBlock: (block: LessonBlock) => void;
    removeCurrentBlock: () => void;
}

export interface CourseAnswers {
    courseId: number;
    userId: number;
    unitAnswers: UnitAnswers[];
}

export interface UnitAnswers {
    unitId: number;
    lessonAnswers: LessonAnswers[];
}

export interface LessonAnswers {
    lessonId: number;
    clozeAnswers: ClozeBlockUserAnswer[]
    userSelectedMcosIds: number[]
    // multipleChoiceUserSelectedOptionIds: number[]
}

export interface ClozeBlockUserAnswer {
    clozeBlockAnswerId: number;
    userInput: string;
}

export interface UserAnswersRequestDto {
    clozeAnswers: ClozeBlockUserAnswerRequestDto[];
    multipleChoiceUserSelectedOptionIds: number[];
}

export interface ClozeBlockUserAnswerRequestDto {
    clozeBlockAnswerId: number;
    userInput: string;
}

export type ProgressInfo = {
    totalBlocks: number;
    finishedBlocks: number;
    progress: number;
};

// export interface CourseAnswersWithProgress {
//     courseId: number;
//     userId: number;
//     progress: number;
//     unitAnswersWithProgress: UnitAnswersWithProgress[];
// }

// export interface UnitAnswersWithProgress {
//     unitId: number;
//     progress: number;
//     lessonAnswersWithProgress: LessonAnswersWithProgress[];
// }

// export interface LessonAnswersWithProgress {
//     lessonId: number;
//     progress: number;
//     clozeAnswers: ClozeBlockUserAnswer[];
//     choiceUserSelectedAnswers: number[];
// }
