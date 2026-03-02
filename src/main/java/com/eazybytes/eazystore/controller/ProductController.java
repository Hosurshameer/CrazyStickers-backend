package com.eazybytes.eazystore.controller;


import com.eazybytes.eazystore.dto.PageResponseDto;
import com.eazybytes.eazystore.dto.ProductDto;
import com.eazybytes.eazystore.entity.Product;
import com.eazybytes.eazystore.repository.ProductRepository;
import com.eazybytes.eazystore.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {


    private  final IProductService iProductService;

    @GetMapping
  public ResponseEntity<PageResponseDto> getProdcts( Pageable pageable)  {

//        return ResponseEntity.status(HttpStatus.OK).body(iProductService.getProducts());
        return ResponseEntity.ok().body(iProductService.getProducts(pageable));
  }
}
