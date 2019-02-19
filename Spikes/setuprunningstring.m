% location of all files etc for this run
fileprefix = '"/Users/lss/Documents/workspace/PyramidalCells/Test_nov2018/" ' ;
% all the rest of the flags
commandflags = '-d "driving_6synapses.csv" -c "context_6synapses.csv" -s 5000 -t 5.0 -wd drivingsynapses_6.txt -wc contextsynapses_6.txt -alpha_driver 1000 -alpha_context 300 -apical_multiplier 8 -p_refractory_period 0.01 -i_refractory_period 0.01 -t_basal 0.01 -t_apical 0.1 -t_inhib 0.05 -n "networkconfig.txt"  -sout "t1outSpikes.csv"' ;
% actual command to run opyramidal neuron simulation
commandtorun = 'java -jar pyramidal.jar ' ;
fp = '/Users/lss/Documents/workspace/PyramidalCells/Test_nov2018/' ;
cf1 = 'c "context_6synapses.csv" -s 5000 -t 5.0 -wd drivingsynapses_6.txt -wc contextsynapses_6.txt -alpha_driver 1000 -alpha_context 300 -apical_multiplier 8 -p_refractory_period 0.01 -i_refractory_period 0.01 -t_basal 0.01 -t_apical 0.1 -t_inhib 0.05 -n "networkconfig.txt"  -sout "t1outSpikes.csv"' ;
cf2 = '-s 5000 -t 5.0 -wd drivingsynapses_6.txt -wc contextsynapses_6.txt -alpha_driver 1000 -alpha_context 300 -apical_multiplier 8 -p_refractory_period 0.01 -i_refractory_period 0.01 -t_basal 0.01 -t_apical 0.1 -t_inhib 0.05 -n "networkconfig.txt"  -sout "t1outSpikes.csv"' ;

