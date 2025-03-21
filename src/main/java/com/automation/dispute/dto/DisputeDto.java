package com.automation.dispute.dto;

import com.automation.dispute.enums.PriorityLevelEnum;
import com.automation.dispute.model.Dispute;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisputeDto {
    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("dispute_type")
    private String disputeType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("priority")
    private PriorityLevelEnum priority = PriorityLevelEnum.LOW;

    @JsonProperty("is_resolved")
    private Boolean isResolved = false;
}
