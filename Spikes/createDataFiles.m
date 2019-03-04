function createDataFiles(minfrequency,maxfrequency, N, neuronid, duration, jitterpercent, fileprefix, filenameroot)
%createDataFiles create a set of data files 
%   creates a set of data files suitable for the spiking neural
%   netsimulator. Each is at a constant spiking frequency, starting at
%   minfrequency, and going up to maxfrequency, inclusive (so N >= 2). 
%   duration is the length of the trains in seconds. The
%   jitter is a percentage of the inter-spike interval. Files are written
%   out to [fileprefix filenameroot '_' num2str(i) '.csv']
%
% LSS started 2 March 2019
%   last uodated 3 March 2019 LSS
%
% calculate the frequencies of the spike trains
frequarray = minfrequency : (maxfrequency - minfrequency)/(N-1) :  maxfrequency ;

% create the spike trains. Currently all to synapse 1
for trainno = 1:N
    train = createSpikeTrain(frequarray(trainno), frequarray(trainno), duration, 'jitter', (jitterpercent/100.0)/frequarray(trainno)) ; 
    train_c =  makeCSVarray(train, neuronid, 1) ;
    train_sorted = sortrows(train_c, 3) ;
    % write to file
    csvwrite([fileprefix filenameroot '_' num2str(trainno)], train_sorted) ;
end

end

