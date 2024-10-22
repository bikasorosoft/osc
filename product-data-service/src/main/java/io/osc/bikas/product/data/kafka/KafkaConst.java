package io.osc.bikas.product.data.kafka;

public class KafkaConst {

    public static final String SCHEMA_REGISTRY = "http://localhost:18081";
    public static final String BOOTSTRAP_SERVER = "localhost:19092";

    public static final String APPLICATION_NAME = "product-data-service";

    public static final String CATEGORY_DATA_TOPIC = "bikas-category-data-store";
    public static final String CATEGORY_DATA_STORE = "bikas-category-data-store";

    public static final String PRODUCT_DATA_TOPIC = "bikas-product-data-topic";
    public static final String PRODUCT_DATA_STORE = "bikas-product-data-store";

    public static final String PRODUCT_CLICK_TOPIC = "bikas-product-click";
    public static final String PRODUCT_CLICK_STORE = "bikas-click-store";
    public static final String POPULAR_PRODUCT_STORE = "bikas-popular-product-store";
}
