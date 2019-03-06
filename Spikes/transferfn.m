function outval = transferfn(B,D,k1, k2,m, logistigradient, logisticintercept)
%transferfn test the transfer function
%   Detailed explanation goes here
% B is basal weighted sum
% D is apical tuft weighted sum
% k1, k2 are the values from Kay and phillips 2011
% m is multipler in apical dendrite
%
plot = true ;

outval = zeros([length(B) length(D)]) ;
B1 = logistic(B, logistigradient, logisticintercept) ;
D1 = logistic(D, logistigradient, logisticintercept) ;
% B and D can be 1D arrays
for bval = 1:length(B)
    for aval = 1: length(D)
        outval(bval, aval)  = B1(bval) * ( k1 + (1-k1) * exp(k2 * B1(bval) * D1(aval) * m)) ;
    end
end

if plot
    figure;
    surf(outval);
    ylabel('basal activation (scaled)') ;
    xlabel('apical tuft activation (scaled)') ;
end



end

function y =  logistic(x, gradient, intercept)
y = 1 ./ (1 + exp(- (gradient * x - intercept))) ;
end

