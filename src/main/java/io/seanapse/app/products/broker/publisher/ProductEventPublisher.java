package io.seanapse.app.products.broker.publisher;

import io.seanapse.app.products.domain.event.ProductEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class ProductEventPublisher implements ApplicationListener<ProductEvent>, Consumer<FluxSink<ProductEvent>> {

    private final Executor executor;
    private final BlockingQueue<ProductEvent> queue;

    ProductEventPublisher() {
        this.executor = Executors.newSingleThreadExecutor();
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onApplicationEvent(ProductEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<ProductEvent> sink) {
        this.executor.execute(() -> {
            while (true) {
                try {
                    ProductEvent event = queue.take();
                    sink.next(event);
                } catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
            }
        });
    }
}