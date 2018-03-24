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
@Table(name = Driver.TABLE)
public class Driver extends BaseAudit{
    
    public static final String TABLE = "driver";
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "fullname")
    private String fullname;
    
    @Column(name = "age") 
    private Integer age;

    @Column(name = "car_id") 
    private Integer carId;
    
    public Driver() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }


}
