package com.debasish.demomap.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;

public interface IFirebaseManager {

   public DatabaseReference getFirebaseRef ();
   public CollectionReference getCollectionRef ();
}
