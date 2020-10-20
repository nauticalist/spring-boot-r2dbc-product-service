package io.seanapse.app.products.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
    /**
     * Utility method to convert object to json string
     * @param o
     * @return json
     */
    public static String convertObjectToJsonString(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            // Ignore
        }
        return null;
    }
}
