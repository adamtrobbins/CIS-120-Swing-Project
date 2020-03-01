=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: adamrobb
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections
  Teams use a Set (implementation TreeSet) to remember which territories they own at a given time. 
  This is superior to an array because territories are constantly conquered and lost, meaning
  resizing is often necessary. It being a set is helpful because it enforces that there can be
  no duplicate territories, each should only be represented once. Territories themselves also
  Contain a Set (implementation TreeSet) of neighboring territories, so that they can easily verify
  if a territory is their neighbor (thanks to TreeSet's implementation being a sorted tree) using
  the contains() method. This is used to ensure that players cannot attack a territory from across
  the map (for example, from Australia to Egypt), so each territory must know which are adjacent.
  Finally, the WorldMap class, which displays the map, has a LinkedList of territories because
  the order is important to maintain when reading and writing to files so nothing gets mismatched.
  Because all that needs to be done is iteration and ordering, the LinkedList is the simplest
  solution as ArrayList would have been unnecessary (since individual indices don't need to be 
  accessed).
  To make all this work I implemented the comparable interface for territories by comparing their
  names (using compareTo()) because I know that no two territory names will ever be exactly 
  identical.

  2.File I/O
  I use file I/O to read and write to files. The game has a save file (saveFile.txt) where games
  are stored when the save button is clicked. This writes all pertinent game state data to this
  file, which can then be used later to continue a game (file writing). There is also a load save 
  button, which loads the data from this save file to the current game (file reading). These are
  accomplished by storing the data, namely current turn, current scores, territory ownership
  and troop counts, in one place. The files can also be edited manually to create your own unique
  scenarios: to see which line number corresponds to which province, consult inputFileGuide.txt. 
  The use of I/O makes keeping data across different runs possible and makes manipulation of the
  starting simple.

  3.Inheritance/Subtyping for Dynamic Dispatch
  This is used to handle the three types of territories. I used the abstract class Territory
  as their parent because they share much of the exact same implementation (some core code for
  attacking, keeping track of neighbors, some display information, etc.) while several important
  methods were abstract which mandated that the subclasses implement them: 1) The amount of troops
  which can be drafted from the territory 2) The amount of score received for taking the territory
  3) The mechanics of battles which unfold on that territory (generateBattle() method specifically)
  and of course 4) the display of the province itself. The three subclasses which extend Territory
  are Province, City, and Capital, each with their own unique mechanics. However, they are all
  lumped together in many collections (as described in 1) so dynamic dispatch determines how each
  will behave, for example, when a set is iterated over to calculate something or a battle is being
  generated on a given territory. Note: paintComponent is not actually abstract because doing that 
  would prevent the super call to JComponent from being possible, so that is simply normally 
  overriden in all three.

  4.Testable Component
  The game state is largely stored in the Game and WorldMap classes, and there are methods there
  which make accessing province owners, province troop amounts, etc. easy to access. Further, the 
  map has a handleClick method, which makes simulating mouse clicks very easy to test drafting/
  attacking provinces, etc. The buttons also call methods in Game, so those can be directly accessed
  very easily too. With attacking provinces, I added a feature of dice rigging which allows the dice
  rolls to be "rigged" with certain preset values, which makes testing different scenarios far
  easier since rolls are usually random (and always are in the actual game). I wrote many JUnit 
  tests to test the intended behavior of the different classes and also make sure edge cases(invalid
  inputs, selecting an invalid province, selecting the ocean/nothing) are handled appropriately and
  do not derail the course of the game. I also use File I/O to easily construct, load, and test
  new scenarios which makes testing far easier. Tests were useful when refactoring to ensure nothing
  got broken.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Battle: Handles battles, when one territory attacks another one. Rolls dice and transfers the 
province if the attacker wins. 

BattleDice: Contains both attacker and defender dice, to be used and displayed during battles. The
values of the dice are used by the battle class to process battles.  

Capital: A subclass of Territory which defines its unique behavior, including a much larger bonus in 
points for conquering, higher contribution to total draftable troops, and a capital defense mechanic
which makes attacking capitals owned by their "true" owner harder than attacking normally. Also has 
a unique display.

City: A subclass of Territory which defines its unique behavior, including a slightly larger bonus  
in points for conquering, slightly more contribution to total draftable troops, and different
mechanics for attacking, making assaulting cities more difficult than generic territories. Also has 
a unique display.

Die: Represents a single die, is used in DieSet to form the dice for each team. 

DieRollGenerator: Generates dice rolls. In the actual game always returns a random # from 1-6, but
for debugging purposes allows the rigging of dice so desired outcomes can be tested.

DieSet: Composed of several dice, which are then rolled and sorted (for comparison later)

Game: Has the main method and is the primary control center for the game, controlling phase, saving,
and loading files. 

GamePhase: An enum representing which phase of the game it is, with associated helpful methods.
 
LineSegment: Represents a line segment between to points, primarily used to determine if a it is
intersected by a ray, helpful for determining if a point is in a given territory. 

NumLabel: a subclass of JLabel which makes representing numbers easier

Pgon: my custom class for representing a polygon with an array of points, used in the Territory 
abstract class. 

Point: represents a single point in space, used in geometry classes

Province: most generic subclass of Territory, with lowest amounts for score, troops drafted, and
standard behavior during battles. 

Team: Represents one of the teams in the game, either red or blue and relevant state. 

Territory: Represents one of the territories on the map and relevant state and display information

Vector: Represents a direction and a magnitude, with helpful methods for doing vector operations. Is
helpful for the display of territories (through Pgons)

WorldMap: Displays the entire map canvas, and handles when the user clicks on a location on the map
to route it to the appropriate territories and methods.  


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  I originally found it difficult to allow events within classes (one side wins a battle, no more
  troops can be drafted, etc.) to communicate with the overall game state. Before refactoring, the
  solutions I used were messy, convoluted, and did not maintain encapsulation well. Knowing just
  how each component needed to interact made it easier when redoing code to do a better job. 


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  Overall I am pleased with the end design. I think it does a good job of representing state within
  the parameters of the game functionality. I tried to encapsulate as much as was possible while
  still allowing for necessary communication between all of the different classes. Separation of
  function between classes proved very helpful for keeping things neat. If I were given a chance
  to refactor further, I might consider simplifying and generalizing teams, to enable there to be
  more than two teams at a given time. The mandating of two teams is probably not entirely ideal,
  but works well for these purposes.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  https://en.wikipedia.org/wiki/Point_in_polygon (used to get the idea for the algorithm, all of the
  code is mine however)
