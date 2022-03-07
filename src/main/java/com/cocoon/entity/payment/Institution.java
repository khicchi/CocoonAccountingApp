package com.cocoon.entity.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name="institutions")
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted=false")
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "id"},ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Institution {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean isDeleted=false;

    public Institution(String name) {
        this.name = name;
    }
}
