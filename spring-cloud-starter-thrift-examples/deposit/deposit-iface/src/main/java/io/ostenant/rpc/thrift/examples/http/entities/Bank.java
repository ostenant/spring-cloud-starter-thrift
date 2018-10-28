package io.ostenant.rpc.thrift.examples.http.entities;


import io.ostenant.rpc.thrift.examples.thrift.entities.ThriftBank;

import java.io.Serializable;
import java.util.Collections;

public class Bank implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 银行编码
     */
    private String code;

    /**
     * 银行名称
     */
    private String name;

    /**
     * 相关描述信息
     */
    private String description;

    public Long getId() {
        return id;
    }

    public Bank withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Bank withCode(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public Bank withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public Bank withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append("]");
        return sb.toString();
    }


    public ThriftBank toThrift() {
        ThriftBank thriftBank = new ThriftBank();

        thriftBank.setId(this.getId());
        thriftBank.setCode(this.getCode());
        thriftBank.setName(this.getName());
        thriftBank.setDescription(this.getDescription());
        thriftBank.setBranches(Collections.emptyMap());

        return thriftBank;
    }

    public static Bank fromThrift(ThriftBank thriftBank) {
        Bank bank = new Bank();

        bank.setId(thriftBank.getId());
        bank.setCode(thriftBank.getCode());
        bank.setName(thriftBank.getName());
        bank.setDescription(thriftBank.getDescription());

        return bank;
    }

}