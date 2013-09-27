package com.englishtown.vertx.metrics;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.Map;

/**
 * Metric utilities to set up defaults for a Verticle
 */
public class Utils {

    public static JmxReporter create(Verticle verticle, MetricRegistry registry, JsonObject config) {
        return create(verticle, registry, null, config);
    }

    public static JmxReporter create(Verticle verticle, MetricRegistry registry, boolean jmxReporter) {
        return create(verticle, registry, null, jmxReporter);
    }

    public static JmxReporter create(Verticle verticle, MetricRegistry registry, Map<String, Object> values, JsonObject config) {
        return create(verticle, registry, values, config.getBoolean("jmx-reporter", false));
    }

    public static JmxReporter create(Verticle verticle, MetricRegistry registry, Map<String, Object> values, boolean jmxReporter) {

        try {
            new VerticleGauges(verticle, registry, values);
        } catch (Throwable t) {
            verticle.getContainer().logger().warn("Error creating VerticleGauges", t);
        }
        try {
            new VertxEventLoopGauges(verticle.getVertx(), verticle.getContainer(), registry);
        } catch (Throwable t) {
            verticle.getContainer().logger().warn("Error creating VertxEventLoopGauges", t);
        }

        if (jmxReporter) {
            JmxReporter reporter = JmxReporter.forRegistry(registry).build();
            reporter.start();
            return reporter;
        }

        return null;

    }

}