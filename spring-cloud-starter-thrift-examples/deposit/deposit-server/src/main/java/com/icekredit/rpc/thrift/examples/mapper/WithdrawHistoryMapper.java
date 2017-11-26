package com.icekredit.rpc.thrift.examples.mapper;

import com.icekredit.rpc.thrift.examples.http.entities.WithdrawHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface WithdrawHistoryMapper {

    int deleteById(String serialNumber);

    int save(WithdrawHistory record);

    WithdrawHistory findById(String serialNumber);

    int update(WithdrawHistory record);

    List<WithdrawHistory> findAll();

    List<WithdrawHistory> findByIds(List<String> list);

    String deleteByIds(List<String> list);

    String saveBatch(List<WithdrawHistory> list);

    String updateBatch(List<WithdrawHistory> list);

    List<WithdrawHistory> queryWithdrawHistoryList(@Param("depositCardId") String depositCardId);
}