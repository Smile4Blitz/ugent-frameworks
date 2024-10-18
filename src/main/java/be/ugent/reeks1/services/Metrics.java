package be.ugent.reeks1.services;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

@Service
public final class Metrics {
    public final CompositeMeterRegistry meterRegistry;

    private final Counter createCounter;
    public final void increaseCreate() { this.createCounter.increment(); }
    public final double getCreate() { return this.createCounter.count(); }
    
    private final Counter readCounter;
    public final void increaseRead() { this.createCounter.increment(); }
    public final double getRead() { return this.readCounter.count(); }
    
    private final Counter readCollection;
    public final void increaseReadCollection() { this.readCollection.increment(); }
    public final double getReadCollection() { return this.readCollection.count(); }    

    private final Counter updateCounter;
    public final void increaseUpdate() { this.createCounter.increment(); }
    public final double getUpdate() { return this.updateCounter.count(); }
    
    private final Counter deleteCounter;
    public final void increaseDelete() { this.createCounter.increment(); }
    public final double getDelete() { return this.deleteCounter.count(); }

    public Metrics() {
        this.meterRegistry = new CompositeMeterRegistry();

        this.createCounter = Counter.builder("blogpost.create")
            .tag("operation", "create")
            .description("Number of blogposts created.")
            .register(meterRegistry);

        this.readCounter = Counter.builder("blogpost.read")
            .tag("operation", "read")
            .description("Number of blogposts read.")
            .register(meterRegistry);

        this.readCollection = Counter.builder("blogpost.readCollection")
            .tag("operation", "readCollection")
            .description("Number of blogpost collections read.")
            .register(meterRegistry);

        this.updateCounter = Counter.builder("blogpost.update")
            .tag("operation", "update")
            .description("Number of blogposts updated.")
            .register(meterRegistry);

        this.deleteCounter = Counter.builder("blogpost.delete")
            .tag("operation", "delete")
            .description("Number of blogposts deleted.")
            .register(meterRegistry);
    }
}