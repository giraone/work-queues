package com.giraone.queuing.entry.service.tasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Pickup ticket. Response for a task request.
 */
@Data
@NoArgsConstructor // for Jackson
@AllArgsConstructor
@ToString
public class TaskTicket implements Serializable {

    /**
     * Unique ID of task. Needed to download the task result.
     */
    @JsonProperty("task_reference_id")
    String taskReferenceId;
}
