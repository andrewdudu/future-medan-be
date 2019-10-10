package com.future.medan.backend.services.impl;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.repositories.ProductRepository;
import com.future.medan.backend.services.ProductService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductImplTest {

    private ProductRepository productRepository;

    private ProductService productService;

    @Before
    public void setup(){
        this.productService = new ProductImpl(this.productRepository);
    }

    @Test
    public void testGetAll() {
//        Integer count = 16;
//        List<String> result = Arrays.asList(
//                "FizzBuzz", "1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8",
//                "Fizz", "Buzz", "11", "Fizz", "13", "14", "FizzBuzz"
//        );
//
//        List<Product> actual = this.productService.getAll();
//        assertEquals(count.intValue(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertNotNull(result.get(i));
//            assertNotNull(actual.get(i));
//            assertEquals(result.get(i), actual.get(i));
//        }
    }

    @Test
    public void testGetById(){
        String findId = "Apa nama id nya";
        Product result = new Product();
        result.setName("Anak bebek");
        Product actual = this.productService.getById(findId);

        assertEquals(actual.getName(), result.getName());
    }

    @Test
    public void testSave(){

    }

    @Test
    public void testEditById(){

    }

    @Test
    public void testDeleteById(){

    }
}
