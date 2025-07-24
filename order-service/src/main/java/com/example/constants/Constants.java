package com.example.constants;

public class Constants {

    public static final String APPLICATION_JSON = "application/json";
    public static final String JWT_AUTH = "jwtAuth";
    public static final String AUTHORIZATION = "Authorization";
    public static final int JWT_BEGIN_INDEX = 7;
    public static final String BEAERER_AUTH = "BearerAuth";
    public static final String BAERER = "Bearer ";
    public static final String REFRESH = "REFRESH_";

    public static final String AUTH_SUMMARY = "Login and retrieve JWT token";
    public static final String AUTH_DESC = "Authenticates the user and returns a JWT token if successful.";

    public static final String OK_CODE = "200";
    public static final String AUTH_OK_DESC = "Successfully authenticated";

    public static final String UNAUTHORIZED_CODE = "401";
    public static final String AUTH_UNAUTHORIZED_DESC = "Unauthorized - Invalid username or password";
    public static final String AUTH_UNAUTHORIZED_EXAMPLE = """
                {
                  "error": "Unauthorized",
                  "message": "Invalid credentials",
                  "status": 401,
                  "timestamp": "2025-07-12T14:30:00.000"
                }
            """;

    public static final String INTERNAL_SERVER_ERROR_CODE = "500";
    public static final String AUTH_INTERNAL_ERROR_DESC = "Internal Server Error - Authentication processing failed";
    public static final String AUT_INTERNAL_ERROR_EXAMPLE = """
                {
                  "error": "Internal Server Error",
                  "message": "Unexpected authentication failure",
                  "status": 500,
                  "timestamp": "2025-07-12T14:30:00.000"
                }
            """;

    public static final String ASSETS_SUMMARY = "List assets for a customer";
    public static final String ASSETS_DESC = "Lists all assets (including TRY) held by a customer.";
    public static final String ASSETS_OK_DESC = "Assets listed successfully";
    public static final String ASSETS_OK_EXAMPLE = """
                [
                    {
                        "id": 1,
                        "customerId": 100,
                        "symbol": "TRY",
                        "size": 10000.00,
                        "usableSize": 9500.00
                    },
                    {
                        "id": 2,
                        "customerId": 100,
                        "symbol": "BTC",
                        "size": 0.5,
                        "usableSize": 0.4
                    }
                ]
            """;

    public static final String ACCESS_DENIED_CODE = "403";
    public static final String ASSETS_ACCESS_DENIED_DESC = "Access denied - not authorized to view these assets";
    public static final String ASSETS_ACCESS_DENIED_EXAMPLE = """
                {
                    "timestamp": "2025-07-23T14:15:22",
                    "status": 403,
                    "error": "Forbidden",
                    "message": "You can only view your own assets",
                    "path": "/api/v1/assets/customerId/100"
                }
            """;

    public static final String NOT_FOUND_CODE = "404";
    public static final String ASSETS_NOT_FOUND_DESC = "Customer not found or no assets found";
    public static final String ASSETS_NOT_FOUND_EXAMPLE = """
                {
                    "timestamp": "2025-07-23T14:15:22",
                    "status": 404,
                    "error": "Not Found",
                    "message": "Customer not found with id: 100",
                    "path": "/api/v1/assets/customerId/100"
                }
            """;

    public static final String ASSETS_INTERNAL_ERROR_DESC = "Internal server error - List assets failed";
    public static final String ASSETS_INTERNAL_ERROR_EXAMPLE = """
                {
                    "timestamp": "2025-07-23T14:15:22",
                    "status": 500,
                    "error": "Internal Server Error",
                    "message": "Unexpected error occurred",
                    "path": "/api/v1/assets/customerId/100"
                }
            """;

    public static final String GET_ORDERS_SUMMARY = "Search orders with optional filters";
    public static final String GET_ORDERS_DESC = "Retrieves a list of orders using optional filters like customerId, assetId, date range, order side, status, price and size ranges. Admin users can access any customer's orders, while non-admin users will only receive their own orders regardless of the provided customerId.";
    public static final String GET_ORDERS_OK_DESC = "Orders retrieved successfully";

