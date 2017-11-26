package com.icekredit.rpc.thrift.examples.http.entities;

import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftDepositCard;
import com.icekredit.rpc.thrift.examples.thrift.entities.ThriftWithdraw;
import com.icekredit.rpc.thrift.examples.thrift.enums.ThriftWithdrawStatus;

import java.io.Serializable;

public class WithdrawHistory implements Serializable {
    /**
     * 取款流水号
     */
    private String serialNumber;

    /**
     * 取款金额
     */
    private Double transactionAmount;

    /**
     * 取款提交时间
     */
    private String submittedTime;

    /**
     * 取款完成时间
     */
    private String finishedTime;

    /**
     * 取款状态(1：已完成，2：进行中，3：失败)
     */
    private Integer status;

    /**
     * 储蓄卡编号
     */
    private String depositCardId;

    public String getSerialNumber() {
        return serialNumber;
    }

    public WithdrawHistory withSerialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public WithdrawHistory withTransactionAmount(Double transactionAmount) {
        this.setTransactionAmount(transactionAmount);
        return this;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getSubmittedTime() {
        return submittedTime;
    }

    public WithdrawHistory withSubmittedTime(String submittedTime) {
        this.setSubmittedTime(submittedTime);
        return this;
    }

    public void setSubmittedTime(String submittedTime) {
        this.submittedTime = submittedTime == null ? null : submittedTime.trim();
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public WithdrawHistory withFinishedTime(String finishedTime) {
        this.setFinishedTime(finishedTime);
        return this;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime == null ? null : finishedTime.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public WithdrawHistory withStatus(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDepositCardId() {
        return depositCardId;
    }

    public WithdrawHistory withDepositCardId(String depositCardId) {
        this.setDepositCardId(depositCardId);
        return this;
    }

    public void setDepositCardId(String depositCardId) {
        this.depositCardId = depositCardId == null ? null : depositCardId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", serialNumber=").append(serialNumber);
        sb.append(", transactionAmount=").append(transactionAmount);
        sb.append(", submittedTime=").append(submittedTime);
        sb.append(", finishedTime=").append(finishedTime);
        sb.append(", status=").append(status);
        sb.append(", depositCardId=").append(depositCardId);
        sb.append("]");
        return sb.toString();
    }

    public ThriftWithdraw toThrift() {
        ThriftWithdraw thriftWithdraw = new ThriftWithdraw();
        thriftWithdraw.setSerialNumber(this.getSerialNumber());
        thriftWithdraw.setTransactionAmount(this.getTransactionAmount());
        thriftWithdraw.setSubmittedTime(this.getSubmittedTime());
        thriftWithdraw.setFinishedTime(this.getFinishedTime());
        thriftWithdraw.setStatus(this.getStatus() == 1 ? ThriftWithdrawStatus.FINISHED : ThriftWithdrawStatus.FAILED);

        ThriftDepositCard thriftDepositCard = new ThriftDepositCard();
        thriftWithdraw.setDepositCard(thriftDepositCard);

        return thriftWithdraw;
    }

    public static WithdrawHistory fromThrift(ThriftWithdraw thriftWithdraw) {
        WithdrawHistory withdrawHistory = new WithdrawHistory();
        withdrawHistory.setSerialNumber(thriftWithdraw.getSerialNumber());

        ThriftWithdrawStatus withdrawStatus = thriftWithdraw.getStatus();
        if (ThriftWithdrawStatus.FINISHED.equals(withdrawStatus)) {
            withdrawHistory.setStatus(1);
        } else {
            withdrawHistory.setStatus(0);
        }

        withdrawHistory.setTransactionAmount(thriftWithdraw.getTransactionAmount());
        withdrawHistory.setSubmittedTime(thriftWithdraw.getSubmittedTime());
        withdrawHistory.setFinishedTime(thriftWithdraw.getFinishedTime());

        ThriftDepositCard depositCard = thriftWithdraw.getDepositCard();
        if (depositCard != null) {
            withdrawHistory.setDepositCardId(depositCard.getId());
        }

        return withdrawHistory;
    }

}