import { BlockType, type LessonBlock } from '../../../../../types/course';
import BigHeading from './BigHeading';
import Cloze from './Cloze';
import MultipleChoice from './MultipleChoice';
import Paragraph from './Paragraph';
import SmallHeading from './SmallHeading';

interface LessonBlockEditorProps {
    blockNumber: number;
    block: LessonBlock;
    updateBlock: (block: LessonBlock) => void;
    removeCurrentBlock: () => void;
}

function Lesson({ block, blockNumber, removeCurrentBlock, updateBlock }: LessonBlockEditorProps) {
    switch (block.type) {
        case BlockType.Text:
            switch (block.textBlockType) {
                case BlockType.BigHeading:
                    return <BigHeading block={block} blockNumber={blockNumber} removeCurrentBlock={removeCurrentBlock} updateBlock={updateBlock} />
                case BlockType.SmallHeading:
                    return <SmallHeading block={block} blockNumber={blockNumber} removeCurrentBlock={removeCurrentBlock} updateBlock={updateBlock} />
                case BlockType.ParagraphBlock:
                    return <Paragraph block={block} blockNumber={blockNumber} removeCurrentBlock={removeCurrentBlock} updateBlock={updateBlock} />
                default: return "hmm, it should never happen too"
            }
        case BlockType.Cloze:
            return <Cloze block={block} blockNumber={blockNumber} removeCurrentBlock={removeCurrentBlock} updateBlock={updateBlock} />
        case BlockType.MultipleChoice:
            return <MultipleChoice block={block} blockNumber={blockNumber} removeCurrentBlock={removeCurrentBlock} updateBlock={updateBlock} />
        default: return "hmm, it should never happen"
    }
}

export default Lesson;