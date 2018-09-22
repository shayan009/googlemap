package com.recharge.demomap.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;

public interface IFirebase {

   public DatabaseReference getFirebaseRef ();
   public CollectionReference getCollectionRef ();
}
