package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private StorageService storageService;

    private SequenceService sequenceService;

    @Autowired
    public ProductServiceImpl(ProductRepository repository,
                              StorageService storageService,
                              SequenceService sequenceService) {
        this.sequenceService = sequenceService;
        this.storageService = storageService;
        this.productRepository = repository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllWithoutHidden() {
        return productRepository.getAllByHiddenIs(false);
    }

    @Override
    public List<Product> getByCategoryIdWithoutHidden(String categoryId) {
        List<Product> products = productRepository.getByCategoryIdAndHiddenIs(categoryId, false);

        if (products == null) throw new ResourceNotFoundException("Product", "Category", categoryId);

        return products;
    }

    @Override
    public List<Product> getByMerchantId(String merchantId) {
        return productRepository.getByMerchantId(merchantId);
    }

    @Override
    public Page<Product> findPaginated(int page, int size) {
        Pageable paging = PageRequest.of(page, size);

        return productRepository.findAll(paging);
    }

    @Override
    public Product getById(String id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    @Transactional
    public Set<Product> findByIdIn(Set<String> id) {
        return productRepository.findByIdIn(id);
    }

    @Override
    public Product hide(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setHidden(!product.getHidden());

        return productRepository.save(product);
    }

    @Override
    public Product save(Product product) throws IOException {
        String sku = sequenceService.save(product.getName().replaceAll("\\s+","").substring(0, 3).toUpperCase());
        product.setSku(sku);

        String variant = sequenceService.save(sku);
        product.setVariant(variant);

        String pdfPath = storageService.storePdf(product.getPdf(), product.getSku());
        String imagePath = storageService.storeImage(product.getImage(), product.getSku());

        product.setPdf(pdfPath);
        product.setImage(imagePath);

        return productRepository.save(product);
    }

    @Override
    public Product save(Product productRequest, String id) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        String pdfPath = storageService.storePdf(productRequest.getPdf(), product.getSku());


        String imageName = product.getImage().split("/")[3].split("\\.")[0];

        Pattern pattern = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
        Matcher m = pattern.matcher(imageName);

        if (m.matches()) {
            String prefix = m.group(1);
            String last = m.group(2);
            String suffix = m.group(3);
            if (suffix == null) suffix = "";

            int count = last != null ? Integer.parseInt(last) : 0;

            count++;
            imageName = prefix + "(" + count + ")" + suffix;
        }

        String imagePath = storageService.storeImage(productRequest.getImage(), imageName);

        productRequest.setPdf(pdfPath);
        productRequest.setImage(imagePath);
        productRequest.setHidden(product.getHidden());

        return productRepository.save(productRequest);
    }

    @Override
    public void deleteById(String id){
        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException("Product", "id", id);
        else
            productRepository.deleteById(id);
    }
}
