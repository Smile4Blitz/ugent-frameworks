package be.ugent.reeks1.controller;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ugent.reeks1.services.Metrics;

import java.util.List;

@RestController
public class MetricsController {
    private final Metrics metrics;

    public MetricsController(Metrics metrics) {
        this.metrics = metrics;
    }

    @GetMapping("/actuator/metrics/{metric_name}")
    public CompositeMeterRegistry getMetrics(@PathVariable("metric_name") String metric_name, @RequestParam(name = "tag", required = false) List<String> tag) {
        return metrics.meterRegistry;
    }
}
