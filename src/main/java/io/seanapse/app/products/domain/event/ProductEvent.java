package io.seanapse.app.products.domain.event;

import io.seanapse.app.products.domain.entity.Product;
import org.springframework.context.ApplicationEvent;

public class ProductEvent extends ApplicationEvent {
    public static final String PRODUCT_CREATED = "CREATED";
    public static final String PRODUCT_UPDATED = "UPDATED";

    private String eventType;

    public ProductEvent(String eventType, Product product) {
        super(product);
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
