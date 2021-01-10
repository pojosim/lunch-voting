package com.example.lunchvoting.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String... details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String[] getDetails() {
        return details;
    }
}