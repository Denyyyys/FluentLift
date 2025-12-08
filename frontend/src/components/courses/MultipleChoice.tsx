import type { UiMultipleChoiceBlock } from "../../types/course";
import { FaRegCircleCheck } from "react-icons/fa6";
import { RxCrossCircled } from "react-icons/rx";

type MultipleChoiceBlockProps = {
    block: UiMultipleChoiceBlock;
    onOptionChange: (mcOptionId: number, value: boolean) => void
    showCorrect: boolean;
    isChecked: boolean;

}

function MultipleChoice({ block, onOptionChange, showCorrect, isChecked }: MultipleChoiceBlockProps) {

    const userResponseIsCorrect = () => {
        return block.choiceOptions.every((choiceOption) => {
            return choiceOption.isCorrect === choiceOption.isSelected;
        })
    }

    return (
        <div>
            <div className="d-flex align-items-center gap-2">
                <h2>{block.question}</h2>
                {!showCorrect && isChecked && (
                    userResponseIsCorrect() ? <FaRegCircleCheck color="green" size={24} /> : <RxCrossCircled color="red" size={24} />
                )}
            </div>
            {block.choiceOptions.map(choiceOption => (
                <div key={choiceOption.id} className="form-check">
                    <input
                        className="form-check-input"
                        type="checkbox"
                        checked={showCorrect ? choiceOption.isCorrect : choiceOption.isSelected}
                        id={`mc-${block.id}-${choiceOption.id}`}
                        onChange={(e) => onOptionChange(choiceOption.id, e.target.checked)}
                        disabled={showCorrect}

                    // className={!showCorrect && isChecked
                    //             ? answerIsCorrect(answer) ?
                    //                 "bg-success "
                    //                 : "bg-danger"
                    //             : ""
                    //         }

                    />
                    <label className="form-check-label" htmlFor={`mc-${block.id}-${choiceOption.id}`}>
                        {choiceOption.text}
                    </label>
                </div>
            ))}
        </div>
    )
}

export default MultipleChoice