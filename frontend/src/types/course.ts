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

export interface CourseResponse {
    id: number;
    creator: CourseCreatorResponse;
    title: string;
    description: string;
    goals: string[];
    prerequisiteLevel: string;
    outcomeLevel: string;
    baseLanguage: string;
    targetLanguage: string;
    units: CourseUnitResponse[];
}

export interface UserEnrollmentResponse {
    userId: number;
    courseId: number;
    enrolledStatus: string;
    enrolledAt?: string;
}

export interface CourseUnitResponse {
    id: number;
    title: string;
    overview: string;
    unitNumber: number;
    lessons: LessonResponse[];
}

export interface LessonResponse {
    id: number;
    title?: string;
    lessonNumber: number;
    textBlocks: TextBlockResponse[];
    clozeBlocks: ClozeBlockResponse[];
    multipleChoiceBlocks: MultipleChoiceBlockResponse[];
}

export interface UiLesson {
    id: number;
    title?: string;
    lessonNumber: number;
    textBlocks: TextBlockResponse[];
    clozeBlocks: UiClozeBlock[];
    multipleChoiceBlocks: UiMultipleChoiceBlock[];
}

export interface UiClozeBlock {
    id: number;
    blockNumber: number;
    type: BlockType.Cloze;
    question: string;
    template: string;
    answers: UiClozeBlockAnswer[];
}

export interface UiClozeBlockAnswer {
    id: number;
    key: string;
    expected: string;
    caseSensitive: boolean;
    userAnswer: string;
}

export interface UiMultipleChoiceBlock {
    id: number;
    blockNumber: number;
    type: BlockType.MultipleChoice;
    question: string;
    choiceOptions: UiMultipleChoiceOption[];
}

export interface UiMultipleChoiceOption {
    id: number;
    text: string;
    isCorrect: boolean;
    isSelected: boolean;
}

export interface TextBlockResponse {
    id: number;
    blockNumber: number;
    type: BlockType.BigHeading | BlockType.SmallHeading | BlockType.ParagraphBlock;
    text: string;
}

export interface ClozeBlockResponse {
    id: number;
    blockNumber: number;
    // type: BlockType.Cloze;
    question: string;
    template: string;
    answers: ClozeBlockResponseAnswer[];
}

export interface ClozeBlockResponseAnswer {
    id: number;
    key: string;
    expected: string;
    caseSensitive: boolean;
}

export interface MultipleChoiceBlockResponse {
    id: number;
    blockNumber: number;
    // type: BlockType.MultipleChoice;
    question: string;
    choiceOptions: MultipleChoiceOptionResponse[];
}

export interface MultipleChoiceOptionResponse {
    id: number;
    text: string;
    isCorrect: boolean;
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

export interface CourseProgress {
    courseId: number;
    userId: number;
    progress: number;
    unitProgresses: UnitProgress[];
}

export interface UnitProgress {
    unitId: number;
    progress: number;
    lessonProgresses: LessonProgress[];
}

export interface LessonProgress {
    lessonId: number;
    progress: number;
    clozeAnswers: ClozeProgress[];
    choiceUserSelectedAnswers: MultipleChoiceProgress[];
}

export interface ClozeProgress {
    id: number;
    clozeBlockAnswerId: number;
    userInput: string;
}

export interface MultipleChoiceProgress {
    multipleChoiceSelectedOptionId: number;
}