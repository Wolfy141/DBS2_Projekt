package com.aplikace.Pujcovna_Zimniho_Vybaveni.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Vybaveni")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Vybaveni")
    private Long idVybaveni;

    @Column(nullable = false)
    private String nazev;

    private String popis;

    @Column(nullable = false)
    private BigDecimal cena;

    @Column(nullable = false)
    private BigDecimal zaloha;

    @ManyToOne
    @JoinColumn(name = "Fyzycky_stav_vyb_ID_F_stavu")
    private FyzyckyStavVyb stav;

    @ManyToOne
    @JoinColumn(name = "Kategorie_vyb_ID_kategorie")
    private KategorieVyb kategorie;

    // --- Gettery a settery ---

    public Long getIdVybaveni() {
        return idVybaveni;
    }

    public void setIdVybaveni(Long idVybaveni) {
        this.idVybaveni = idVybaveni;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public BigDecimal getZaloha() {
        return zaloha;
    }

    public void setZaloha(BigDecimal zaloha) {
        this.zaloha = zaloha;
    }

    public FyzyckyStavVyb getStav() {
        return stav;
    }

    public void setStav(FyzyckyStavVyb stav) {
        this.stav = stav;
    }

    public KategorieVyb getKategorie() {
        return kategorie;
    }

    public void setKategorie(KategorieVyb kategorie) {
        this.kategorie = kategorie;
    }
}
