package org.pmv.springboot.product;

import lombok.RequiredArgsConstructor;
import org.pmv.springboot.category.Category;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{


    private final ProductRepository productRepository;

    @Override
    public List<Product> listAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        if(null == product.getCreatedAt()){
            product.setCreatedAt(new Date());
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product tmp_product = getProduct(product.getId());
        if(null == tmp_product){
            throw new IllegalArgumentException("Unknown product");
        }
        tmp_product.setName(product.getName());
        tmp_product.setDescription(product.getDescription());
        tmp_product.setCategory(product.getCategory());
        tmp_product.setPrice(product.getPrice());
        return productRepository.save(tmp_product);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product tmp_product = getProduct(id);
        if(null == tmp_product){
            throw new IllegalArgumentException("Unknown product");
        }
        tmp_product.setStatus("ELIMINADO");
        return productRepository.save(tmp_product);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product tmp_product = getProduct(id);
        if(null == tmp_product){
            throw new IllegalArgumentException("Unknown product");
        }
        double stock = tmp_product.getStock() + quantity;
        tmp_product.setStock(stock);
        return productRepository.save(tmp_product);
    }
}
