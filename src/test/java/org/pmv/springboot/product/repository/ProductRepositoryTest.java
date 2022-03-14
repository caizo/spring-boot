package org.pmv.springboot.product.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.pmv.springboot.category.Category;
import org.pmv.springboot.product.Product;
import org.pmv.springboot.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void find_all_products(){
        List<Product> result = productRepository.findAll();
        assertThat(result.size()).isEqualTo(3);
    }


    @Test
    void findProduct_by_description() {
        // given
        LocalDate localDate = LocalDate.of(2000, 12, 12);
        Date createdAt = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Product product = Product.builder()
                .id(333L)
                .name("PRODUCT 1")
                .description("xxx")
                .stock(10.0)
                .createdAt(createdAt)
                .category(Category.builder().id(1L).build())
                .build();
        // when
        productRepository.save(product);
        // then
        Product product1 = productRepository.findByDescription("xxx");
        assertThat(product1.getDescription()).isEqualTo("xxx");

    }

    @Test
    void findProduct_by_categoryId() {
        // given
        LocalDate localDate = LocalDate.of(2000, 12, 12);
        Date createdAt = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Product product = Product.builder()
                .id(333L)
                .name("PRODUCT 1")
                .description("xxx")
                .stock(10.0)
                .createdAt(createdAt)
                .category(Category.builder().id(1L).build())
                .build();
        // when
        productRepository.save(product);
        // then
        List<Product> products = productRepository.findByCategory(Category.builder().id(1L).build());
        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(3);


    }
}