const questionsDb = [
    "You spawn on the streets of a world full of magic and adventure, what is the first thing that you want to do? " + 
    "Additional info: Player does not have anything except for everyday clothing",
    
    "Additional info: some event happens to make players relise that they have no money. " + 
    "You hear of a adventurer guild that provides people jobs. What do you do with this information? ",

    "Instruction: if the player chose to go to the adventurer's guild, then they walk to the adventurer's guild which is in the business district of the city. " +
    "Instruction: if the player chose not to go or some other action, then introduce a new NPC to give them quests. " +
    "You are given a variety of quests, some of which are the classic slime killing and mushroom collecting. What kind of quests would you like?",

    "Instruction: depending on the player answer, generate an objective - unless the player already gave some kinda of objective in their response " +
    "You picked your quest and are told that the target location is in the forest to the South. Do you depart immediately?",

    "Instruction: change the following 'You depart to the forest in search of what you are looking for' part to make sense given the context of the previous question. " +
    "You depart to the forest in search of what you are looking for. Before long you found a singular wild dog. It looked very hostile. " +
    "Do you confront it in hope it's friendly? ",

    "After the dog issue, you continue on your quest. You complete your quest with major difficulties. Afterwards you quickly return back " +
    "to the person who gave you this quick errand and cashed in your loot. You earned enough money for food and a place to stay for the night. " +
    "Do you want to see your personality type?"
]

export default questionsDb;