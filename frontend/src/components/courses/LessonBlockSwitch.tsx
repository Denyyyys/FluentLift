import { BlockType, type TextBlockResponse, type UiClozeBlock, type UiMultipleChoiceBlock } from "../../types/course";
import ClozeBlock from "./ClozeBlock";
import MultipleChoice from "./MultipleChoice";
import TextBlock from "./TextBlock";

type LessonBlockProps = {
    block: TextBlockResponse | UiClozeBlock | UiMultipleChoiceBlock;
    handleClozeAnswerChange: (blockId: number, answerId: number, value: string) => void;
    handleMultipleChoiceAnswerChange: (mcBlockId: number, mcOptionId: number, value: boolean) => void;
    showCorrect: boolean;
    isChecked: boolean;
}

function LessonBlockSwitch({ block, handleClozeAnswerChange, handleMultipleChoiceAnswerChange, showCorrect, isChecked }: LessonBlockProps) {
    switch (block.type) {
        case BlockType.Cloze:
            return <ClozeBlock block={block} showCorrect={showCorrect} isChecked={isChecked} onAnswerChange={(answerId, value) => handleClozeAnswerChange(block.id, answerId, value)}
            />
        case BlockType.MultipleChoice:
            return <MultipleChoice block={block} showCorrect={showCorrect} isChecked={isChecked} onOptionChange={(mcOptionId, value) => handleMultipleChoiceAnswerChange(block.id, mcOptionId, value)} />
        case BlockType.BigHeading:
        case BlockType.SmallHeading:
        case BlockType.ParagraphBlock:
            return <TextBlock block={block} />
        default:
            return <h3>Something is wrong here</h3>
    }
}

export default LessonBlockSwitch;