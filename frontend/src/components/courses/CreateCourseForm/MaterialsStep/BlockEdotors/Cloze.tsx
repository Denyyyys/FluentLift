import type { BlockProps, ClozeBlock, ClozeBlockAnswer } from '../../../../../types/course'
import { v4 as uuid } from "uuid";

import { FaTrashAlt } from 'react-icons/fa';

function Cloze({ block, updateBlock, blockNumber, removeCurrentBlock }: BlockProps<ClozeBlock>) {

    const addNewAnswer = () => {
        if (block.answers.length > 0 && (block.answers[block.answers.length - 1].key === "" || block.answers[block.answers.length - 1].expected === ""))
            return;
        const updatedBlock = { ...block, answers: [...block.answers, { caseSensitive: true, id: uuid(), expected: "", key: "" }] } satisfies ClozeBlock
        updateBlock(updatedBlock);
    }

    const updateAnswer = (updatedAnswer: ClozeBlockAnswer) => {
        const updatedAnswers = block.answers.map(answer => answer.id !== updatedAnswer.id ? answer : updatedAnswer);
        updateBlock({ ...block, answers: updatedAnswers })
    }

    const toggleAnswerCaseSensitive = (answerId: string) => {
        const updatedAnswers = block.answers.map(answer => answer.id !== answerId ? answer : { ...answer, caseSensitive: !answer.caseSensitive })
        updateBlock({ ...block, answers: updatedAnswers });
    }

    const removeCurrentAnswer = (answerId: string) => {
        updateBlock({ ...block, answers: block.answers.filter(answer => answer.id !== answerId) })
    }

    return (
        <div className="mb-3">
            <div className='d-flex align-items-center justify-content-between mb-2'>
                <div className='d-flex'>
                    <small>{blockNumber}</small><h4> Cloze</h4>
                </div>
                <FaTrashAlt size={24} className="clickable" onClick={removeCurrentBlock} />
            </div>
            <div className="mb-2">
                <label htmlFor={`cloze-${blockNumber}-question`} className="form-label">Task Question</label>
                <input
                    id={`cloze-${blockNumber}-question`}
                    type="text"
                    value={block.question}
                    className="form-control"
                    placeholder='Fill the gaps'
                    onChange={(e) => updateBlock({ ...block, question: e.target.value })}
                />
            </div>
            <div className="mb-2">
                <label htmlFor={`cloze-${blockNumber}-template`} className="form-label">Task Template</label>
                <input
                    id={`cloze-${blockNumber}-template`}
                    type="text"
                    value={block.template}
                    className="form-control"
                    placeholder="The capital of {{country}} is Paris"
                    onChange={(e) => updateBlock({ ...block, template: e.target.value })}
                />
            </div>
            <div className="mb-2">
                <label htmlFor={`cloze-${blockNumber}-answers-1`} className="form-label">Correct Answers In Gaps</label>
                {block.answers.map((answer, index) => (
                    <div className='d-flex align-items-center justify-content-between mb-4' key={answer.id}>
                        {
                            answer.caseSensitive ? <button className='btn btn-success' onClick={() => toggleAnswerCaseSensitive(answer.id)}>Case Sensitive: Yes</button> : <button className='btn btn-danger' onClick={() => toggleAnswerCaseSensitive(answer.id)}>Case Sensitive: No</button>
                        }
                        <div className='d-flex align-items-center'>
                            <label className='me-2'>Key</label>
                            <input
                                id={`cloze-${blockNumber}-answers-key-${index}`}
                                type="text"
                                value={answer.key}
                                className="form-control"
                                placeholder="country"
                                onChange={(e) => updateAnswer({ ...answer, key: e.target.value })}
                            />
                        </div>

                        <div className='d-flex align-items-center'>
                            <label className='me-2'>Answer</label>
                            <input
                                id={`cloze-${blockNumber}-answers-expected-${index}`}
                                type="text"
                                value={answer.expected}
                                className="form-control"
                                placeholder="France"
                                onChange={(e) => updateAnswer({ ...answer, expected: e.target.value })}
                            />
                        </div>

                        <FaTrashAlt size={24} className="clickable" onClick={() => removeCurrentAnswer(answer.id)} />
                    </div>
                ))}
                <button className='btn btn-secondary' onClick={addNewAnswer}>Add Answer For Gap</button>

            </div>
            <div className="mt-1 progress" role="progressbar" aria-label="Example 1px high" aria-valuenow={25} aria-valuemin={0} aria-valuemax={100} style={{ height: "1px" }}>
                <div className="progress-bar bg-info" style={{ width: "100%" }}></div>
            </div>
        </div>
    );
}
export default Cloze;