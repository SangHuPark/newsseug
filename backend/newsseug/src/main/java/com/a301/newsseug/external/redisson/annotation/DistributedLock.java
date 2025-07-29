package com.a301.newsseug.external.redisson.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    String key();
    long waitTime();
    long leaseTime();

}