    public static final String CUSTOMER_ID_DESC = "Customer ID to filter by (admins only)";
    public static final String START_DATE_DESC = "Start of create date range (ISO format)";
    public static final String END_DATE_DESC = "End of create date range (ISO format)";
    public static final String STATUS_DESC = "Order status (e.g., PENDING, COMPLETED)";
    public static final String SIDE_DESC = "Order side (BUY or SELL)";
    public static final String ASSET_ID_DESC = "Asset ID to filter by";
    public static final String MIN_PRICE_DESC = "Minimum order price";
    public static final String MAX_PRICE_DESC = "Maximum order price";
    public static final String MIN_SIZE_DESC = "Minimum order size";
    public static final String MAX_SIZE_DESC = "Maximum order size";

    public static final String CUSTOMER_ID_NAME = "customerId";
    public static final String CUSTOMER_ID_EXAMPLE = "123";
    public static final String START_DATE_NAME = "startDate";
    public static final String START_DATE_EXAMPLE = "2024-01-01T00:00:00";
    public static final String END_DATE_NAME = "endDate";
    public static final String END_DATE_EXAMPLE = "2024-01-31T23:59:59";
    public static final String STATUS_NAME = "status";
    public static final String STATUS_EXAMPLE = "PENDING";
    public static final String SIDE_NAME = "side";
    public static final String SIDE_EXAMPLE = "BUY";
    public static final String ASSET_ID_NAME = "assetId";
    public static final String ASSET_ID_EXAMPLE = "789";
    public static final String MIN_PRICE_NAME = "minPrice";
    public static final String MIN_PRICE_EXAMPLE = "100.00";
    public static final String MAX_PRICE_NAME = "maxPrice";
    public static final String MAX_PRICE_EXAMPLE = "200.00";
    public static final String MIN_SIZE_NAME = "minSize";
    public static final String MIN_SIZE_EXAMPLE = "10.0";
    public static final String MAX_SIZE_NAME = "maxSize";
    public static final String MAX_SIZE_EXAMPLE = "100.0";

    public static final String GET_ORDERS_OK_EXAMPLE = """
                [
                  {
                    "id": 1,
                    "customerId": 100,
                    "symbol": "BTC",
                    "orderType": "BUY",
                    "price": 950000.00,
                    "quantity": 0.1,
                    "status": "PENDING",
                    "timestamp": "2025-07-23T15:30:00"
                  }
                ]
            """;

