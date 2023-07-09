package com.doantotnghiep.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Account")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "Email")
    private String email;
    @Column(name = "FullName")
    private String fullName;
    @Column(name = "Password")
    private String password;
    @Column(name = "Status")
    private int status;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AccountRole", joinColumns = @JoinColumn(name = "Email"),
            inverseJoinColumns = @JoinColumn(name = "RoleId"))
    private List<Role> roles = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
