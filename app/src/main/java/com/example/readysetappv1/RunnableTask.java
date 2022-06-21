package com.example.readysetappv1;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.ExecutionException;

public class RunnableTask implements Runnable {
    private volatile QuerySnapshot value;
    private Task<QuerySnapshot> task;

    public RunnableTask(Task<QuerySnapshot> task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        try {
            value = Tasks.await(task);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public QuerySnapshot getValue() {
        return value;
    }
}
