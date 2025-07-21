package com.example.service;

import com.example.repository.AssetRepository;
import com.example.repository.CustomerRepository;
import com.example.response.AssetResponse;
import com.example.util.AssetMapper;
import com.example.util.AuthUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {

    final AssetRepository assetRepository;
    final CustomerRepository customerRepository;
    private final AssetMapper assetMapper;
    private final AuthUtil authUtil;

    public List<AssetResponse> listAssets(Long customerId) {

        if (!authUtil.isAdminOrAccessingOwnData(customerId)) {
            throw new AccessDeniedException("You can only view your own assets");
        }

        customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));

        return assetRepository.getAssetsByCustomerId(customerId)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new EntityNotFoundException("No assets found for customerId: " + customerId))
                .stream()
                .map(assetMapper::fromAsset)
                .collect(Collectors.toList());
    }
}

//    public List<AssetResponse> listAssets(Long customerId) {
//
//        if (!authUtil.isAdminOrAccessingOwnData(customerId)) {
//            throw new AccessDeniedException("You can only view your own assets");
//        }
//
//        return assetRepository.getAssetsByCustomerId(customerId)
//                .map(list -> {
//                    if (list.isEmpty()) {
//                        throw new EntityNotFoundException("No assets found for customerId: " + customerId);
//                    }
//                    return list.stream()
//                            .map(assetMapper::fromAsset)
//                            .collect(Collectors.toList());
//                })
//                .orElseThrow(() -> new EntityNotFoundException("No assets found for customerId: " + customerId));
//    }


