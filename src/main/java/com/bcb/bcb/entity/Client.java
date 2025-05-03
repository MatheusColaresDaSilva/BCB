package com.bcb.bcb.entity;

import com.bcb.bcb.dto.request.ClientRequestDTO;
import com.bcb.bcb.enums.DocumentEnum;
import com.bcb.bcb.enums.PlanEnum;
import com.bcb.bcb.exception.InsufficientBalanceException;
import com.bcb.bcb.exception.InvalidDocumentException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Entity
@Table(name = "CLIENT")
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {

    private String name;

    @Column(name = "document_id", length = 20, nullable = false, unique = true)
    private String documentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentEnum documentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanEnum planType;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(precision = 15, scale = 2)
    private BigDecimal limitBalance;

    private Boolean active;

    public static Client fromDTO(ClientRequestDTO dto) {
        DocumentEnum documentType = DocumentEnum.fromCode(dto.getDocumentType());

        if (!documentType.isValid(dto.getDocumentId())) {
            throw new InvalidDocumentException(dto.getDocumentType());
        }

        return Client.builder()
                .name(dto.getName())
                .documentId(dto.getDocumentId())
                .documentType(DocumentEnum.valueOf(dto.getDocumentType()))
                .planType(PlanEnum.valueOf(dto.getPlanType()))
                .balance(dto.getBalance())
                .limitBalance(dto.getLimit())
                .active(dto.getActive())
                .build();
    }

    public BigDecimal getAvailableBalance() {
        if(isPrePaid()) return this.getBalance();
        return this.getLimitBalance();
    }

    public Boolean isPrePaid() {
        return PlanEnum.PREPAID.equals(this.getPlanType());
    }

    public Boolean canAfford(BigDecimal cost) {
        return getAvailableBalance().compareTo(cost) >= 0;
    }

    public void debitAmount(BigDecimal amount) {
        if (canAfford(amount)) {
            BigDecimal newBalance = getAvailableBalance().subtract(amount);
            if (isPrePaid()) {
                setBalance(newBalance);
            } else {
                setLimitBalance(newBalance);
            }
        } else {
            throw new InsufficientBalanceException();
        }
    }
}
