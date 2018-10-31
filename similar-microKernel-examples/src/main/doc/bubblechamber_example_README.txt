This example illustrates how a "bubble chamber" simulation can be implemented using the micro-kernel and
the common libraries.

MODEL OF THE SIMULATION:

***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
***** WHAT IS A BUBBLE CHAMBER ?
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****
**		A bubble chamber is a vessel filled with a superheated transparent liquid (most often liquid hydrogen) used 
**		to detect electrically charged particles moving through it.
**		[...]
**		As particles enter the chamber, a piston suddenly decreases its pressure, and the liquid enters into a 
**		superheated, metastable phase. Charged particles create an ionisation track, around which the liquid vaporises, 
**		forming microscopic bubbles.
***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****

The simulation contains two levels:
	- The "chamber" level, where:
		- "particle" and "bubble" agents reside
		- Rather than telling that the particles 'decide' to move, we chose a more
			realistic approach where the moves of the particles come from physical laws
			and therefore are the result of the natural action of the environment;
		- The environment manages both the moves of the particles (according to their
			location, speed and acceleration) and the apparition of bubbles on their
			path;
	- The "external" level, where:
		- A "cannon" agent fires periodically "particles" into the "chamber" level;
		- A "cannon" heats up as it fires and cannot fire when in an overheated state;
		- The natural action of the environment reduces the temperature of the
			cannons according to the ambient temperature.
		
The main class of this simulation is the "BubbleChamberMain" class from the 
"fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber" package.