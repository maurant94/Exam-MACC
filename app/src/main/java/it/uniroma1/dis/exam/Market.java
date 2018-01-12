package it.uniroma1.dis.exam;

/**
 * Created by antoniomauro on 12/01/18.
 */

public class Market {

    private String ccomune;
    private String cprovincia;
    private String cregione;
    private String cnome;
    private String canno_inserimento;
    private String cdata_e_ora_inserimento;
    private Long cidentificatore_in_openstreetmap;
    private Double clongitudine;
    private Double clatitudine;

    public String getCcomune() {
        return ccomune;
    }

    public void setCcomune(String ccomune) {
        this.ccomune = ccomune;
    }

    public String getCprovincia() {
        return cprovincia;
    }

    public void setCprovincia(String cprovincia) {
        this.cprovincia = cprovincia;
    }

    public String getCnome() {
        return cnome;
    }

    public void setCnome(String cnome) {
        this.cnome = cnome;
    }

    public String getCanno_inserimento() {
        return canno_inserimento;
    }

    public void setCanno_inserimento(String canno_inserimento) {
        this.canno_inserimento = canno_inserimento;
    }

    public String getCdata_e_ora_inserimento() {
        return cdata_e_ora_inserimento;
    }

    public void setCdata_e_ora_inserimento(String cdata_e_ora_inserimento) {
        this.cdata_e_ora_inserimento = cdata_e_ora_inserimento;
    }

    public Long getCidentificatore_in_openstreetmap() {
        return cidentificatore_in_openstreetmap;
    }

    public void setCidentificatore_in_openstreetmap(Long cidentificatore_in_openstreetmap) {
        this.cidentificatore_in_openstreetmap = cidentificatore_in_openstreetmap;
    }

    public Double getClongitudine() {
        return clongitudine;
    }

    public void setClongitudine(Double clongitudine) {
        this.clongitudine = clongitudine;
    }

    public Double getClatitudine() {
        return clatitudine;
    }

    public void setClatitudine(Double clatitudine) {
        this.clatitudine = clatitudine;
    }

    public String getCregione() {
        return cregione;
    }

    public void setCregione(String cregione) {
        this.cregione = cregione;
    }

    @Override
    public String toString(){
        String s = "";
        s = s + this.getCnome() + "\n"
                + this.getCcomune() + " (" + this.getCprovincia() + " - " + this.getCregione() + ")";
        return s;
    }
}
