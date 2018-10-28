package io.ostenant.rpc.thrift.examples.http.entities;

import io.ostenant.rpc.thrift.examples.thrift.entities.ThriftDeposit;
import io.ostenant.rpc.thrift.examples.thrift.entities.ThriftDepositCard;
import io.ostenant.rpc.thrift.examples.thrift.enums.ThriftDepositStatus;

import java.io.Serializable;

public class DepositHistory implements Serializable {
    /**
     * 存款流水号
     */
    private String serialNumber;

    /**
     * 存款金额
     */
    private Double transactionAmount;

    /**
     * 存款提交时间
     */
    private String submittedTime;

    /**
     * 存款完成时间
     */
    private String finishedTime;

    /**
     * 存款状态(1：已完成，2：进行中，3：失败)
     */
    private Integer status;

    /**
     * 储蓄卡编号
     */
    private String depositCardId;

    public String getSerialNumber() {
        return serialNumber;
    }

    public DepositHistory withSerialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public DepositHistory withTransactionAmount(Double transactionAmount) {
        this.setTransactionAmount(transactionAmount);
        return this;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getSubmittedTime() {
        return submittedTime;
    }

    public DepositHistory withSubmittedTime(String submittedTime) {
        this.setSubmittedTime(submittedTime);
        return this;
    }

    public void setSubmittedTime(String submittedTime) {
        this.submittedTime = submittedTime == null ? null : submittedTime.trim();
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public DepositHistory withFinishedTime(String finishedTime) {
        this.setFinishedTime(finishedTime);
        return this;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime == null ? null : finishedTime.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public DepositHistory withStatus(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDepositCardId() {
        return depositCardId;
    }

    public DepositHistory withDepositCardId(String depositCardId) {
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

    public ThriftDeposit toThrift() {
        ThriftDeposit thriftDeposit = new ThriftDeposit();
        thriftDeposit.setSerialNumber(this.getSerialNumber());
        thriftDeposit.setTransactionAmount(this.getTransactionAmount());
        thriftDeposit.setSubmittedTime(this.getSubmittedTime());
        thriftDeposit.setFinishedTime(this.getFinishedTime());
        thriftDeposit.setStatus(this.getStatus() == 1 ? ThriftDepositStatus.FINISHED : ThriftDepositStatus.FAILED);

        ThriftDepositCard thriftDepositCard = new ThriftDepositCard();
        thriftDeposit.setDepositCard(thriftDepositCard);

        return thriftDeposit;
    }

    public static DepositHistory fromThrift(ThriftDeposit thriftDeposit) {
        DepositHistory depositHistory = new DepositHistory();
        depositHistory.setSerialNumber(thriftDeposit.getSerialNumber());

        ThriftDepositStatus depositStatus = thriftDeposit.getStatus();
        if (ThriftDepositStatus.FINISHED.equals(depositStatus)) {
            depositHistory.setStatus(1);
        } else {
            depositHistory.setStatus(0);
        }

        depositHistory.setTransactionAmount(thriftDeposit.getTransactionAmount());
        depositHistory.setSubmittedTime(thriftDeposit.getSubmittedTime());
        depositHistory.setFinishedTime(thriftDeposit.getFinishedTime());

        ThriftDepositCard depositCard = thriftDeposit.getDepositCard();
        if (depositCard != null) {
            depositHistory.setDepositCardId(depositCard.getId());
        }

        return depositHistory;
    }

}