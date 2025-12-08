import type { CardCreateDto } from '../../types/card'
import { FaTrashAlt } from "react-icons/fa";

type CardFormProps = {
    newCard: CardCreateDto,
    updateCard: (updatedNewCard: CardCreateDto) => void
    deleteCard: (id: string) => void
}

function CardForm({ newCard, updateCard, deleteCard }: CardFormProps) {

    return (
        <div className='mb-3 p-3'>
            <p >
                <FaTrashAlt color='red' size={20} className="clickable" onClick={() => deleteCard(newCard.id)} />
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

export default CardForm