
      ___            ___        /  /\         /  /\         /  /\    
     /  /\          /__/\      /  /::\       /  /::\       /  /::\   
    /  /:/          \__\:\    /  /:/\:\     /  /:/\:\     /  /:/\:\  
   /  /:/           /  /::\  /  /:/  \:\   /  /::\ \:\   /  /:/  \:\ 
  /__/:/  ___    __/  /:/\/ /__/:/ \__\:| /__/:/\:\ \:\ /__/:/ \__\:\
  |  |:| /  /\  /__/\/:/~~  \  \:\ /  /:/ \  \:\ \:\_\/ \  \:\ /  /:/
  |  |:|/  /:/  \  \::/      \  \:\  /:/   \  \:\ \:\    \  \:\  /:/ 
  |__|:|__/:/    \  \:\       \  \:\/:/     \  \:\_\/     \  \:\/:/  
   \__\::::/      \__\/        \__\::/       \  \:\        \  \::/   
       ~~~~                        ~~         \__\/         \__\/    
      ___           ___           ___           ___     
     /  /\         /  /\         /  /\         /  /\    
    /  /::\       /  /::\       /  /::|       /  /::\   
   /  /:/\:\     /  /:/\:\     /  /:|:|      /  /:/\:\  
  /  /:/  \:\   /  /::\ \:\   /  /:/|:|__   /  /::\ \:\ 
 /__/:/_\_ \:\ /__/:/\:\_\:\ /__/:/_|::::\ /__/:/\:\ \:\
 \  \:\__/\_\/ \__\/  \:\/:/ \__\/  /~~/:/ \  \:\ \:\_\/
  \  \:\ \:\        \__\::/        /  /:/   \  \:\ \:\  
   \  \:\/:/        /  /:/        /  /:/     \  \:\_\/  
    \  \::/        /__/:/        /__/:/       \  \:\    
     \__\/         \__\/         \__\/         \__\/    

This is a game that I created. I think it's pretty fun, maybe you will too!
To start the game, navigate to mvc > game_controller_main, in which directory you will find a Game class. Run that file and you will be able to play this game that I created!

The name of this game is Asteroids. The instructions are as follows:

The goal of this game is for the falcon, which the player controls, to last for all 6 
currently available levels. Note that each level must be completed in a certain timeframe. To steer the falcon, use the left and right keys to rotate 
clockwise and counterclockwise, respectively, and the up key to thrust in the direction
in which the falcon is oriented. The space bar fires single bullets, which the falcon has
infinite supply of. The F key allows the falcon to fire special weapons, the D key to
initiate a "hyper", and the A key to initiate a shield. Other controls can be seen in
the welcome page. (The pause utility does not work very well... you'll see.)

Special weapons:
There are two special weapons in this game: Blooming shots and Cruise missiles.
The blooming shots generate bullets that will shoot in 12 directions around the Falcon. Every bullet is separated from 
one another by 20 degrees. Cruise missiles accelerate after they are fired and create shock waves that destroys
all foes that are engulfed within their perimeter.
The player is assigned 10 blooming shots and 5 Cruise missiles for each newly spawned Falcon. Too generous? You'll se why 
I did this soon enough.

Hyperspace: 
As a defensive measure, the falcon can leap to random positions on the screen with the help of the
"hyper" utility. This is used as an escape measure for the falcon when it is surrounded by foes. A 
temporary shield (which lasts for the same amount of time as the respawn shield) is given to the 
falcon when the hyper jump takes place to avoid the falcon being destroyed as soon as it reappears. 
Think of this as the falcon having sooooo much speed that it destroys whatever comes in its way during 
the jump.

Shield:
A shield can be used to provide protection for the falcon. When used as a utility, it lasts 3 times as 
long as the respawn shield and the Hyperspace shield. It destroys everything that comes in its way.


Foes:
Asteroids:
These are asteroids that have a white outline. They float around in space and will be destroyed when shot at or when it
collides with the falcon. Large asteroids break down into 2 medium ones, which in turn will further 
break into 3 smaller one. After the small ones explode, they break into debris. The large asteroids
give 1 point to the player, the medium ones 5, and the small ones 10.

StrongAsteroids:
These are asteroids that have a solid-white filling. They behave similar to the Asteroids, but they
take 3 collisions to destroy. Destroying these asteroids give double the points of the regular asteroids.

UFOs (+40 points):
UFOs appear after level 4. 
Be careful! They take 10 hits to destroy and will actively fire at the falcon.

Power-ups (+5 points):
The following power-ups are available to the falcon and will appear randomly throughout game play:

1UP (Green): Gives that falcon another life.

+10 Cruise Shots (Red): gives the falcon 10 extra cruise missiles.

+15 Blooming Shots (Cyan): gives the falcon 15 extra blooming shots.

Hyper (Orange): gives the falcon one extra hyper utility.

Shield (Blue): gives the falcon one extra shield.

The falcon will change color into those that each of these power-ups correspond to and 
fade back to its original white color. During this short period of time, the falcon cannot 
be destroyed by collisions.

Debris:
When an enemy is destroyed, yellow debris are formed and will expand in 12 directions, each 
20 degrees apart. Red debris are formed when the falcon is destroyed.
