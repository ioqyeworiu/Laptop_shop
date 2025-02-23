package com.lapstore.LaptopShop.service;

import java.util.List;

import com.lapstore.LaptopShop.model.Category;


public interface CategoryService {

    public Category saveCategory(Category category);

    public Boolean existCategory(String name);

    public List<com.lapstore.LaptopShop.model.Category> getAllCategory();
}
