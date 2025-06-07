import { QuestionTypes, WebStates } from "./Enums";
import "./Path.css"

function QuestionBanktype({ setQuestionType, setWebState }) {
    const handleClick = (questionTypes) => {
        setQuestionType(questionTypes); ``
        setWebState(WebStates.Questions);
    };

    return (
        <div className="path-choices-container">
            <h1>Which worlds interest you the most?</h1>

            <div className="path-buttons-container">
                <button className="path-buttons" onClick={() => handleClick(QuestionTypes.Fantasy)}>Fantasy</button>
                <button className="path-buttons" onClick={() => handleClick(QuestionTypes.SchoolLife)}>School Life</button>
            </div>
        </div>
    );
}

export default QuestionBanktype;