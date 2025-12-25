import { useState } from "react";
import { Button, Form } from "react-bootstrap";

interface Props {
    placeholder?: string;
    questionId: string;
    parentAnswerId: null | number;
    onSubmit: (questionId: string, content: string, parentAnswerId: null | number) => void;
    onCancel?: () => void;
}

export const AnswerForm = ({
    placeholder = "Write your answer...",
    onSubmit,
    questionId,
    parentAnswerId,
    onCancel
}: Props) => {
    const [content, setContent] = useState("");

    const handleSubmit = () => {
        if (!content.trim()) return;
        onSubmit(questionId, content, parentAnswerId);
        setContent("");
    };

    return (
        <div className="mt-3">
            <Form.Control
                as="textarea"
                rows={3}
                value={content}
                placeholder={placeholder}
                onChange={e => setContent(e.target.value)}
            />

            <div className="d-flex gap-2 mt-2">
                <Button size="sm" onClick={handleSubmit}>
                    Submit
                </Button>

                {onCancel && (
                    <Button size="sm" variant="outline-secondary" onClick={onCancel}>
                        Cancel
                    </Button>
                )}
            </div>
        </div>
    );
};
