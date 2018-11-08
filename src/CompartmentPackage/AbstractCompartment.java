package CompartmentPackage;

import java.util.ArrayList;

/**
 * Abstract compartment underlying all neuronal compartments. 
 * @author lss
 */
import java.util.List;

import NeuronPackage.AbstractNeuron;
import SynapsePackage.InternalSynapse;

public abstract class AbstractCompartment {

	public AbstractNeuron myNeuron ;
	public double activation ;
	public int compartmentID ;
	public double samplingInterval ;
	public int samplingrate ;
	public String compartmentType = null ;
	
	protected List <InternalSynapse> incomingSynapses  = null;
	
	/**
	 * 
	 * @param neuron neuron to which this compartment belongs
	 * @param compartmentID ID of this compartment
	 */
	public AbstractCompartment(AbstractNeuron neuron, int compartmentID) {
		// TODO Auto-generated constructor stub
		this.activation = 0 ; // initialise the activation, here in this abstract version, to 0
		this.myNeuron = neuron ;
		this.compartmentID = compartmentID ;
		this.samplingInterval = neuron.samplingInterval ;
		this.samplingrate = neuron.samplingRate ;
	}
	
	public int getNeuronID(){
		return myNeuron.neuronID ;
	}
	
	/**
	 * add an incoming synapse to this compartment: these are internal synapses
	 * @param syn synapse object
	 */
	public void addIncomingSynapse(InternalSynapse syn){
		if (incomingSynapses == null)
			// create the arraylist
			incomingSynapses = new ArrayList<InternalSynapse>() ;
		incomingSynapses.add(syn) ;
	}
	
	/*
	 * CurrentTime current simulation time
	 * null: called by subclasses
	 */
	public void run(double currentTime){
	}

}
