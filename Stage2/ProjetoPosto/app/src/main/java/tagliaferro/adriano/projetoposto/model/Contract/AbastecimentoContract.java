package tagliaferro.adriano.projetoposto.model.Contract;

/**
 * Created by Adriano2 on 20/07/2017.
 */

public final class AbastecimentoContract {

    public static final String tbAbastecimento = "abastecimento";

    public static final class Columns{
        public static final String abastecimento_id = "id";
        public static final String abastecimento_id_veiculo = "veiculo_id";
        public static final String abastecimento_id_posto = "posto_id";
        public static final String abastecimento_comb = "comb";
        public static final String abastecimento_valor_litro = "valor_litro";
        public static final String abastecimento_data = "data";
        public static final String abastecimento_km_atual = "km_atual";
    }
}
