package org.citic.iiot.persist.domain.user;

import org.citic.iiot.persist.domain.PageEntity;

import java.io.Serializable;

public class User extends PageEntity implements Serializable {

    private static final long serialVersionUID = 5651952458731440278L;
    private int id;
    private String userid;
    private String username;
    private String password;
    private int deptid;
    private int roleid;
    private java.sql.Timestamp createTime;
    private String account;
    private String name;
    private int roleId;
        
    public int getId() {
        return this.id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }
        
    public String getUserid() {
        return this.userid;
    }

    public User setUserid(String userid) {
        this.userid = userid;
        return this;
    }
        
    public String getUsername() {
        return this.username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }
        
    public String getPassword() {
        return this.password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
        
    public int getDeptid() {
        return this.deptid;
    }

    public User setDeptid(int deptid) {
        this.deptid = deptid;
        return this;
    }
        
    public int getRoleid() {
        return this.roleid;
    }

    public User setRoleid(int roleid) {
        this.roleid = roleid;
        return this;
    }
        
    public java.sql.Timestamp getCreateTime() {
        return this.createTime;
    }

    public User setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
        return this;
    }
        
    public String getAccount() {
        return this.account;
    }

    public User setAccount(String account) {
        this.account = account;
        return this;
    }
        
    public String getName() {
        return this.name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }
        
    public int getRoleId() {
        return this.roleId;
    }

    public User setRoleId(int roleId) {
        this.roleId = roleId;
        return this;
    }

}
