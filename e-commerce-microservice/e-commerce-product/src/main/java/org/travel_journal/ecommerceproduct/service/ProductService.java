package org.travel_journal.ecommerceproduct.service;

import org.springframework.stereotype.Service;
import org.travel_journal.ecommerceproduct.dto.ProductDTO;
import org.travel_journal.ecommerceproduct.dto.ProductUpdateDTO;
import org.travel_journal.ecommerceproduct.entity.Product;
import org.travel_journal.ecommerceproduct.entity.ProductStatus;
import org.travel_journal.ecommerceproduct.repository.ProductRepository;
import org.travel_journal.ecommerceproduct.utility.ProductMapper;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProductDTO createProduct(ProductDTO dto) {
        Product entity = mapper.toEntity(dto);
        entity.setStatus(ProductStatus.DRAFT); // 預設狀態
        Product saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public ProductDTO updateProduct(Long id, ProductUpdateDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        mapper.updateProductFromDto(dto, product);  // 套用部分更新

        Product updated = repository.save(product);
        return mapper.toDTO(updated);
    }

    public ProductDTO getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        product.setViewCount(product.getViewCount() + 1);
        repository.save(product);

        return mapper.toDTO(product);
    }

    public ProductDTO publishProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        product.setStatus(ProductStatus.AVAILABLE);
        product.setReleaseDate(LocalDate.now());

        Product updated = repository.save(product);
        return mapper.toDTO(updated);
    }

    public ProductDTO unpublishProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (product.getStatus() != ProductStatus.AVAILABLE) {
            throw new IllegalStateException("商品目前不是銷售中，無法執行下架操作");
        }

        product.setStatus(ProductStatus.UNAVAILABLE);
        Product updated = repository.save(product);

        return mapper.toDTO(updated);
    }

    public void deleteProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        repository.delete(product);
    }

    public ProductDTO archiveProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        product.setStatus(ProductStatus.ARCHIVED);
        Product updated = repository.save(product);

        return mapper.toDTO(updated);
    }
}
