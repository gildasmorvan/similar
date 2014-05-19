This example illustrates how a modified version of the game of life simulation can be implemented using the
extended-kernel, the common libraries and the extended libraries.

It extends the "Lambda Life" model implemented in the fr.lgi2a.similar.extendedkernel.examples.lambdalife
package.

MODEL OF THE SIMULATION:

***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
***** WHAT IS THE DENSITY CONTROLLED LIFE MODEL ?
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
**		One cruel and ironic aspect of the GoL  is that, starting from a random initial configuration (soup),
**		a cell has generally little chance to remain alive in the long run.
**		The resulting board is composed of small still lifes (stable patterns) and 1-period
**		oscillators (blinkers) in a 2/1 ratio. The density of living cells is approximatively
**		equal to 0.0287, with an important local variability.
**		The goal of this multi-level model is to keep Life boards at the desired density rho+, by controlling
**		the proportion of dying cells at the mesoscopic level, to account for the natural variability of density
**		between regions of the grid.  Moreover, the control should affect as less as possible simulations at the
**		microscopic level and, to keep it simple, should be tuned by a single linear parameter such as lambda
**		in the "Lambda Life" model.  
**		Therefore, the model uses a simple proportional controller: if the density is lower than a treshold,
**		state changes of dying cells are not taken into account with a probability p such as p = Kp * epsilon,
**		where Kp is the proportional controller parameter and epsilon the difference between expected and actual
**		densities. The Kp parameter has to be carefully tuned to run realistic simulations:  too small
**		simulations do not achieve the desired solution, too big the board density tends to oscillate
**		and  the number of micro influences not taken into account becomes too important. However, for
**		appropriate Kp values, this simple linear controller achieves good results and allows to find a good
**		compromise between conflicting micro and meso knowledge.
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****

The simulation contains two levels:
	- The "micro" level which comes from the "lambda life model"
	- The "meso" level where:
		- "cell clusters" agents reside and computes the p value associated to the cluster.
		- The environment manages the topology of the cell cluster grid.

The main class of this simulation is the "DensityControlledLifeMain" class from the 
"fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife" package. 