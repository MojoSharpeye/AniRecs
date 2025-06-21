import { QuestionTypes } from "../Enums";

const cyberpunkQuestions = [
    {
        type: QuestionTypes.OpenEnded,
        question: "You hear the alarms ring and woke up for the morning. Whats the first thing you do?"
    },
    {
        type: QuestionTypes.OpenEnded,
        question: "You start heading to school and on the way, you see your friend accross the road. How would you approach him?"
    }, 
    {
        type: QuestionTypes.OpenEnded,
        question: "As you walk into the classroom, you realize the seating chart has changed. How do you respond to being moved away from your friends?"
    }, 
    {
        type: QuestionTypes.YesOrNo,
        question: "You sit down on your sit. The teacher comes in and announce that you are to do a group project today. Are you excited?",
        choices: [
            "Yes",
            "No"
        ]
    },
    {
        type: QuestionTypes.OpenEnded,
        question: "You got into your groups and the day continues. One of your group member was bludging and doesn't contribute at all. What would you do and why?"
    },
    {
        type: QuestionTypes.WordedMultipleChoice,
        question: "At the end of they day, as you are walking home, you overhear a rumor about one of your classmates that may not be true. How would you respond?",
        choices: [
            "Go up and punch him in the face",
            "Ignore it",
            "Politely stand up for your classmate"
        ]
    },
    {
        type: QuestionTypes.ScaleMultipleChoice,
        question: "You return home after. You ate dinner with your family and then went to bed. As you lay there, you think about the day. How confident are you in your choices?",
        scaleRange: 5
    }
];

export default cyberpunkQuestions;