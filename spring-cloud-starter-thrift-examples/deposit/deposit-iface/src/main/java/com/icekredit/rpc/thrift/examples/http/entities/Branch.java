package com.icekredit.rpc.thrift.examples.http.entities;

import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftBank;
import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftBranch;
import com.icekredit.rpc.thrift.examples.thrift.enums.ThriftRegion;

import java.io.Serializable;

public class Branch implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 银行分支编码
     */
    private String code;

    /**
     * 银行分支名称
     */
    private String name;

    /**
     * 分支银行地址
     */
    private String address;

    /**
     * 员工数量
     */
    private Integer staffs;

    /**
     * 所属银行ID
     */
    private Long bankId;

    /**
     * 分支银行所属区域(NORTH：1，CENTRAL：2，SOUTH：3，EAST：4，SOUTHWEST：5，NORTHWEST：6，NORTHEAST：7)
     */
    private Integer regionId;

    public Long getId() {
        return id;
    }

    public Branch withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Branch withCode(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public Branch withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public Branch withAddress(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getStaffs() {
        return staffs;
    }

    public Branch withStaffs(Integer staffs) {
        this.setStaffs(staffs);
        return this;
    }

    public void setStaffs(Integer staffs) {
        this.staffs = staffs;
    }

    public Long getBankId() {
        return bankId;
    }

    public Branch withBankId(Long bankId) {
        this.setBankId(bankId);
        return this;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public Branch withRegionId(Integer regionId) {
        this.setRegionId(regionId);
        return this;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
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
        sb.append(", address=").append(address);
        sb.append(", staffs=").append(staffs);
        sb.append(", bankId=").append(bankId);
        sb.append("]");
        return sb.toString();
    }

    public ThriftBranch toThrift() {
        ThriftBranch thriftBranch = new ThriftBranch();

        thriftBranch.setId(this.getId());
        thriftBranch.setCode(this.getCode());
        thriftBranch.setName(this.getName());
        thriftBranch.setAddress(this.getAddress());
        thriftBranch.setStaffs(this.getStaffs());

        ThriftBank thriftBank = new ThriftBank();
        thriftBank.setId(this.getBankId());
        thriftBranch.setBank(thriftBank);

        thriftBranch.setRegion(ThriftRegion.findByValue(this.getRegionId()));
        return thriftBranch;
    }

    public static Branch fromThrift(ThriftBranch thriftBranch) {
        Branch branch = new Branch();

        branch.setId(thriftBranch.getId());
        branch.setCode(thriftBranch.getCode());
        branch.setName(thriftBranch.getName());
        branch.setStaffs(thriftBranch.getStaffs());
        branch.setAddress(thriftBranch.getAddress());

        ThriftBank thriftBank = thriftBranch.getBank();
        if (thriftBank != null) {
            branch.setBankId(thriftBank.getId());
        }

        return branch;
    }


}