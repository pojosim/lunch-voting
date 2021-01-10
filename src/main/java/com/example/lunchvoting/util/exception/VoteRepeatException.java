package com.example.lunchvoting.util.exception;

import org.springframework.lang.NonNull;

public class VoteRepeatException extends RuntimeException {
    public VoteRepeatException(@NonNull String msg) {
        super(msg);
    }
}