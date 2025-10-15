import { BlockType, type TextBlockResponse } from "../../types/course";


type TextBlockProps = {
    block: TextBlockResponse;
}

// TODO XSS attack prevent or check if it works in the first place
function TextBlock({ block }: TextBlockProps) {
    if (block.type === BlockType.BigHeading) {
        return <h2>{block.text}</h2>
    }

    if (block.type === BlockType.SmallHeading) {
        return <h3>{block.text}</h3>
    }
    if (block.type === BlockType.ParagraphBlock) {
        return <p>{block.text}</p>
    }


}

export default TextBlock