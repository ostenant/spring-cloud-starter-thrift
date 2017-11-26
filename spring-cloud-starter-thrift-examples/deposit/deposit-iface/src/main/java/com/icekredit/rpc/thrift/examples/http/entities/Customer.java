package com.icekredit.rpc.thrift.examples.http.entities;

import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftCustomer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Customer implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 生日日期
     */
    private String birthday;

    /**
     * (0: 男，1：女)
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     *
     */
    private String address;

    public String getId() {
        return id;
    }

    public Customer withId(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public Customer withName(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public Customer withBirthday(String birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public Customer withSex(Integer sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public Customer withAge(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public Customer withAddress(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", birthday=").append(birthday);
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", address=").append(address);
        sb.append("]");
        return sb.toString();
    }

    public ThriftCustomer toThrift() {
        ThriftCustomer thriftCustomer = new ThriftCustomer();

        thriftCustomer.setIDNumber(this.getId());
        thriftCustomer.setAge(this.getAge());
        thriftCustomer.setSex(this.getSex());
        thriftCustomer.setBirthday(this.getBirthday());
        thriftCustomer.setName(this.getName());

        String addressesStr = this.getAddress();
        if (addressesStr != null && !"".equals(addressesStr.trim())) {
            List<String> addressList = Arrays.asList(addressesStr.split(","));
            thriftCustomer.setAddress(addressList);
        }

        return thriftCustomer;
    }

    public static Customer fromThrift(ThriftCustomer thriftCustomer) {
        Customer customer = new Customer();

        customer.setId(thriftCustomer.getIDNumber());
        customer.setName(thriftCustomer.getName());
        customer.setBirthday(thriftCustomer.getBirthday());
        customer.setSex(thriftCustomer.getSex());
        customer.setAge(thriftCustomer.getAge());

        List<String> addressList = thriftCustomer.getAddress();
        if (addressList != null && addressList.size() > 0) {
            customer.setAddress(String.join(",", addressList));
        }

        return customer;
    }

}