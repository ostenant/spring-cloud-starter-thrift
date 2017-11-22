package com.icekredit.rpc.thrift.examples.mapper;

import com.icekredit.rpc.thrift.examples.http.entities.DepositHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DepositHistoryMapper {

    int deleteById(String serialNumber);

    int save(DepositHistory record);

    DepositHistory findById(String serialNumber);

    int update(DepositHistory record);

    List<DepositHistory> findAll();

    List<DepositHistory> findByIds(List<String> list);

    String deleteByIds(List<String> list);

    String saveBatch(List<DepositHistory> list);

    String updateBatch(List<DepositHistory> list);

    List<DepositHistory> queryDepositHistoryList(@Param("depositCardId") String depositCardId);

}