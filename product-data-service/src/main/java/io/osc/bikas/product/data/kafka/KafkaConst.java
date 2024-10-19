package io.osc.bikas.product.data.kafka;

public class KafkaConst {

    public static final String SCHEMA_REGISTRY = "http://0.0.0.0:18081";

    public static final String PRODUCT_VIEW_TOPIC = "bikas-product-view-topic";
    public static final String PRODUCT_VIEW_STORE = "bikas-product-view-store";

    public static final String SORTED_CATEGORIES_TOPIC = "bikas-sorted-categories-topic";
    public static final String SORTED_CATEGORIES_STORE = "bikas-sorted-categories-store";
    public static final String SORTED_CATEGORIES_TOPIC_DEFAULT_KEY = "sorted-categories";

    public static final String SORTED_PRODUCT_DATA_TOPIC = "bikas-sorted-product-data-topic";
    public static final String SORTED_PRODUCT_DATA_STORE = "bikas-sorted-product-data-store";

    public static final String PRODUCT_DATA_TOPIC = "bikas-product-data-topic";
    public static final String PRODUCT_DATA_STORE = "bikas-product-data-store";

    public static final String PRODUCT_CLICK_TOPIC = "bikas-product-click";
    public static final String PRODUCT_CLICK_STORE = "bikas-click-store";
    public static final String POPULAR_PRODUCT_STORE = "bikas-popular-product-store";
}
