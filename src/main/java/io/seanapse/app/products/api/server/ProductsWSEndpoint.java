package io.seanapse.app.products.api.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.seanapse.app.products.broker.publisher.ProductEventPublisher;
import io.seanapse.app.products.domain.entity.Product;
import io.seanapse.app.products.domain.event.ProductEvent;
import io.seanapse.app.products.infrastructure.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@WebSocket
public class ProductsWSEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ProductsWSEndpoint.class);


    @Bean
    HandlerMapping handlerMapping(WebSocketHandler socketHandler) {
        return new SimpleUrlHandlerMapping() {{
            setUrlMap(Collections.singletonMap("/ws/events", socketHandler));
            setOrder(10);
        }};
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandler(ProductEventPublisher eventPublisher, ObjectMapper objectMapper) {
        Flux<ProductEvent> publish = Flux.create(eventPublisher).share();
        return session -> {
            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
                try {

                    Product product = (Product) evt.getSource();
                    Map<String, Product> data = new HashMap<>();
                    data.put(evt.getEventType(), product);

                    return objectMapper.writeValueAsString(data);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).map(str -> {
                LOG.debug("Publishing message to Websocket :: " + str);
                return session.textMessage(str);
            });

            return session.send(messageFlux);
        };
    }
}
