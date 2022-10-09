package com.giraone.queuing.worker.service.tasks.payload;

import java.util.Map;

public interface Payload {

    Map<String, Object> run(Map<String, Object> parameter);
}
