package com.aplikace.Pujcovna_Zimniho_Vybaveni.service;

import com.aplikace.Pujcovna_Zimniho_Vybaveni.model.Item;
import com.aplikace.Pujcovna_Zimniho_Vybaveni.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
