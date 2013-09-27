package com.englishtown.vertx.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link VerticleGauges}
 */
@RunWith(MockitoJUnitRunner.class)
public class VerticleGaugesTest {

    JsonObject config = new JsonObject();

    @Mock
    Vertx vertx;
    @Mock
    Container container;
    @Mock
    Verticle verticle;
    @Mock
    MetricRegistry registry;

    @Before
    public void setUp() throws Exception {
        when(container.config()).thenReturn(config);
        when(verticle.getVertx()).thenReturn(vertx);
        when(verticle.getContainer()).thenReturn(container);
    }

    @Test
    public void testRegister() throws Exception {
        new VerticleGauges(verticle, registry);
        verify(registry, times(3)).register(anyString(), any(Gauge.class));
    }

    @Test
    public void testRegisterValues() throws Exception {

        Map<String, Object> values = new HashMap<>();
        values.put("bool", true);
        values.put("str", "hello");

        new VerticleGauges(verticle, registry, values);
        verify(registry, times(5)).register(anyString(), any(Gauge.class));

    }
}