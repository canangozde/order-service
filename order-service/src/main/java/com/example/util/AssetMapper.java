package com.example.util;

import com.example.model.Asset;
import com.example.model.Customer;
import com.example.response.AssetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(target = "customerId", source = "customer", qualifiedByName = "mapCustomerToId")
    AssetResponse fromAsset(Asset asset);

    @Named("mapCustomerToId")
    static Long mapCustomerToId(Customer customer) {
        return customer != null ? customer.getId() : null;
    }
}
