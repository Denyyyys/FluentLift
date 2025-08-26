import { FaTrashAlt } from "react-icons/fa";
import type { BlockProps, ParagraphBlock } from "../../../../../types/course";

function Paragraph({ block, updateBlock, blockNumber, removeCurrentBlock }: BlockProps<ParagraphBlock>) {
    return (
        <div className="mb-3">
            <div className='d-flex align-items-center justify-content-between'>
                <div className='d-flex'>
                    <small>{blockNumber}</small><h5 className="mb-2">Paragraph</h5>
                </div>
                <FaTrashAlt size={24} className="clickable" onClick={removeCurrentBlock} />
            </div>
            <input
                type="text"
                value={block.text}
                className="form-control"
                placeholder="Paragraph..."
                onChange={(e) => updateBlock({ ...block, text: e.target.value })}
            />
            <div className="mt-1 progress" role="progressbar" aria-label="Example 1px high" aria-valuenow={25} aria-valuemin={0} aria-valuemax={100} style={{ height: "1px" }}>
                <div className="progress-bar bg-info" style={{ width: "100%" }}></div>
            </div>
        </div>
    );
}

export default Paragraph;