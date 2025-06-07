import { QuestionTypes, WebStates } from "./Enums";

function QuestionBanktype({ setQuestionType, setWebState }) {
    const handleClick = (questionTypes) => {
        setQuestionType(questionTypes);
        setWebState(WebStates.Questions);
    }

    return (
        <div>
            <button onClick={() => handleClick(QuestionTypes.Fantasy)}>Fantasy</button>
            <button onClick={() => handleClick(QuestionTypes.SchoolLife)}>School Life</button>
        </div>
    )
}

export default QuestionBanktype;