package com.recharge.demomap.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Firebase implements IFirebase{
DatabaseReference databaseReference;
    CollectionReference collectionReference;
    public Firebase() {

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        databaseReference= FirebaseDatabase.getInstance().getReference("demo_map");
        FirebaseFirestore firebaseFirestore=  FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(settings);
        collectionReference =firebaseFirestore.collection("demo_map");
    }

    @Override
    public DatabaseReference getFirebaseRef() {
       return databaseReference;
    }

    @Override
    public CollectionReference getCollectionRef() {
        return collectionReference;
    }
}
