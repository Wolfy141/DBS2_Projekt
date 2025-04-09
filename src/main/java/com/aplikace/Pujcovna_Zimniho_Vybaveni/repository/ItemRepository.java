package com.aplikace.Pujcovna_Zimniho_Vybaveni.repository;

import com.aplikace.Pujcovna_Zimniho_Vybaveni.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
