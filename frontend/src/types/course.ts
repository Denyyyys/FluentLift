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

export interface BlockProps<T extends LessonBlock> {
    block: T;
    blockNumber: number;
    updateBlock: (block: LessonBlock) => void;
    removeCurrentBlock: () => void;
}