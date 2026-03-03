package com.eazybytes.eazystore.service.impl;

import com.eazybytes.eazystore.dto.PageResponseDto;
import com.eazybytes.eazystore.dto.ProductDto;
import com.eazybytes.eazystore.entity.Product;
import com.eazybytes.eazystore.repository.ProductRepository;
import com.eazybytes.eazystore.service.IProductService;
import com.fasterxml.jackson.databind.util.BeanUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private  final ProductRepository productRepository;


    @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Override
    public PageResponseDto getProducts(Pageable pageable) {
        Page<Product> page=productRepository.findAll(pageable);
       List<ProductDto>content=page.getContent().stream().map(this::transformToDto).collect(Collectors.toUnmodifiableList());

       return new PageResponseDto(content,page.getNumber(),page.getTotalPages(), page.getTotalElements(),page.getSize());
    }
    private ProductDto transformToDto(Product product){
        ProductDto productDto=new ProductDto();
        BeanUtils.copyProperties(product,productDto);
        return productDto;
    }
}
