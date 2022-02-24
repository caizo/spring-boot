package org.pmv.springboot.product;

import org.pmv.springboot.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    public List<Product> findByCategory(Category category);

    Product findByDescription(String description);
}
