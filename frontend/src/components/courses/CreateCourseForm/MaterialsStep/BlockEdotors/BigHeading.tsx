import type { BigHeadingBlock, BlockProps } from '../../../../../types/course'
import { FaTrashAlt } from 'react-icons/fa';

function BigHeading({ block, updateBlock, blockNumber, removeCurrentBlock }: BlockProps<BigHeadingBlock>) {

    return (
        <div className="mb-3">
            <div className='d-flex align-items-center justify-content-between'>
                <div className='d-flex'>
                    <small>{blockNumber}</small><h4 className="mb-2"> Big Heading</h4>
                </div>
                <FaTrashAlt size={24} className="clickable" onClick={removeCurrentBlock} />
            </div>
            <input
                type="text"
                value={block.text}
                className="form-control"
                placeholder="Big heading..."
                onChange={(e) => updateBlock({ ...block, text: e.target.value })}
            />
            <div className="mt-1 progress" role="progressbar" aria-label="Example 1px high" aria-valuenow={25} aria-valuemin={0} aria-valuemax={100} style={{ height: "1px" }}>
                <div className="progress-bar bg-info" style={{ width: "100%" }}></div>
            </div>
        </div>
    );
}

export default BigHeading;