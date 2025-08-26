import { FaTrashAlt } from "react-icons/fa";
import type { BlockProps, MultipleChoiceOption, MultipleChoiceBlock } from "../../../../../types/course"
import { v4 as uuid } from "uuid";

function MultipleChoice({ block, updateBlock, blockNumber, removeCurrentBlock }: BlockProps<MultipleChoiceBlock>) {
    const addNewChoice = () => {
        if (block.choices.length > 0 && block.choices[block.choices.length - 1].text === "")
            return;
        const updatedBlock = { ...block, choices: [...block.choices, { isCorrect: false, id: uuid(), text: "" }] } satisfies MultipleChoiceBlock
        updateBlock(updatedBlock);
    }

    const toggleIsCorrect = (choiceId: string) => {
        updateBlock({ ...block, choices: block.choices.map(choice => choice.id !== choiceId ? choice : { ...choice, isCorrect: !choice.isCorrect }) } satisfies MultipleChoiceBlock)

    }

    const updateChoice = (updatedChoice: MultipleChoiceOption) => {
        const updatedChoices = block.choices.map(choice => choice.id !== updatedChoice.id ? choice : updatedChoice);
        updateBlock({ ...block, choices: updatedChoices })
    }

    return (
        <div className="mb-3">
            <div className='d-flex align-items-center justify-content-between mb-2'>
                <div className='d-flex'>
                    <small>{blockNumber}</small><h4> Multiple Choice</h4>
                </div>
                <FaTrashAlt size={24} className="clickable" onClick={removeCurrentBlock} />
            </div>
            <div className="mb-2">
                <label className="form-label">Task Question</label>
                <input
                    type="text"
                    value={block.question}
                    className="form-control"
                    placeholder="Which of the following words mean 'friend' in Spanish?"
                    onChange={(e) => updateBlock({ ...block, question: e.target.value } satisfies MultipleChoiceBlock)}
                />
            </div>
            <h5 className="mb-1">Choices</h5>
            {block.choices.map((choice) => (
                <div className="d-flex align-items-center justify-content-between gap-3 mb-2" key={choice.id}>
                    {
                        choice.isCorrect ? <button className='btn btn-success' style={{ minWidth: "120px" }}
                            onClick={() => toggleIsCorrect(choice.id)}>Correct: Yes</button> : <button className='btn btn-danger' style={{ minWidth: "120px" }} onClick={() => toggleIsCorrect(choice.id)}>Correct: No</button>
                    }
                    <input
                        type="text"
                        value={choice.text}
                        className="form-control"
                        placeholder="Amigo"
                        onChange={(e) => updateChoice({ ...choice, text: e.target.value })}
                    />
                </div>
            ))
            }
            <button className="btn btn-secondary" onClick={addNewChoice}>Add Choice</button>
            <div className="mt-1 progress" role="progressbar" aria-label="Example 1px high" aria-valuenow={25} aria-valuemin={0} aria-valuemax={100} style={{ height: "1px" }}>
                <div className="progress-bar bg-info" style={{ width: "100%" }}></div>
            </div>
        </div >
    )
}
export default MultipleChoice;
