package com.giraone.queuing.worker.service.tasks.payload;

import java.util.Map;

public class FibonacciPayload implements Payload {

    public Map<String, Object> run(Map<String, Object> parameter) {

        int input = (Integer) parameter.get("input");
        int output = fib(input);
        return Map.of("output", output);
    }

    public static int fib(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }
}
