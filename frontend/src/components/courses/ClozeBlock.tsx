import type { UiClozeBlock } from "../../types/course";


type ClozeBlockProps = {
    block: UiClozeBlock;
    onAnswerChange: (answerId: number, newValue: string) => void;
}

function ClozeBlock({ block, onAnswerChange }: ClozeBlockProps) {
    const parts = block.template.split(/({{.*?}})/g);

    return (
        <div>
            <h3>{block.question}</h3>
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
                                value={answer.userAnswer}
                                onChange={(e) => onAnswerChange(answer.id, e.target.value)}
                                className="border-b border-gray-500 px-1 mx-1 outline-none focus:border-blue-500"
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