
package com.aplikace.Pujcovna_Zimniho_Vybaveni.model;

import jakarta.persistence.*;

@Entity
public class FyzyckyStavVyb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_F_stavu")
    private Long idFStavu;

    private String popis;

    public Long getIdFStavu() {
        return idFStavu;
    }

    public void setIdFStavu(Long idFStavu) {
        this.idFStavu = idFStavu;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }
}
