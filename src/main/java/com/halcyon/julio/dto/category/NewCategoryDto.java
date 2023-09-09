package com.halcyon.julio.dto.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    private Long channelId;

    @Size(min = 3, max = 20, message = "Category title must be greater than 3 characters and less than 20 characters.")
    private String title;
}