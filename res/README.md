### **Overview**
The current project creates an interactive GUI for an adventure 
game in which a player can walk around a dungeon and collect treasure.
The user interact with the game using keys and mouse-clicks. The
objective of the game is to explore the dungeon collecting
treasure and reach the end of the dungeon while avoiding and/or 
slaying the monsters that might come in the way.
The player starts at a predetermined start location in the
game and the game ends when the player reaches the end location
or is eaten by the monster.

### **List of features**
- The dungeon created by this model is such that there exists
at least one path between each location in the dungeon.
- The dungeon contains caves as well as tunnels which the player
can use to move around.
- Initially the dungeon is all black, except the current position
of the player.
- The player can see their location and the directions in which
they can move next from the GUI.
- A birds eye view of the dungeon unravels as the player moves
around the dungeon.
- The treasure in the dungeon could be found in caves whereas
the arrows can be found in caves as well as tunnels.
- The player can sense the monsters nearby by the indications of
bad and terrible stench.
- The player can use the arrows to slay the monsters.
- While shooting arrows the player can specify the distance 
they want the arrow to travel given that the distance has to
be between 1-5.
- The distance given for an arrow is calculated in terms of
caves only.
- The arrows can turn when passing through the tunnels.
- If an arrow hits a monster successfully, the player receives
a feedback based on the sound the monster makes on being hit.
- The player can use mouse click as well as key presses to move the player 
around the dungeon.
- The player can restart the same game using the Restart Game command.

### **How To Run**
In Command prompt/Terminal, navigate to the /res folder and run the 
command: 
- To Run Interactive Command-based game:
```
java -jar Project5-GUIAdventureGame.jar <rowcount> <columncount> <interconnectivity>
 <IsWrapping(Y/N)> <% of caves/locations with treasure/arrows> <difficulty level>
```
- To Run Interactive GUI-based game:
```
java -jar Project5-GUIAdventureGame.jar
```

### **How to Use the Program**
The User can use any of the above 2 commands to run different
instances of the games. The command with parameters start a console
based interactive game, where as adding no parameters launches a 
GUI-based game.
The game starts with the player at the start location. The 
player is given the option to select any of the commands from
Move, Pick or Shoot. Based on the command selected by the user
the next action is determined. If the player selects the move
command, the player is asked to specify the direction in which
they need to move and if it is a valid direction, the player 
is moved in that direction. If the command selected is Pick-up,
the user is asked to specify the item and the quantity of that
item they want to pick. Then based on the availability of that 
item at the location, the item is added to the users bag. Similary,
the Shoot command allows the user to shoot an arrow in a specified
direction for a specified distance. The player has to move through
the dungeon to reach the end location without getting killed by the 
monster.
The GUI allows the player to use keys or mouse clicks to move
the player in the game. The GUI also keeps track of the players
current bag details as well as the items present in a particular
location. 

### **Description of Examples**
GUI-ScreenCaptures (GUI screenshots)
- Shows the Instructions for playing the game.
- Fully Explored non-wrapping dungeon
- Fully explored wrapping dungeon
- Player collecting the treasure and Arrows.

Run1-PlayerWins.txt (Player winning example)
Run2-OtyughWins.txt (Player losing example)

- Once the game starts, the screen will display the players'
information along with the information of the location the 
player is in:
```
----------------------------------------------------
You are in a Cave
Doors lead to: E S W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3
You find 6 rubies, 2 diamonds, 2 arrows here
```
- The player can select the action they want to perform. For 
Pick action the player can type P and specify what item and 
quantity they want to pick as follows:
```
Move, Pickup, or Shoot (M-P-S) or Q to Quit: p
Pick what (R-D-S-A)?: R
Pick how many?: 5
```
- The picked item gets updated in the players bag and can be
seen when the next action is requested from the player.
```
----------------------------------------------------
You are in a Cave
Doors lead to: E S W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 5, Arrows- 3
You find 1 rubies, 2 diamonds, 2 arrows here
```

- The player can move from one location to another by specifying
the direction in which they want to move as follows:
```
----------------------------------------------------             
You are in a Cave
Doors lead to: E S W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 5, Arrows- 5
You find 1 rubies, 2 diamonds, here

Move, Pickup, or Shoot (M-P-S) or Q to Quit: m
Where to (N-E-S-W)?: e
----------------------------------------------------
You are in a Tunnel
Doors lead to: E W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 5, Arrows- 5
```
- When the player is near a monster, the game indicates the same
as follows:
```
----------------------------------------------------
You are in a Cave
Doors lead to: N E S W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 5, Arrows- 7
You smell something Bad nearby.
```
- If the monster is in the neighbouring cave, the smell is more
pungent and is given as:
```
You are in a cave
Doors lead to: N E W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 0, Arrows- 3
You smell something Terrible nearby.
```

