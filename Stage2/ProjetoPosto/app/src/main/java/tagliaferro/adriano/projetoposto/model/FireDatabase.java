package tagliaferro.adriano.projetoposto.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tagliaferro.adriano.projetoposto.controller.Posto;

/**
 * Created by Adriano2 on 14/10/2017.
 */

public class FireDatabase {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public void sendData(Posto posto) {
        try {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference().child("postos");
            mDatabaseReference.push().setValue(posto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
