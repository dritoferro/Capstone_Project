package tagliaferro.adriano.projetoposto.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tagliaferro.adriano.projetoposto.R;
import tagliaferro.adriano.projetoposto.controller.Posto;

/**
 * Created by Adriano2 on 14/10/2017.
 */

public class FireDatabase {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private boolean isToInsert = false;

    public void sendData(final Posto posto) {
        try {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference().child("postos");
            //Aqui é feita uma query ao Firebase para verificar se já existe um posto com este nome cadastrado.
            Query query = mDatabaseReference.orderByChild("posto_nome").equalTo(posto.getPosto_nome());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        //Entra aqui se existe algum registro com este nome de posto
                        if (dataSnapshot.exists()) {
                            List<Posto> postoList = new ArrayList<>();
                            Gson gson = new Gson();
                            //Faz uma lista dos postos encontrados com o mesmo nome fazendo uma conversão usando GSON.
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                try {
                                    //Foi necessário passar para JSON primeiro pois o padrão do Firebase é diferente dos JSONs comuns.
                                    //Depois fiz a desconversão de JSON para o formato Posto.class
                                    String tempJson = gson.toJson(snap.getValue());
                                    Posto tempPosto = gson.fromJson(tempJson, Posto.class);
                                    postoList.add(tempPosto);
                                } catch (Exception e) {
                                    throw new RuntimeException(e.getMessage());
                                }

                            }
                            //O que interessa é apenas o último registro de posto no Firebase Database.
                            //Aqui verifica se existe um combustível cadastrado e verifica o preço do litro para ver qual é o menor.
                            //Se detectado que o posto sendo informado agora é menor do que o último registro do Firebase então, salve-o.
                            Posto objPosto = postoList.get(postoList.size() - 1);
                            if (!objPosto.getPosto_comb1().equals(R.string.select) && !objPosto.getPosto_comb1().equals("")) {
                                if (!objPosto.getPosto_comb1().isEmpty() && !posto.getPosto_valor_comb1().isEmpty()) {
                                    if (Double.parseDouble(objPosto.getPosto_valor_comb1()) > Double.parseDouble(posto.getPosto_valor_comb1())) {
                                        isToInsert = true;
                                    }
                                }
                            }
                            //O que interessa é apenas o último registro de posto no Firebase Database.
                            //Aqui verifica se existe um combustível cadastrado e verifica o preço do litro para ver qual é o menor.
                            //Se detectado que o posto sendo informado agora é menor do que o último registro do Firebase então, salve-o.
                            if (!objPosto.getPosto_comb2().equals(R.string.select) && !objPosto.getPosto_comb2().equals("")) {
                                if (!objPosto.getPosto_comb2().isEmpty() && !posto.getPosto_valor_comb2().isEmpty()) {
                                    if (Double.parseDouble(objPosto.getPosto_valor_comb2()) > Double.parseDouble(posto.getPosto_valor_comb2())) {
                                        isToInsert = true;
                                    }
                                }
                            }
                            //Aqui é feita a inserção com base na decisão tomada anteriormente.
                            if (isToInsert) {
                                mDatabaseReference.push().setValue(posto);
                            }
                        } else {
                            //Senão já adiciona o mesmo no Database do Firebase. Não existe posto com este nome cadastrado ainda.
                            mDatabaseReference.push().setValue(posto);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
