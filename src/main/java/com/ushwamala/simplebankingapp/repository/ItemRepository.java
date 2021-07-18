package com.ushwamala.simplebankingapp.repository;

import com.ushwamala.simplebankingapp.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
