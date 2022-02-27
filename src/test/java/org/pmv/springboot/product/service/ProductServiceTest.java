package org.pmv.springboot.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pmv.springboot.category.Category;
import org.pmv.springboot.product.Product;
import org.pmv.springboot.product.ProductRepository;
import org.pmv.springboot.product.ProductService;
import org.pmv.springboot.product.ProductServiceImpl;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }


    @Test
    void list_all_products_Test() {
        // when
        productService.listAllProduct();
        // then
        verify(productRepository).findAll();

    }

    @Test
    void createProduct_with_createdAty_date_Test() {
        // GIVEN: Dados los siguientes datos
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
        // WHEN: Cuando se ejecute la siguiente acción
        productService.createProduct(product);

        // THEN: Entonces se hacen las comprobaciones.
        // Compara que el producto que se pasa como argumento al servicio es el mismo que termina recibiendo el
        // repository
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedProduct).isEqualTo(product);
    }
    @Test
    void createProduct_without_createdAty_date_Test() {
        // GIVEN: Dados los siguientes datos

        Product product = Product.builder()
                .id(333L)
                .name("PRODUCT 1")
                .description("xxx")
                .stock(10.0)
                .category(Category.builder().id(1L).build())
                .build();
        // WHEN: Cuando se ejecute la siguiente acción
        productService.createProduct(product);

        // THEN: Entonces se hacen las comprobaciones.
        // Compara que el producto que se pasa como argumento al servicio es el mismo que termina recibiendo el
        // repository
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedProduct).isEqualTo(product);
    }
    @Test
    void update_product_Test() {
        // GIVEN
        Product product = Product.builder()
                .id(3L)
                .name("Spring Boot in Action")
                .description("El mejor framework del mundo")
                .category(Category.builder().id(2L).build())
                .price(66.0)
                .build();
        // WHEN
        when(productRepository.findById(3L)).thenReturn(Optional.of(product));
        productService.updateProduct(product);
        // THEN
        verify(productRepository).findById(product.getId());


    }

    @Test
    void update_unknown_product_Test(){
        // GIVEN
        Product product = Product.builder()
                .id(-1L)
                .name("Spring Boot in Action")
                .description("El mejor framework del mundo")
                .category(Category.builder().id(2L).build())
                .price(66.0)
                .build();
        // WHEN
        // THEN
        assertThatThrownBy(() -> productService.updateProduct(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown product");
    }


    @Test
    void delete_product_Test() {
        // GIVEN
        Product product = Product.builder()
                .id(1L)
                .name("Spring Boot in Action")
                .description("El mejor framework del mundo")
                .category(Category.builder().id(2L).build())
                .price(66.0)
                .build();
        // WHEN
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProduct(1L);
        // THEN
        verify(productRepository).findById(product.getId());

    }


    @Test
    void delete_unknown_product_Test(){
        assertThatThrownBy(() -> productService.deleteProduct(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown product");
    }

    @Test
    void find_by_category_Test() {
        // GIVEN
        Category category = Category.builder().id(2L).name("books").build();
        // WHEN
        productService.findByCategory(category);
        // THEN
        verify(productRepository).findByCategory(category);
    }

    @Test
    void update_stock_product_Test() {
        // GIVEN
        double stock = 99.0;
        Product product = Product.builder()
                .id(1L)
                .name("Spring Boot in Action")
                .description("El mejor framework del mundo")
                .category(Category.builder().id(2L).build())
                .price(66.0)
                .stock(1000.0)
                .build();
        // WHEN
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.updateStock(product.getId(),stock);
        verify(productRepository).save(product);
    }

    @Test
    void update_stock_for_unknown_product_Test(){
        // GIVEN
        Product product = Product.builder()
                .id(-1L)
                .name("Spring Boot in Action")
                .description("El mejor framework del mundo")
                .category(Category.builder().id(2L).build())
                .stock(1000.0)
                .price(66.0)
                .build();
        // WHEN & THEN
        //Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        assertThatThrownBy(() -> productService.updateStock(product.getId(), product.getStock()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown product");
    }

}