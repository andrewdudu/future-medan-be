package com.future.medan.backend.controllers;

import com.future.medan.backend.payload.responses.FileWebResponse;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.PurchaseService;
import com.future.medan.backend.services.StorageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api
@RestController
public class FileController {

    private StorageService storageService;

    private JwtTokenProvider jwtTokenProvider;

    private PurchaseService purchaseService;

    @Autowired
    public FileController(StorageService storageService, JwtTokenProvider jwtTokenProvider, PurchaseService purchaseService) {
        this.storageService = storageService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/api/get-image/{filePath}")
    public Response<FileWebResponse> imageToBase64(@PathVariable String filePath) throws IOException {
        return ResponseHelper.ok(WebResponseConstructor.toFileWebResponse(storageService.loadImage(filePath)));
    }

    @GetMapping("/api/get-pdf")
    public ResponseEntity<byte[]> pdfToBase64(@RequestParam("file-path") String filePath, @RequestParam("product-id") String productId, @RequestHeader("Authorization") String bearerToken) throws IOException {
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        if (isPurchased(productId, jwtTokenProvider.getUserIdFromJWT(token))) {
            byte[] contents = storageService.loadBook(filePath);
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.parseMediaType("application/pdf"));

            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
        }

        throw new AccessDeniedException("You need to purchase the book.");
    }

    private Boolean isPurchased(String productId, String userId) {
        return purchaseService.getByProductIdAndUserId(productId, userId) != null;
    }
}
