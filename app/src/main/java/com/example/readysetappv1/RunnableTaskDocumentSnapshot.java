package com.example.readysetappv1;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.ExecutionException;

public class RunnableTaskDocumentSnapshot implements Runnable {

    private static final String TAG = "RTDocumentSnapshot";//RunnableTaskDocumentSnapshot

    private volatile DocumentSnapshot value;
    private Task<DocumentSnapshot> task;

    public RunnableTaskDocumentSnapshot(Task<DocumentSnapshot> task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        try {
            value = Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            value = null;
            Log.w(TAG, "Failed to process document snapshot", e);
        }
    }

    public DocumentSnapshot getValue() {
        return value;
    }
}

