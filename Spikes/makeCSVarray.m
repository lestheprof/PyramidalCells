function [csvarray] = makeCSVarray(inputarray, neuronnumber, synapsenumber, varargin)
%makeCSVarray convert an array into an N by 3 array nearly ready for saving
%as a .csv file
%
% to create the csv file, 
%   0: use createSpikeTrain or other function to create a 1-dimensional array
%   of times of spikes. 
%   1: use makeCSVarray to turn this 1-D array into a no_of_spikes by 3 array, 
%   with element 1 being neuron and element 2 being synapse, as often as required
%   2: use vertcat to turn all the csv arrays into one single array
%   3: use sortrows(array, 3) to sort into time order
%   4: use csvwrite to write the sorted array to the appropriate location.
%
csvarray = zeros([length(inputarray), 3]) ;
csvarray(:,1) = neuronnumber ;
csvarray(:,2) = synapsenumber ;
csvarray(:,3) = inputarray ;
end

