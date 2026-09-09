package pe.edu.ufv.msproduct.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.ufv.msproduct.configuration.error.ResourceNotFoundException;
import pe.edu.ufv.msproduct.model.dto.ProductCreateRequestDto;
import pe.edu.ufv.msproduct.model.dto.ProductResponseDto;
import pe.edu.ufv.msproduct.model.dto.ProductUpdateRequestDto;
import pe.edu.ufv.msproduct.model.entity.CategoryEntity;
import pe.edu.ufv.msproduct.model.entity.DeletedProduct;
import pe.edu.ufv.msproduct.model.entity.ProductEntity;
import pe.edu.ufv.msproduct.model.entity.ProductStatus;
import pe.edu.ufv.msproduct.model.mapper.ProductMapper;
import pe.edu.ufv.msproduct.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAll(ProductStatus status, int port) {

        log.info("findAll");
        var list = repository.findAll(status);
        log.info("found");

        return list.stream().map(p -> mapper.entityToResponse(p, port)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id, int port) {

        log.info("findById");

        return repository.findById(id)
                .filter(p -> p.getDeleted() == DeletedProduct.CREATED)
                .map(p -> mapper.entityToResponse(p, port)).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByIdCategory(Long id, int port) {

        log.info("findByIdCategory");
        var list = repository.findByCategoryAndDeleted(CategoryEntity.builder().id(id).build(), DeletedProduct.CREATED);
        log.info("found category");

        return list.stream().map(p -> mapper.entityToResponse(p, port)).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto create(ProductCreateRequestDto productRequest, int port) {

        log.info("create");
        ProductEntity productEntity = mapper.requestToEntity(productRequest);
        repository.save(productEntity);
        log.info("saved");

        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductUpdateRequestDto productRequest, int port) {

        log.info("update");
        ProductEntity productEntity = getProductById(id);
        BeanUtils.copyProperties(productRequest, productEntity);
        productEntity.setCategory(CategoryEntity.builder().id(productRequest.getCategoryId()).build());
        repository.save(productEntity);
        log.info("updated");

        return mapper.entityToResponse(productEntity, port);
    }

    @Transactional
    public ProductResponseDto updateStock(Long id, ProductUpdateRequestDto productRequest, int port) {

        log.info("updateStock");
        ProductEntity productEntity = getProductById(id);
        productEntity.setStock(productRequest.getStock() + productEntity.getStock());
        repository.save(productEntity);
        log.info("stock updated");

        return mapper.entityToResponse(productEntity, port);

    }

    @Transactional
    public void delete(Long id, int port) {

        log.info("delete");
        ProductEntity productEntity = getProductById(id);
        productEntity.setDeleted(DeletedProduct.DELETED);
        repository.save(productEntity);
        log.info("deleted");
    }

    private ProductEntity getProductById(Long id) {
        Optional<ProductEntity> productEntityOptional = repository.findById(id)
                .filter(p -> p.getDeleted() == DeletedProduct.CREATED);
        if (!productEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("Resource not found");
        }

        return productEntityOptional.get();
    }
}
