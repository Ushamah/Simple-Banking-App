package com.ushwamala.simplebankingapp.service;

import java.util.List;
import java.util.Optional;

import com.ushwamala.simplebankingapp.model.Item;
import com.ushwamala.simplebankingapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<Item> findItemById(int id) {
        return itemRepository.findById(id);
    }

    public List<Item> getAllItems() {
        List<Item> items = itemRepository.findAll();
        items.forEach(item -> item.setValue(item.getPrice() * item.getQuantity()));
        return items;
    }

    public void deleteItemById(int id) {
        itemRepository.deleteById(id);
    }

    public void saveItem(Item item) {
        itemRepository.save(item);
    }
}
