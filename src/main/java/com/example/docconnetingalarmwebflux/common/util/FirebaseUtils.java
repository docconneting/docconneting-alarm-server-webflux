package com.example.docconnetingalarmwebflux.common.util;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.CompletableFuture;

public class FirebaseUtils {
    public static <T> CompletableFuture<T> toCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        ApiFutures.addCallback(apiFuture, new ApiFutureCallback<T>() {
            @Override
            public void onFailure(Throwable t) {
                completableFuture.completeExceptionally(t);
            }

            @Override
            public void onSuccess(T result) {
                completableFuture.complete(result);
            }
        }, MoreExecutors.directExecutor());
        return completableFuture;
    }
}
