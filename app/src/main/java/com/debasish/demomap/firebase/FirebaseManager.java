package com.debasish.demomap.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FirebaseManager implements IFirebaseManager {
DatabaseReference databaseReference;
    CollectionReference collectionReference;
    public FirebaseManager () {

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        databaseReference=FirebaseDatabase.getInstance().getReference("hotedin_chat");
        FirebaseFirestore firebaseFirestore=  FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(settings);
        collectionReference =firebaseFirestore.collection("hotedin_chat");
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
