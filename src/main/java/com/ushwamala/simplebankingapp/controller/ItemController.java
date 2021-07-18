package com.ushwamala.simplebankingapp.controller;

import java.util.List;

import javax.validation.Valid;

import com.ushwamala.simplebankingapp.model.Item;
import com.ushwamala.simplebankingapp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<Item> getItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/{id}")
    public Item getItemById(@PathVariable int id) {
        return itemService.findItemById(id)
                .orElse(null);
    }

    @DeleteMapping("/items/{id}")
    public HttpStatus deleteItem(@PathVariable int id) {
        Item item = getItemById(id);
        if (item == null) {
            return HttpStatus.NOT_FOUND;
        }
        itemService.deleteItemById(id);
        return HttpStatus.OK;
    }

    @PostMapping("/items")
    public HttpStatus createItem(@Valid @RequestBody Item item) {
        itemService.saveItem(item);
        return HttpStatus.CREATED;
    }
}
