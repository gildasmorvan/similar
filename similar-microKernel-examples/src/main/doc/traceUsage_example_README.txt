This example defines a usage example of the "Learning" simulation from the common libs. A "Learning" simulation defines
a simulation template where the complete trace of the simulation can be visualized. It gives access to the following values:
	- The content of a consistent or half consistent dynamic state of the simulation:
		- The value of the public local states and the global memory state of the agents;
		- The value of the public local states of the environment;
		- The state dynamics (persistent influences) of each level.
	- The parameters that were given to the various calls to the:
		- Perception, memory revision and decision methods of the agents;
		- Natural methods of the environment;
		- Reaction (regular and system) of the levels.
This simulation is especially usefull when a user wishes to study the execution mechanisms of a simulation.

This example uses this "Learning" simulation for a simulation model:
	- Containing one level:
		- which time model builds the next time stamp by adding 1 to the identifier of the previous time stamp;
		- having a full perception and influence relation graph (obviously since there is only one level);
	- Containing two agents:
		- An actor agent, which behavior modifies (with influences) the public local state of all the agents from 
			the level and the public local state of the environment in the level;
		- A procrastinator agent, which behavior does nothing;
	- Containing an environment modifying (with influences) the public local state of all the agents from 
			the level and the public local state of the environment in the level.
			
The main class of this simulation is the "MySimulationMainClass" class from the 
"fr.lgi2a.similar.microkernel.examples.traceusage1" package