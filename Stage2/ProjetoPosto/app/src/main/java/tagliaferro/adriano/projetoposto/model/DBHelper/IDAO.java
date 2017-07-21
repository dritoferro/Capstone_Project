package tagliaferro.adriano.projetoposto.model.DBHelper;

import java.util.List;

/**
 * Created by Adriano2 on 20/07/2017.
 */

public interface IDAO<T> {

    void inserir(T obj);

    void alterar(T obj);

    List<T> listar();

    List<T> listar(T obj);

    T buscar(T obj);

    void excluir(T obj);

}
