package tagliaferro.adriano.projetoposto.controller;

/**
 * Created by Adriano2 on 20/07/2017.
 */

public class Posto {

    private int posto_id;
    private String posto_nome;
    private String posto_comb1;
    private String posto_comb2;
    private String posto_valor_comb1;
    private String posto_valor_comb2;
    private String posto_localizacao;


    public int getPosto_id() {
        return posto_id;
    }

    public void setPosto_id(int posto_id) {
        this.posto_id = posto_id;
    }

    public String getPosto_nome() {
        return posto_nome;
    }

    public void setPosto_nome(String posto_nome) {
        this.posto_nome = posto_nome;
    }

    public String getPosto_comb1() {
        return posto_comb1;
    }

    public void setPosto_comb1(String posto_comb1) {
        this.posto_comb1 = posto_comb1;
    }

    public String getPosto_comb2() {
        return posto_comb2;
    }

    public void setPosto_comb2(String posto_comb2) {
        this.posto_comb2 = posto_comb2;
    }

    public String getPosto_valor_comb1() {
        return posto_valor_comb1;
    }

    public void setPosto_valor_comb1(String posto_valor_comb1) {
        this.posto_valor_comb1 = posto_valor_comb1;
    }

    public String getPosto_valor_comb2() {
        return posto_valor_comb2;
    }

    public void setPosto_valor_comb2(String posto_valor_comb2) {
        this.posto_valor_comb2 = posto_valor_comb2;
    }

    public String getPosto_localizacao() {
        return posto_localizacao;
    }

    public void setPosto_localizacao(String posto_localizacao) {
        this.posto_localizacao = posto_localizacao;
    }
}
