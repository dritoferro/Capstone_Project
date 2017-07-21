package tagliaferro.adriano.projetoposto.controller;

/**
 * Created by Adriano2 on 20/07/2017.
 */

public class Abastecimento {

    private int abastecimento_id;
    private int abastecimento_veiculo_id;
    private int abastecimento_posto_id;
    private String abastecimento_comb;
    private String abastecimento_valor_litro;
    private String abastecimento_data;
    private String abastecimento_km_atual;


    public int getAbastecimento_id() {
        return abastecimento_id;
    }

    public void setAbastecimento_id(int abastecimento_id) {
        this.abastecimento_id = abastecimento_id;
    }

    public int getAbastecimento_veiculo_id() {
        return abastecimento_veiculo_id;
    }

    public void setAbastecimento_veiculo_id(int abastecimento_veiculo_id) {
        this.abastecimento_veiculo_id = abastecimento_veiculo_id;
    }

    public int getAbastecimento_posto_id() {
        return abastecimento_posto_id;
    }

    public void setAbastecimento_posto_id(int abastecimento_posto_id) {
        this.abastecimento_posto_id = abastecimento_posto_id;
    }

    public String getAbastecimento_comb() {
        return abastecimento_comb;
    }

    public void setAbastecimento_comb(String abastecimento_comb) {
        this.abastecimento_comb = abastecimento_comb;
    }

    public String getAbastecimento_valor_litro() {
        return abastecimento_valor_litro;
    }

    public void setAbastecimento_valor_litro(String abastecimento_valor_litro) {
        this.abastecimento_valor_litro = abastecimento_valor_litro;
    }

    public String getAbastecimento_data() {
        return abastecimento_data;
    }

    public void setAbastecimento_data(String abastecimento_data) {
        this.abastecimento_data = abastecimento_data;
    }

    public String getAbastecimento_km_atual() {
        return abastecimento_km_atual;
    }

    public void setAbastecimento_km_atual(String abastecimento_km_atual) {
        this.abastecimento_km_atual = abastecimento_km_atual;
    }
}
