package com.riders.thelab.core.worker;

import androidx.work.DelegatingWorkerFactory;

import com.riders.thelab.data.remote.LabService;

//@Singleton
public class LabDelegatingWorkerFactory extends DelegatingWorkerFactory {

    //    @Inject
    public LabDelegatingWorkerFactory(LabService service) {
        addFactory(new LabWorkerFactory(service));
        // Add here other factories that you may need in your application
    }
}
