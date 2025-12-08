import type { UiClozeBlock, UiClozeBlockAnswer } from "../../types/course";


type ClozeBlockProps = {
    block: UiClozeBlock;
    onAnswerChange: (answerId: number, newValue: string) => void;
    showCorrect: boolean;
    isChecked: boolean;

}

function ClozeBlock({ block, onAnswerChange, showCorrect, isChecked }: ClozeBlockProps) {
    const parts = block.template.split(/({{.*?}})/g);

    const answerIsCorrect = (blockAnswer: UiClozeBlockAnswer) => {
        return (blockAnswer.caseSensitive && blockAnswer.expected === blockAnswer.userAnswer) ||
            (!blockAnswer.caseSensitive && blockAnswer.expected.toLowerCase() === blockAnswer.userAnswer.toLowerCase())
    }

    return (
        <div className="mb-2">
            <h2>{block.question}</h2>
            <p>

                {parts.map((part, index) => {
                    const match = part.match(/{{(.*?)}}/);
                    if (match) {
                        const key = match[1];
                        const answer = block.answers.find(a => a.key === key);

                        if (!answer) {
                            return <span key={index} className="text-red-500">[unknown: {key}]</span>;
                        }

                        return (
                            <input
                                key={index}
                                type="text"
                                value={showCorrect ? answer.expected : answer.userAnswer}
                                disabled={showCorrect}
                                className={!showCorrect && isChecked
                                    ? answerIsCorrect(answer) ?
                                        "bg-success "
                                        : "bg-danger"
                                    : ""
                                }


                                onChange={(e) => onAnswerChange(answer.id, e.target.value)}

                            />
                        );
                    }

                    return <span key={index}>{part}</span>;
                })}
            </p>
        </div>
    )
}

export default ClozeBlock