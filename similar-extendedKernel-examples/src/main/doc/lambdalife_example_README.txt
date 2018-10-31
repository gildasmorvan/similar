This example illustrates how a modified version of the game of life simulation can be implemented using the
extended-kernel, the common libraries and the extended libraries.

MODEL OF THE SIMULATION:

***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
***** WHAT IS THE GAME OF LIFE?
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
**		The game of life (or simply life) is a cellular automaton (CA) invented by J.H. Conway.
** 		In its original definition, the universe consists in an infinite grid of square cells
**		that have two states: dead or alive. However, for practical reasons, in this model we consider
**		finite toroidal or bounded universes.
**		The Moore neighborhood is used: each cell has 9 neighbors, itself and the 8 adjacent cells.
**		Cells evolve in parallel according to the following rules:
**			o 	if a cell is dead and has three living neighbors or is alive and has two or three living 
**				neighbors, it will be alive at the next step;
**			o	in other cases, it will be dead.
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****

***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
***** WHAT IS THE LAMBDA VALUE OF THE GAME OF LIFE?
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
**		One cruel and ironic aspect of the GoL  is that, starting from a random initial configuration (soup),
**		a cell has generally little chance to remain alive in the long run.
**		The resulting board is composed of small still lifes (stable patterns) and 1-period
**		oscillators (blinkers) in a 2/1 ratio. The density of living cells is approximatively
**		equal to 0.0287, with an important local variability.
**		This behavior is predictable knowing the lambda parameter of the Life.
**		lambda is a complexity measure of cellular automata introduced by Langton that depends on
**		the number of cell states K, the neighborhood N and the number n of transitions to a
**		quiescent state Sq in the transition function such as lambda = 1 - n/K^N, 
**		with K = 2, N = 9 and n = binom(8,2) + 2 * Sum for i = 0,1,4-8  binom(8,i) for Life.
**		binom(i,j) denotes  the number of j-combinations in a set of i elements:
**		binom(i,j) = i!/((i-j)!*j!)
**		Thus, the value of the lambda parameter of life is equal to 0.2734375.
**		As Langton notes notes, for lambda  values approximatively equal to 0.25:
**		"structures of period 1 appear. Thus, there are now three different possible outcomes
**		for the ultimate dynamics of the system, depending on the initial state. The dynamics
**		may reach a homogeneous fixed point consisting entirely of state Sq, or it may reach
**		a heterogeneous fixed point consisting mostly of cells in state Sq with a sprinkling
**		of cells stuck in one of the other states, or it may settle down to periodic behavior".
**		from Langton, Computation at the edge of chaos: Phase transitions and emergent computation,
**		Physica D: Nonlinear Phenomena, 42(1-3), pp 12-37, 1990
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****

***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
***** WHAT IS THE LAMBDA LIFE MODEL ?
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
**		To improve lambda value of Life, one needs to change the rules of the game.
**		In this model, lambda is regarded as a macroscopic parameter, explicitly introduced in the model and
**		independent from cell behaviors: State changes of dying cells are not taken into account with a
**		probability p such as n = binom(8,2) + (2 - p) * (sum for i = 0,1,4-8 binom(8,i)).
**		Thus, their is a simple linear relation between p and lambda: p = (lambda - 0.2734375)/0.3359.
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****

***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
***** WHAT IS THE GOAL OF THE LAMBDA LIFE MODEL ?
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
**		A still life is for a given CA, is a pattern that does not change from one generation to the next. 		
**		Finding maximum density still lifes in the game of life is a complicated problem: the search space
**		for a nxn grid has size 2^(n*n). This problem was then used as a case study in constraint programming.
**		The goal of the lambda life model is to find approximate solutions to this problem in bounded universes. 
**		For lambda in [0.48, 0.6], the number of dying cells (and more generally the energy of the system) tends
**		to decrease in time and large structures of vertical or horizontal rows eventually emerge, shaped by
**		moving groups of switching state cells that seem to work at their boundaries, and eventually vanish when
**		the board becomes a dense still life of density approximatively equal to 0.5.
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****


The simulation contains one level:
	- The "micro" level, where:
		- "cell" agents reside and behave as classical life cells.
		- The environment manages the topology of the cell grid.
		- the reaction function applies state changes according to p.
		
The simulation contains four specific probes located in the
"fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.probes" package
	- MacroStateProbe prints the density and energy of the system at each simulation step to
	  the specified target (e.g., a file or System.out).
	- LambdaGameOfLifeLastStateExporter exports the grid state of the last simulation step as
	  a png file.
	- LambdaGameOfLifeSwingView displays the game of life grid as a Swing JPanel as a function
	  of the current and next states of the cells using the following color code:
	  	- alive to alive: BLUE
	  	- dead to dead: background color
	  	- alive to dead: RED
	  	- dead to alive: GREEN
	- LambdaGameOfLifeHistorySwingView displays the game of life grid as a Swing JPanel as a function
	  of the current state  of the cells and the simulation step at which the last change occurs.


The main class of this simulation is the "LambdaLifeMain" class from the 
"fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife" package. 