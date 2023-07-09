package org.dieschnittstelle.mobile.android.skeleton.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncOperationRunner {
    private ProgressBar progressBar;
    private Activity owner;

    public AsyncOperationRunner(Activity owner, ProgressBar progressBar){
        this.owner = owner;
        this.progressBar = progressBar;
    }

    public <T> void run(Supplier<T> asyncOperation, Consumer<T> operationResultConsumer){
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }

        new Thread(()->{
            T result = asyncOperation.get();
            Log.i(owner.getClass().getSimpleName(), " got result from async operation: " + result);
            owner.runOnUiThread(()-> {
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
                if(operationResultConsumer != null) {
                    operationResultConsumer.accept(result);
                }
            });
        }).start();
    }
}
