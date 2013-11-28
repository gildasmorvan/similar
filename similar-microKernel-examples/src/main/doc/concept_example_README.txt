This example illustrates how the concepts of the formal model of SIMILAR are applied to a concrete simulation.
Il also illustrates the best practices to follow when a simulation is designed (see the design guidelines 
of the micro-kernel).

MODEL OF THE SIMULATION:

This simulation is inspired from the pop culture rather than on a scientific subject to avoid complex 
domain-related information. It describes the evolution of a country where aliens can perform experiments 
on citizens.

The simulation contains three levels:
	- The space level, where:
		- the mothership of the aliens resides
		- aliens observe earth to find their experiment subjects (citizens) and decide to go on earth;
	- The earth level, where: 
		- the citizens live and can share on social networks information if they think they were the 
			subjects of alien experiments
		- the aliens land to perform experiments on citizen and take off when their experiments are finished
		- the FBI ensures that no citizen believes in a conspiracy of the government and hunts down aliens
	- The media (social) level, where:
		- an editor in chief defines the information being broadcasted on TV (a criteria helping citizen to identify 
			if they were the subjects of alien experiments);
		- the FBI censors an editor in chief broadcasting information that were not advised by them.
		
The main class of this simulation is the "ConceptsSimulationMain" class from the 
"fr.lgi2a.similar.microkernel.examples.concepts" package.