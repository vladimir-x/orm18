/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dude.orm.sample.entity;

import ru.dude.orm.model.annotations.Column;
import ru.dude.orm.model.annotations.Entity;
import ru.dude.orm.model.annotations.Id;
import ru.dude.orm.model.annotations.Table;

/**
 *
 * @author dude
 */
@Entity
@Table(name = Car.TABLE)
public class Car extends BaseAudit{
    
    public static final String TABLE = "car";
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "brand")
    private String brand;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "color") 
    private String color;

    @Column(name = "body") 
    BodyType body;
    
    public Car() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BodyType getBody() {
        return body;
    }

    public void setBody(BodyType body) {
        this.body = body;
    }

}
