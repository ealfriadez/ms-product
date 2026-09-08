package pe.edu.ufv.msproduct.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.ufv.msproduct.model.entity.CategoryEntity;
import pe.edu.ufv.msproduct.model.entity.DeletedProduct;
import pe.edu.ufv.msproduct.model.entity.ProductEntity;
import pe.edu.ufv.msproduct.model.entity.ProductStatus;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    @Query("from ProductEntity where deleted = pe.edu.ufv.msproduct.model.entity.DeletedProduct.CREATED and ((:status is null) or (status = :status))")
    List<ProductEntity> findAll(@Param("status")ProductStatus status);

    List<ProductEntity> findByCategoryAndDeleted(CategoryEntity category, DeletedProduct deleted);
}
