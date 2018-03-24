/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dude.orm.sample.entity;

import java.util.Date;
import java.util.UUID;
import ru.dude.orm.model.annotations.Column;
import ru.dude.orm.model.annotations.Entity;
import ru.dude.orm.model.annotations.Id;
import ru.dude.orm.model.annotations.Inheritance;
import ru.dude.orm.model.annotations.InheritanceType;
import ru.dude.orm.model.annotations.Table;

/**
 * Базовая таблица
 * 
 * @author dude
 */
@Entity
@Table(name = BaseAudit.TABLE)
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseAudit {
    
    public static final String TABLE = "base_audit";
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "entity_type")
    protected String entityType;
    
    @Column(name = "create_date")
    private Date createDate;
    
    @Column(name = "edit_date")
    private Date editDate;

    @Column(name = "guid")
    protected UUID guid;
    
    @Column(name = "user_id")
    private Integer userId;

    public BaseAudit() {
    }

    public void fillNew(String entityType){
        this.entityType = entityType;
        createDate = new Date();
        editDate = new Date();
        guid = UUID.randomUUID();
        userId = 10;
    }
    
    public void fillEdit(){
        editDate = new Date();
        userId = 10;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
}
