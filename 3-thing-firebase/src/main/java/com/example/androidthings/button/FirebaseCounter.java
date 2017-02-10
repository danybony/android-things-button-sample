package com.example.androidthings.button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class FirebaseCounter {

    private static final String COUNTER_NODE = "counter";

    private final DatabaseReference databaseReference;
    private Listener listener;
    private final ValueEventListener notifyListenerOValueChanged = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            counter = (Long) dataSnapshot.getValue();
            listener.onCounterUpdated(counter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // do nothing
        }
    };
    private long counter = 0;

    static FirebaseCounter newInstance() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(COUNTER_NODE);
        return new FirebaseCounter(databaseReference);
    }

    private FirebaseCounter(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    void decreaseCounter() {
        counter--;
        syncWithFirebase();
    }

    void increaseCounter() {
        counter++;
        syncWithFirebase();
    }

    private void syncWithFirebase() {
        databaseReference.setValue(counter);
    }

    void setListener(Listener listener) {
        this.listener = listener;
        this.databaseReference.addValueEventListener(notifyListenerOValueChanged);
    }

    void removeListener() {
        this.databaseReference.removeEventListener(notifyListenerOValueChanged);
        this.listener = null;
    }

    interface Listener {

        void onCounterUpdated(long value);

    }
}
