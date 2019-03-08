for gradient = 1:1:10 ;
    for intercept = -10:2:10
        s_array = runMultiple(fileprefix, 1, 'logisticGradientBasal', gradient, 'logisticGradientTuft' , gradient, ...\
            'logisticInterceptBasal', intercept, 'logisticInterceptTuft', intercept) ;
        figure ; surf(s_array) ; title(['g = ', num2str(gradient), ' i = ', num2str(intercept)]) ;
    end
end