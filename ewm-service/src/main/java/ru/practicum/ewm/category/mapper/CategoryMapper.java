package ru.practicum.ewm.category.mapper;

import ru.practicum.ewm.category.dto.CategoryDtoResponse;
import ru.practicum.ewm.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static List<CategoryDtoResponse> mapToCategoryDtoResponse(List<Category> categories) {
        return categories.stream().map(CategoryMapper::mapToCategoryDtoResponse).collect(Collectors.toList());
    }

    public static CategoryDtoResponse mapToCategoryDtoResponse(Category category) {
        return CategoryDtoResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
