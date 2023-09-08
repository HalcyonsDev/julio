package com.halcyon.julio.controller;

import com.halcyon.julio.dto.category.NewCategoryDto;
import com.halcyon.julio.model.Category;
import com.halcyon.julio.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody @Valid NewCategoryDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Category createdCategory = categoryService.create(dto);
        return ResponseEntity.ok(createdCategory);
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Category>> findAllByChannelId(@PathVariable Long channelId) {
        List<Category> categories = categoryService.findAllByChannelId(channelId);
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteById(@PathVariable Long categoryId) {
        String response = categoryService.deleteById(categoryId);
        return ResponseEntity.ok(response);
    }
}