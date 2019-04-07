**Design ad Concept:**

Game is built on MVC-like pattern.
                       
Required: configurations file(game.properties)

Main instance is the Controller, which is responsible for initialization and full control of turns and displaying informations.
Controller is triggered as thread. This decision is based on behaviours of simulation testing.

-controller initializes the game(playing order, playground)
-player enters x,y on standard input
-turn is created by stdin input
-turn is passed to playground
-playground validates turn and captures field by player
-controller evaluates if one of finishing conditions matches
-if not next player is on turn 

**Build**
./gradlew clean build

**Run**

`java -jar tic-tac-toe-1.0.jar`

 
