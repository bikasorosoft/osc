package io.osc.bikas.dashboard.dto;

public enum ResponseDataType {

    CART("Categories"), FEATURED_PRODUCT("Featured Products"), LAST_VIEWED_PRODUCT(""), SIMILAR_PRODUCT(""), CATEGORY("");

    private String type;

    private ResponseDataType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }

}
