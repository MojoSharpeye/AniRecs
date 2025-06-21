import { WorldTypes, WebStates } from "./Enums";
import fantasybg from "./assets/fantasybg.jpg";
import solbg from "./assets/solbg.jpg";
import cyberpunkbg from "./assets/cyberpunkbg.jpg";
import "./Path.css"

function QuestionBanktype({ setWorldType, setWebState }) {
    const handleClick = (worldTypes) => {
        setWorldType(worldTypes); 
        setWebState(WebStates.Questions);
    };

    return (
        <div className="path-choices-container">
            <h1>After your death you reincarnated.</h1>
            <h1>Which world would you like to visit?</h1>

            <div className="path-buttons-container">
                <button className="path-buttons" onClick={() => handleClick(WorldTypes.Fantasy)}> <img src={fantasybg}></img> </button>
                <button className="path-buttons" onClick={() => handleClick(WorldTypes.SchoolLife)}> <img src={solbg}></img> </button>
                <button className="path-buttons" onClick={() => handleClick(WorldTypes.Cyberpunk)}> <img src={cyberpunkbg}></img> </button>
            </div>
        </div>
    );
}

export default QuestionBanktype;