- The player can slay the monster by shooting the arrows. The
Shoot action is used to shoot the arrow in specified direction
and at specified distance.
```
----------------------------------------------------
You are in a Cave
Doors lead to: N S W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 5, Arrows- 4
You smell something Bad nearby.

Move, Pickup, or Shoot (M-P-S) or Q to Quit: s
Shoot in which direction (N-E-S-W)?: n
Shoot how far? (1-5): 1
You shoot an arrow into the darkness
```
- If the arrow successfully hits a monster, you get a reaction sound
as follows:
```
----------------------------------------------------
You are in a Cave
Doors lead to: N S W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 5, Arrows- 3
You smell something Bad nearby.

Move, Pickup, or Shoot (M-P-S) or Q to Quit: s
Shoot in which direction (N-E-S-W)?: n
Shoot how far? (1-5): 2
You shoot an arrow into the darkness
You hear a great howl in the distance
```
- If the same monster is hit with a arrow again, it dies. The
game lets you know about it as follows:
```
----------------------------------------------------
You are in a Cave
Doors lead to: N S W 
You have: Diamonds- 0, Sapphires- 0, Rubies- 5, Arrows- 2
You smell something Bad nearby.

Move, Pickup, or Shoot (M-P-S) or Q to Quit: s
Shoot in which direction (N-E-S-W)?: n
Shoot how far? (1-5): 2
You shoot an arrow into the darkness
You killed an Otyugh !
```
- If you successfully reach the end of the Dungeon, you get
the following message:
```
----------------------------------------------------
You are in a Cave
Doors lead to: N S W 
You have: Diamonds- 0, Sapphires- 1, Rubies- 5, Arrows- 1
You find 8 rubies, here

Move, Pickup, or Shoot (M-P-S) or Q to Quit: m
Where to (N-E-S-W)?: n
Congratulations !!! You successfully completed the maze.
Your Bag contains: Diamonds- 0, Sapphires- 1, Rubies- 5, Arrows- 1
```
- If you end up in a cave that contains an alive monster, the 
monster can eat you. The game ends once the monster eats the
player:
```
----------------------------------------------------
You are in a Cave
Doors lead to: N E S W 
You have: Diamonds- 2, Sapphires- 0, Rubies- 0, Arrows- 0
You smell something Terrible nearby.

Move, Pickup, or Shoot (M-P-S) or Q to Quit: m
Where to (N-E-S-W)?: w
Chomp, chomp, chomp, you are eaten by an Otyugh!
Better luck next time
```
- If the player has exhausted all the arrows in their bag, 
the game shows the following message:
```
----------------------------------------------------
You are in a Tunnel
Doors lead to: N S 
You have: Diamonds- 2, Sapphires- 0, Rubies- 0, Arrows- 0
You smell something Bad nearby.

Move, Pickup, or Shoot (M-P-S) or Q to Quit: s
Shoot in which direction (N-E-S-W)?: s
Shoot how far? (1-5): 2
You are out of arrows, explore to find more
```

### **Design Changes**
- A separate Controller interface has been added for the 
Swing Controller.
- New methods have been added to keep track of players
visited locations and to restart a game using the same model.
- Player has been given the option to pick up a specified amount 
of items in the Command based game and to pick up all treasure 
or all arrows at a location in the GUI-based game.


### **Assumptions**
The model assumes few details in order to make the working less 
complicated. Few such assumptions are as follows:

- The command line inputs for Dungeon dimensions and properties will
always be non-negative integer values and boolean value for wrap
condition.
- The treasure amount assigned to each cave is randomly decided.
- The dungeon of dimensions less than 3X3 cannot be created.
- The game ends once the player reaches the End location or is
eaten by a monster.
- The player has 50% chance of making out alive from a cave
that contains an injured monster.
- Start and End location can be any cave within the dungeon
provided the minimum distance between them is 5 or more.
- The player can move only 1 step in any direction at a time 
provided the move is valid.
- If a player tries to move in a direction where a wall exists,
the player shall remain in the same location as before.
- If the player shoots an arrow through a tunnel that isn't a
straight line, the arrow changes the direction along the tunnel.
- The player can enter the Dungeon only at the predefined Start location.
- If any item is picked from any cave, it is not replenished.
- The player can pick any amount of treasure given that quantity
exists in the location.
- There are only 3 types of treasures available in any cave of the
dungeon.
- The monsters in the dungeon are always stationary i.e. they 
do not change their locations.
- The monsters will never be in a tunnel.
- There will always be a monster at the end location of the 
dungeon.
- -If an arrow is shot through the cave that houses a monster, but
the destination of the arrow is not the same cave, the monster
is not struck by it.
- It takes 2 arrow strikes to kill the monster.
- If there exist 2 monsters each at 2 positions away from the player,
the smell is similar to when there is one monster in the neighbouring 
cave.


### **Limitations**
The program has a few limitations that could be worked on in future versions:

- The player can move from Start location to End location 
without collecting any treasure.
- The player does not know the best action or path to reach the 
end location.


### **Citations**
Nil.