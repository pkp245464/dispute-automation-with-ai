package com.automation.dispute.model;

import com.automation.dispute.enums.PriorityLevelEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dispute")
public class Dispute {
    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "dispute_type")
    private String disputeType;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private PriorityLevelEnum priority = PriorityLevelEnum.LOW;

    @Column(name = "is_resolved")
    private Boolean isResolved = false;
}
