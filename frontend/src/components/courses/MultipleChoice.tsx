import type { UiMultipleChoiceBlock } from "../../types/course";

type MultipleChoiceBlockProps = {
    block: UiMultipleChoiceBlock;
    onOptionChange: (mcOptionId: number, value: boolean) => void
}

function MultipleChoice({ block, onOptionChange }: MultipleChoiceBlockProps) {
    return (
        <div>
            <h2>{block.question}</h2>
            {block.choiceOptions.map(choiceOption => (
                <div key={choiceOption.id} className="form-check">
                    <input className="form-check-input" type="checkbox" checked={choiceOption.isSelected} id={`mc-${block.id}-${choiceOption.id}`} onChange={(e) => onOptionChange(choiceOption.id, e.target.checked)} />
                    <label className="form-check-label" htmlFor={`mc-${block.id}-${choiceOption.id}`}>
                        {choiceOption.text}
                    </label>
                </div>
            ))}
        </div>
    )
}

export default MultipleChoice