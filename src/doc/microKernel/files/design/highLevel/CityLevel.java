/**
 * The model of the "City" level.
 */
public class CityLevel implements ILevel {
	/**
	 * The unique identifier of the level.
	 */
	private final LevelIdentifier identifier;
	/**
	 * The out neighborhood of this level in the perception relation graph (<i>i.e.</i> the 
	 * levels that can be perceived from this level).
	 */
	private Set<LevelIdentifier> perceptionRelations;
	/**
	 * The out neighborhood of this level in the influence relation graph (<i>i.e.</i> the 
	 * levels that can be influenced by this level).
	 */
	private Set<LevelIdentifier> influenceRelations;
	/**
	 * The most recent consistent state of this level.
	 */
	private ConsistentPublicLocalDynamicState mostRecentConsistentState;
	
	/**
	 * Builds an half-initialized level. This constructor defines the value of:
	 * <ul>
	 * 	<li>The identifier of the level.</li>
	 * 	<li>The time stamp of the last consistent dynamic state of the level.</li>
	 * </ul>
	 * To complete the initialization of the level, the following elements have to be specified in
	 * the last consistent state of the level:
	 * <ul>
	 * 	<li>The initial public local state of the environment;</li>
	 * 	<li>The public local state of the agents initially lying in the level have to be added;</li>
	 * 	<li>The persistent influences initially lying in the level have to be added.</li>
	 * </ul>
	 * @param initialTime The initial time stamp of the simulation.
	 */
	public CityLevel( SimulationTimeStamp initialTime ) {
		this.identifier = MyAwesomeLevelList.CITY;
		this.perceptionRelations = this.buildPerceptionRelations( );
		this.influenceRelations = this.buildInfluenceRelations( );
		this.mostRecentConsistentState = new ConsistentPublicLocalDynamicState(
				initialTime, 
				this.getIdentifier()
		);
	}
	
	/**
	 * Builds the perception relation graph of this level.
	 */
	private final Set<LevelIdentifier> buildPerceptionRelations( ) {
		Set<LevelIdentifier> result = new HashSet<LevelIdentifier>();
		// First add the identifier of this level to the graph.
		result.add( this.getIdentifier() );
		// Then add to the set the identifier of the perceptible levels.
		result.add( MyAwesomeLevelList.SLUMBS );
		return result;
	}
	
	/**
	 * Builds the influence relation graph of this level.
	 */
	private final Set<LevelIdentifier> buildInfluenceRelations( ) {
		Set<LevelIdentifier> result = new HashSet<LevelIdentifier>();
		// First add the identifier of this level to the graph.
		result.add( this.getIdentifier() );
		// Then add to the set the identifier of the influenceable levels.
		result.add( MyAwesomeLevelList.SLUMBS );
		result.add( MyAwesomeLevelList.COUNTRY );
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LevelIdentifier getIdentifier() {
		return this.identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LevelIdentifier> getPerceptibleLevels() {
		return this.perceptionRelations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LevelIdentifier> getInfluenceableLevels() {
		return this.influenceRelations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConsistentPublicLocalDynamicState getLastConsistentPublicLocalDynamicState() {
		return this.mostRecentConsistentState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastConsistentPublicLocalDynamicState(
			ConsistentPublicLocalDynamicState lastConsistentState
	) {
		this.mostRecentConsistentState = lastConsistentState;
		
	}
	
	
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //																// //
	// //	The remaining methods are managed in a later step of the simulation design process.	 			// //
	// //																// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationTimeStamp getNextTime(SimulationTimeStamp currentTime) {
		throw new UnsupportedOperationException( "This operation currently has no specification." );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			Set<IInfluence> remainingInfluences
	) {
		throw new UnsupportedOperationException( "This operation currently has no specification." );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeSystemReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Collection<IInfluence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			Collection<IInfluence> newInfluencesToProcess
	) {
		throw new UnsupportedOperationException( "This operation currently has no specification." );
	}
}