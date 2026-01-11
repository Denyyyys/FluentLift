import type { CardOwnerResponseDto } from '../../types/card'
import { FaTrashAlt } from "react-icons/fa";

type UpdateCardFormProps = {
    newCard: CardOwnerResponseDto,
    updateCard: (updatedNewCard: CardOwnerResponseDto) => void
    deleteCard: (tempId: string) => void
}

function UpdateCardForm({ newCard, updateCard, deleteCard }: UpdateCardFormProps) {

    return (
        <div className='mb-3 p-3 rounded'>
            <p >
                <FaTrashAlt color='red' size={20} className="clickable" onClick={() => deleteCard(newCard.tempId)} />
            </p>

            <div className="mb-3">
                <label htmlFor="frontText" className="form-label">Front Text</label>
                <input
                    type="text"
                    className="form-control"
                    id="frontText"
                    value={newCard.frontText}
                    onChange={(e) => updateCard({ ...newCard, frontText: e.target.value })}
                />
            </div>

            <div className="mb-3">
                <label htmlFor="backText" className="form-label">Back Text</label>
                <input
                    type="text"
                    className="form-control"
                    id="backText"
                    value={newCard.backText}
                    onChange={(e) => updateCard({ ...newCard, backText: e.target.value })}
                />
            </div>
        </div>
    )
}

export default UpdateCardForm