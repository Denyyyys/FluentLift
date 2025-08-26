import { useState, type KeyboardEvent } from "react"
import { MdEdit } from "react-icons/md";
import { FaTrashAlt } from "react-icons/fa";
import type { Course } from "../../../types/course";

type CourseSettingsProps = {
    course: Course;
    setCourse: React.Dispatch<React.SetStateAction<Course>>;

};
function InfoStep({ course, setCourse }: CourseSettingsProps) {
    const [newGoal, setNewGoal] = useState("");
    const [editingGoalIndex, setEditingGoalIndex] = useState<number | null>(null);
    const [editingGoalText, setEditingGoalText] = useState("");

    const handleAddGoal = () => {
        if (newGoal.trim() === "") return;
        setCourse({ ...course, goals: [...course.goals, newGoal.trim()] })
        setNewGoal("");
    };

    const handleGoalEnter = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === "Enter") {
            e.preventDefault();
            if (editingGoalIndex !== null) {
                handleSaveEditGoal(editingGoalIndex);
            } else {
                handleAddGoal();
            }
        }
    };

    const handleSaveEditGoal = (index: number) => {
        if (editingGoalText.trim() === "") return;
        const updatedGoals = [...course.goals];
        updatedGoals[index] = editingGoalText.trim();
        // setCourseGoals(updatedGoals);
        setCourse(prev => ({ ...prev, goals: updatedGoals }))
        setEditingGoalIndex(null);
        setEditingGoalText("");
    }

    const handleDeleteGoal = (index: number) => {
        setCourse(prev => ({ ...prev, goals: prev.goals.filter((_, i) => i !== index) }));
        if (editingGoalIndex === index) {
            setEditingGoalIndex(null);
            setEditingGoalText("");
        }
    }

    const handleEditGoal = (index: number) => {
        setEditingGoalIndex(index);
        setEditingGoalText(course.goals[index]);
    }

    const possibleLevelsOptions = ["None", "A1", "A2", "B1", "B2", "C1", "C2"];
    const possibleLanguageOptions = ["English", "Polish", "Ukrainian", "German"];

    return (
        <form className="d-flex gap-5" onSubmit={(e) => { e.preventDefault(); }}>
            <div className="flex-fill">
                <div className="mb-3">
                    <label htmlFor="courseTitle" className="form-label">Title</label>
                    <input type="text" placeholder="Spanish Course - Learn from 0" className="form-control" id="courseTitle" value={course.title} onChange={(e) => setCourse({ ...course, title: e.target.value })}></input>
                </div>

                <div className="mb-3">
                    <label htmlFor="courseDescription" className="form-label">Description</label>
                    <textarea className="form-control" placeholder="This course is designed for beginners who are just starting their journey with Spanish." id="courseDescription" rows={8} value={course.description} onChange={(e) => setCourse({ ...course, description: e.target.value })}></textarea>
                </div>

                <div className="mb-3">
                    <label htmlFor="courseGoals" className="form-label">Goals</label>
                    {
                        course.goals.map((goal, index) => (
                            <div key={index} className="mb-1 d-flex gap-2">
                                {editingGoalIndex === index ? (
                                    <input
                                        type="text"
                                        className="form-control"
                                        value={editingGoalText}
                                        onChange={(e) => setEditingGoalText(e.target.value)}
                                        onKeyDown={handleGoalEnter}
                                    />
                                ) : (
                                    <input
                                        type="text"
                                        className="form-control"
                                        value={goal}
                                        readOnly />
                                )}
                                <button
                                    type="button"
                                    className="btn btn-sm btn-outline-primary"
                                    onClick={() =>
                                        editingGoalIndex === index ? handleSaveEditGoal(index) : handleEditGoal(index)
                                    }
                                >
                                    <MdEdit />
                                </button>
                                <button
                                    type="button"
                                    className="btn btn-sm btn-outline-danger"
                                    onClick={() => handleDeleteGoal(index)}
                                >
                                    <FaTrashAlt />
                                </button>
                            </div>
                        ))
                    }
                    <div className=" gap-2">
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Add a new goal"
                            value={newGoal}
                            onChange={(e) => setNewGoal(e.target.value)}
                            onKeyDown={handleGoalEnter}
                        />
                        <button type="button" className="btn btn-primary mt-1" onClick={handleAddGoal}>
                            Add Goal
                        </button>
                    </div>
                </div>
            </div>
            <div className=" ms-3">
                <div className="mb-3">
                    <label htmlFor="prerequisiteLevel" className="form-label">Prerequisite Level</label>

                    <div className="dropwodn">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="prerequisiteLevel"
                            aria-expanded="false">{course.prerequisiteLevel}
                        </button>
                        <ul className="dropdown-menu ">
                            {possibleLevelsOptions.map((option) => (
                                <li key={option}>
                                    <button className="dropdown-item" onClick={() => setCourse({ ...course, prerequisiteLevel: option })}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>

                </div>

                <div className="mb-3">
                    <label htmlFor="outcomeLevel" className="form-label">Outcome Level</label>

                    <div className="dropwodn">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="outcomeLevel"
                            aria-expanded="false">{course.outcomeLevel}
                        </button>
                        <ul className="dropdown-menu">
                            {possibleLevelsOptions.map((option) => (
                                <li key={option}>
                                    <button className="dropdown-item" onClick={() => setCourse({ ...course, outcomeLevel: option })}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>

                </div>


                <div className="mb-3">
                    <label htmlFor="baseLanguage" className="form-label">Base Language</label>

                    <div className="dropwodn">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="baseLanguage"
                            aria-expanded="false">{course.baseLanguage}
                        </button>
                        <ul className="dropdown-menu">
                            {possibleLanguageOptions.map((option) => (
                                <li key={option}>
                                    <button className="dropdown-item" onClick={() => setCourse({ ...course, baseLanguage: option })}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>

                </div>



                <div className="mb-3">
                    <label htmlFor="targetLanguage" className="form-label">Target Language</label>

                    <div className="dropwodn">
                        <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" id="targetLanguage"
                            aria-expanded="false">{course.targetLanguage}
                        </button>
                        <ul className="dropdown-menu">
                            {possibleLanguageOptions.map((option) => (
                                <li key={option}>
                                    <button className="dropdown-item" onClick={() => setCourse({ ...course, targetLanguage: option })}>{option}</button>
                                </li>
                            ))}
                        </ul>
                    </div>

                </div>
            </div>
        </form>
    )
}

export default InfoStep;