package com.example.project.entity.store;

import com.example.project.entity.BaseEntity;
import com.example.project.entity.constant.ProductCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "products")
@Entity
public class Product extends BaseEntity {

    @SequenceGenerator(name = "product_seq_gen", sequenceName = "product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
    @Id
    private Long id;

    private String name;

    private String itemDetails;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private Long price;

    private String image;

}
