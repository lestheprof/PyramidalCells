function plotspikes(filename)
% plotspikes plot spikes from java pyramidal simulation
%   Should ask for start time, end time, and possibly a selection of neurons to
%   plot. For now, it just plots all of them over time, with nothing
%   plotted on the X axis, or at the top of the graph.
    m1 = csvread(filename) ;
    plot(m1(:,2), m1(:,1), '.') ;
    % adjust y valus to meake plot easier to read
    ylim([0 max(m1(:,1))+1] );
    xlabel('time') ;
end

