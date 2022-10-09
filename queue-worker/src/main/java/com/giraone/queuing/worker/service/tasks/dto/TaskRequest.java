package com.giraone.queuing.worker.service.tasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TaskRequest implements Serializable {

    /**
     * Task to be performed. May not be null.
     */
    String type;
    /**
     * Optional parameters for the task.
     */
    Map<String, Object> parameter;
    /**
     * Optional request ID of client. Returned in response.
     */
    @JsonProperty("client_request_id")
    String clientRequestId;
}
