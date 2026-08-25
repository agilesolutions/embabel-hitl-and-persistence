package com.agilesolutions.embabel.persistence;

public class ProcessOptimisticLockException
        extends RuntimeException {

    public ProcessOptimisticLockException(
            String message) {

        super(message);
    }
}