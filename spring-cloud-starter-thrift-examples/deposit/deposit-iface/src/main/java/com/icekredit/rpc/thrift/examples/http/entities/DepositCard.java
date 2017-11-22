package com.icekredit.rpc.thrift.examples.http.entities;

import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftBranch;
import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftCustomer;
import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftDepositCard;

import java.io.Serializable;

public class DepositCard implements Serializable {
    /**
     * 储蓄卡编号
     */
    private String id;

    /**
     * 是否是vip用户
     */
    private Boolean isVip;

    /**
     * 开户时间
     */
    private String openingTime;

    /**
     * 账户余额
     */
    private Double accountBalance;

    /**
     * 账户总流水(入账金额+出账金额)
     */
    private Double accountFlow;

    /**
     * 开户银行分支ID
     */
    private Long branchId;

    /**
     * 客户ID
     */
    private String customerId;

    public String getId() {
        return id;
    }

    public DepositCard withId(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public DepositCard withIsVip(Boolean isVip) {
        this.setIsVip(isVip);
        return this;
    }

    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public DepositCard withOpeningTime(String openingTime) {
        this.setOpeningTime(openingTime);
        return this;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime == null ? null : openingTime.trim();
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public DepositCard withAccountBalance(Double accountBalance) {
        this.setAccountBalance(accountBalance);
        return this;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Double getAccountFlow() {
        return accountFlow;
    }

    public DepositCard withAccountFlow(Double accountFlow) {
        this.setAccountFlow(accountFlow);
        return this;
    }

    public void setAccountFlow(Double accountFlow) {
        this.accountFlow = accountFlow;
    }

    public Long getBranchId() {
        return branchId;
    }

    public DepositCard withBranchId(Long branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public DepositCard withCustomerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", isVip=").append(isVip);
        sb.append(", openingTime=").append(openingTime);
        sb.append(", accountBalance=").append(accountBalance);
        sb.append(", accountFlow=").append(accountFlow);
        sb.append(", branchId=").append(branchId);
        sb.append(", customerId=").append(customerId);
        sb.append("]");
        return sb.toString();
    }

    public ThriftDepositCard toThrift() {
        ThriftDepositCard thriftDepositCard = new ThriftDepositCard();

        thriftDepositCard.setId(this.getId());
        thriftDepositCard.setAccountBalance(this.getAccountBalance());
        thriftDepositCard.setAccountFlow(this.getAccountFlow());
        thriftDepositCard.setIsVip(this.getIsVip());
        thriftDepositCard.setOpeningTime(this.getOpeningTime());

        ThriftBranch thriftBranch = new ThriftBranch();
        thriftBranch.setId(this.getBranchId());
        thriftDepositCard.setBranch(thriftBranch);

        ThriftCustomer thriftCustomer = new ThriftCustomer();
        thriftCustomer.setIDNumber(this.getCustomerId());
        thriftDepositCard.setCustomer(thriftCustomer);

        return thriftDepositCard;
    }

    public static DepositCard fromThrift(ThriftDepositCard thriftDepositCard) {
        DepositCard depositCard = new DepositCard();

        depositCard.setId(thriftDepositCard.getId());
        depositCard.setAccountBalance(thriftDepositCard.getAccountBalance());
        depositCard.setAccountFlow(thriftDepositCard.getAccountFlow());
        depositCard.setIsVip(thriftDepositCard.isIsVip());

        ThriftCustomer thriftCustomer = thriftDepositCard.getCustomer();
        if (thriftCustomer != null) {
            String customerIDNumber = thriftCustomer.getIDNumber();
            depositCard.setCustomerId(customerIDNumber);
        }

        ThriftBranch thriftBranch = thriftDepositCard.getBranch();
        if (thriftBranch != null) {
            Long branchId = thriftBranch.getId();
            depositCard.setBranchId(branchId);
        }

        depositCard.setOpeningTime(thriftDepositCard.getOpeningTime());

        return depositCard;
    }

}