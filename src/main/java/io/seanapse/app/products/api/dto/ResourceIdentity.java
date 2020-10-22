package io.seanapse.app.products.api.dto;

public class ResourceIdentity {
    private Long id;

    public ResourceIdentity() {
    }

    public ResourceIdentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