    public static final String GET_ORDERS_ACCESS_DENIED_DESC = "Access denied - not authorized to view these orders";
    public static final String GET_ORDERS_ACCESS_DENIED_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:40:00",
                  "status": 403,
                  "error": "Forbidden",
                  "message": "You are not authorized to access these orders",
                  "path": "/api/v1/orders"
                }
            """;

    public static final String GET_ORDERS_INTERNAL_ERROR_DESC = "Internal server error - Get orders failed";
    public static final String GET_ORDERS_INTERNAL_ERROR_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:40:00",
                  "status": 500,
                  "error": "Internal Server Error",
                  "message": "Unexpected error occurred",
                  "path": "/api/v1/orders"
                }
            """;

    public static final String CREATE_ORDER_SUMMARY = "Create a new stock order";
    public static final String CREATE_ORDER_DESC = "Creates a PENDING order. BUY requires sufficient TRY balance; SELL requires asset availability.";
    public static final String CREATED_CODE = "201";
    public static final String ORDER_CREATED_DESC = "Order created successfully";
    public static final String ORDER_CREATED_EXAMPLE = """
                {
                  "id": 2,
                  "customerId": 100,
                  "symbol": "ETH",
                  "orderType": "SELL",
                  "price": 25000.00,
                  "quantity": 1.0,
                  "status": "PENDING",
                  "timestamp": "2025-07-23T15:35:00"
                }
            """;

    public static final String BAD_REQUEST_CODE = "400";
    public static final String CREATE_ORDER_BAD_REQUEST_DESC = "Validation failed or business rule violated";
    public static final String CREATE_ORDER_BAD_REQUEST_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:41:00",
                  "status": 400,
                  "error": "Bad Request",
                  "message": "Insufficient TRY balance for order",
                  "path": "/api/v1/orders"
                }
            """;

    public static final String ORDER_CREATE_ACCESS_DENIED_DESC = "Access denied - you cannot create an order for a different customer";
    public static final String ORDER_CREATE_ACCESS_DENIED_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:41:00",
                  "status": 403,
                  "error": "Forbidden",
                  "message": "You cannot create an order for another customer",
                  "path": "/api/v1/orders"
                }
            """;
    public static final String CREATE_ORDER_INTERNAL_ERROR_DESC = "Internal Server Error - create order failed due to unexpected error";
    public static final String CREATE_ORDER_INTERNAL_ERROR_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:41:00",
                  "status": 500,
                  "error": "Internal Server Error",
                  "message": "Unexpected error occurred",
                  "path": "/api/v1/orders"
                }
            """;
    public static final String DELETE_ORDER_SUMMARY = "Cancel a pending order";
    public static final String DELETE_ORDER_DESC = "Only PENDING orders can be cancelled. Order status becomes CANCELED.";
    public static final String DELETE_ORDER_OK_DESC = "Order cancelled successfully";

    public static final String ORDER_NOT_FOUND_DESC = "Order not found";
    public static final String ORDER_NOT_FOUND_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:42:00",
                  "status": 404,
                  "error": "Not Found",
                  "message": "Order with ID 42 not found",
                  "path": "/api/v1/orders/42"
                }
            """;

    public static final String DELETE_ORDER_ACCESS_DENIED_DESC = "Access denied - not authorized to cancel these assets";
    public static final String DELETE_ORDER_ACCESS_DENIED_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:42:00",
                  "status": 403,
                  "error": "Forbidden",
                  "message": "You are not allowed to cancel this order",
                  "path": "/api/v1/orders/42"
                }
            """;

    public static final String DELETE_ORDER_INTERNAL_ERROR_DESC = "Internal server error - cancel order failed";
    public static final String DELETE_ORDER_INTERNAL_ERROR_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:42:00",
                  "status": 500,
                  "error": "Internal Server Error",
                  "message": "Unexpected error occurred",
                  "path": "/api/v1/orders/42"
                }
            """;
    public static final String MATCH_ORDER_SUMMARY = "Match a pending order";
    public static final String MATCH_ORDER_DESC = "Only ADMIN users can match pending orders manually.";

    public static final String ORDER_MATCHED_DESC = "Only ADMIN users can match pending orders manually.";

    public static final String MATCH_ORDER_ACCESS_DENIED_DESC = "Only admin users are allowed to match orders";
    public static final String MATCH_ORDER_ACCESS_DENIED_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:43:00",
                  "status": 403,
                  "error": "Forbidden",
                  "message": "Admin privileges required",
                  "path": "/api/v1/orders/99/match"
                }
            """;
    public static final String MATCH_ORDER_NOT_FOUND_DESC = "Order not found or already matched";
    public static final String MATCH_ORDER_NOT_FOUND_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:43:00",
                  "status": 404,
                  "error": "Not Found",
                  "message": "Pending order with ID 99 not found",
                  "path": "/api/v1/orders/99/match"
                }
            """;
    public static final String MATCH_ORDER_INTERNAL_ERROR_DESC = "Internal server error - match order failed";
    public static final String MATCH_ORDER_INTERNAL_ERROR_EXAMPLE = """
                {
                  "timestamp": "2025-07-23T15:43:00",
                  "status": 500,
                  "error": "Internal Server Error",
                  "message": "Unexpected error occurred",
                  "path": "/api/v1/orders/99/match"
                }
            """;

    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String ASSET_NOT_FOUND = "Asset not found";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String TRY_ASSET_NOT_FOUND = "TRY asset not found";
    public static final String TRY = "TRY";
    public static final String TRADED_ASSET_NOT_FOUND = "Traded asset not found";
}
