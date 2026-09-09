package pe.edu.ufv.msproduct.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.ufv.msproduct.model.dto.ProductCreateRequestDto;
import pe.edu.ufv.msproduct.model.dto.ProductResponseDto;
import pe.edu.ufv.msproduct.model.dto.ProductUpdateRequestDto;
import pe.edu.ufv.msproduct.model.dto.ProductUpdateStockRequestDto;
import pe.edu.ufv.msproduct.model.entity.ProductStatus;
import pe.edu.ufv.msproduct.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ServletWebServerApplicationContext webServerAppCtxt;

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(@RequestParam(required = false) ProductStatus productStatus) {

        var port = Objects.requireNonNull(webServerAppCtxt.getWebServer()).getPort();
        var result = productService.findAll(productStatus, port);
        if(result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) throws InterruptedException {
        var port = Objects.requireNonNull(webServerAppCtxt.getWebServer()).getPort();
        if(id.equals(4L) || id.equals(5L) || id.equals(6L)) {
            throw new RuntimeException("Internal server error");
        }
        var result = productService.findById(id, port);
        return ResponseEntity.ok(result);
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductCreateRequestDto productRequest) {
        var port = Objects.requireNonNull(webServerAppCtxt.getWebServer()).getPort();
        var result = productService.create(productRequest, port);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping(value="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequestDto productRequest) {
        var port = Objects.requireNonNull(webServerAppCtxt.getWebServer()).getPort();
        var result = productService.update(id, productRequest, port);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/{id}/stock", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<ProductResponseDto> updateStock(@PathVariable Long id, @Valid @RequestBody ProductUpdateStockRequestDto productRequest) {
        var port = Objects.requireNonNull(webServerAppCtxt.getWebServer()).getPort();
        var result = productService.updateStock(id, productRequest, port);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?>  deleteProduct(@PathVariable Long id) {
        var port = Objects.requireNonNull(webServerAppCtxt.getWebServer()).getPort();
        productService.delete(id, port);
        return ResponseEntity.noContent().build();
    }
}
