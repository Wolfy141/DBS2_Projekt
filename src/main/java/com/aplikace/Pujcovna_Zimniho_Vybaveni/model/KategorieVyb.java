
package com.aplikace.Pujcovna_Zimniho_Vybaveni.model;

import jakarta.persistence.*;

@Entity
public class KategorieVyb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_kategorie")
    private Long idKategorie;

    private String nazev;

    public Long getIdKategorie() {
        return idKategorie;
    }

    public void setIdKategorie(Long idKategorie) {
        this.idKategorie = idKategorie;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }
}
