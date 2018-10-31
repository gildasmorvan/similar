This example illustrates how a modified version of the lambda life simulation can be implemented using the
extended-kernel, the common libraries and the extended libraries.

It extends the "Lambda Life" model implemented in the fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife
package.

***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
***** WHAT IS THE LAMBDA ENERGY LIFE MODEL ?
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
**		An issue of the lambda life model is its heavy tailed behavior. For a given initial soup, the grid can
**		be trapped into a state that will not lead quickly to a still life.
**		The lambda energy life model extends the lambda life model but introduces a top-down feedback between
**		a macroscopic property (the energy of the system) and lambda: When the energy of the system lowers to
**		a treshold, lambda decreases linearly to 0.2734375. 
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****

The simulation contains one level:
	- The "micro" level, where:
		- "cell" agents reside and behave as classical life cells.
		- The environment manages the topology of the cell grid.
		- the reaction function applies state changes according to p and the energy of the grid.

The main class of this simulation is the "LambdaEnergyLifeMain" class from the 
"fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdaenergylife" package.