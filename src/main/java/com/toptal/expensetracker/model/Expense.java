package com.toptal.expensetracker.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Sergey Royz
 * Date: 27.12.2014
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense extends Model {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Formula("user_id")
    private String userId;

    public Expense(User user, String description, String comment, BigDecimal amount, Date timestamp) {
        this.user = user;
        this.description = description;
        this.comment = comment;
        this.amount = amount;
        this.timestamp = timestamp;
        userId = user.getId();
    }

}
