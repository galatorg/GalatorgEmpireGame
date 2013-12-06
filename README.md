GalatorgEmpireGame
==================

The Galatorg Empire Game

I'll eventually put a better description of the game here as I get the engine further along.  Ultimately, you will
customize your character(2D) a little bit, mostly being able to adjust colors and skin(male/female/ghost/alien/custom)
as well as name.  You start off in a 2.5D section of The Galatorg Empire where you start to learn what you are doing
there and how to start off your journey.  You go through the first trial(like a tutorial) in The Galatorg Empire to
obtain your first level of power, which allows you to start your own world.  Your own world is 2D.  When you go to it
for the first time, it is just a gate on a small floating island of dirt and grass.  You will be able to add blocks to
your world as you gather them from other realms.  You can only build a certain distance out from the gate(the gate cannot
be moved).  This distance is increased as you gain more levels of power by completing realms that you reach through the
gate.  This gate is your portal into other realms that progress the game.  One of the initial destinations is connecting
to the main Galatorg Empire game server, which is a good place to socialize with people.  The gate can also be opened
to allow multiplayer with friends for a direct connect option, you can then journey through other realms together.  Going
to the Galatorg Empire server is much like direct connecting to a friend, but it's a dedicated server.  Other dedicated
servers can also be setup by anyone.  Servers can setup gates on them to allow players to travel to other servers also.

2013/12/06
-----
Finished commenting all the code even though it pained me to comment some of it that I knew was going to be completely
replaced.  Now that the commenting is done I plan on getting the spritesheet system upgraded to accept multiple spritesheets
and sprites of different sizes.  Afterwards, I may look into upgrading the color system to include more colors because it
will currently only let you choose from 216(6*6*6) different colors.  Another potential thing that may be upgraded is the
number of colors that a tile can use, probably an "EnhancedTile" class that will hold 8 different colors instead of 4.  It
just means that the color system needs to be rethought a bit.  I really like the ability to easily reuse tiles by using
different colors though.  While commenting the code I started watching another game programming series that is very similar
to the one mentioned in the previous update.  This one covers more, is a better pace, and has good explanations too.

http://www.youtube.com/playlist?list=PLlrATfBNZ98eOOCk2fOFg7Qg5yoQfFAdf


2013/08/27
-----
I've been thinking about how this story and game would go for a very long time.  I didn't particularly want to write it in
java, but it's the best option to make it available easily in a browser also.  I want people to be able to play it easily.
The initial commit is everything that I put together while following the tutorial at:

http://www.youtube.com/playlist?list=PL8CAB66181A502179

It's pretty much the same as what he makes, but I used 32 pixel wide tiles instead of 8.  For the most part I just followed
along, while knowing where I was going to end up changing and/or expanding upon much of it.  I'll be finishing up
commenting all the code, then I will start to improve everything and add more functionality to finish the engine that I
will need.  To make it more compatible with anyone that may have been following the tutorial before, I will be starting
by making it so you can load in various spritesheets of different sizes.  Also, a "New Character/Play Game/Options" window.
Many ideas and I know how to do them, just need to spend time on it.
