function [outputarray] = createSpikeTrain(startrate, endrate, duration, varargin)
% createSpikeTrain creates an array of spike times
% a start rate and a
% finioshing rate and a period of duration are supplied. 
% Initial period (default 0.1, settable using varargin) has no spikes, and
% is NOT included in duration.
% spikerate varies from start to finish: use same value twice to keep
% constant
% jitter determines timing noise: should be small compared to spike ISI.
% Can (obviously) be used for driving or contextual input.

initialperiod = 0.1  ; % no spikes here 
jitter = 0 ; % default is no jitter

i=1 ;
while(i<=size(varargin,2))
    switch lower(varargin{i})
        
        case 'initialperiod'
            initialperiod = varargin{i+1}; 
            i=i+1 ;
        case 'jitter'
            jitter = varargin{i+1}; 
            i=i+1 ;
        otherwise
            error('createSpikeTrain: Unknown argument %s given',varargin{i});
    end
    i=i+1 ;
end

endtime = initialperiod + duration ;
ISIstart = 1.0/startrate ;
ISIend = 1.0/endrate ;
meanISI = (ISIstart + ISIend)/2 ;
nspikes = ceil( duration /meanISI) ;
deltaISI = (ISIstart - ISIend)/nspikes ;

% now create the array
outputarray = zeros([1 nspikes]) ;
outputarray(1) = initialperiod ;
for spikeno = 2:nspikes
    currentISI = 0 ;
    while (currentISI <= 0) % ensure spike times are strictly increasing
        currentISI = (ISIstart - (spikeno - 1) * deltaISI  + random('Normal', 0, jitter)) ;
    end
    outputarray(spikeno) = outputarray(spikeno - 1) + currentISI ;
end
% sanity check
if (endtime < outputarray(nspikes))
    disp(['createSpikeTrain: last spike time exceeds expected duration. duration = ' num2str(duration) ' last spike time = ' num2str(outputarray(nspikes))]) ;
end
end

