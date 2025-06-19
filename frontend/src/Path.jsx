import { QuestionTypes, WebStates } from "./Enums";
import fantasybg from "./assets/fantasybg.jpg";
import solbg from "./assets/solbg.jpg";
import cyberpunkbg from "./assets/cyberpunkbg.jpg";
import "./Path.css"

function QuestionBanktype({ setQuestionType, setWebState }) {
    const handleClick = (questionTypes) => {
        setQuestionType(questionTypes); ``
        setWebState(WebStates.Questions);
    };

    return (
        <div className="path-choices-container">
            <h1>After your death you reincarnated.</h1>
            <h1>Which world would you like to visit?</h1>

            <div className="path-buttons-container">
                <button className="path-buttons" onClick={() => handleClick(QuestionTypes.Fantasy)}> <img src={fantasybg}></img> </button>
                <button className="path-buttons" onClick={() => handleClick(QuestionTypes.SchoolLife)}> <img src={solbg}></img> </button>
                <button className="path-buttons" onClick={() => handleClick(QuestionTypes.Cyberpunk)}> <img src={cyberpunkbg}></img> </button>
            </div>
        </div>
    );
}

export default QuestionBanktype;