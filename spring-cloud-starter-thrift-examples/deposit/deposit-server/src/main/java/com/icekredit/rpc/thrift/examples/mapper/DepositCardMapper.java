package com.icekredit.rpc.thrift.examples.mapper;

import com.icekredit.rpc.thrift.examples.http.entities.DepositCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DepositCardMapper {

    int deleteById(String id);

    int save(DepositCard record);

    DepositCard findById(String id);

    int update(DepositCard record);

    List<DepositCard> findAll();

    List<DepositCard> findByIds(List<String> list);

    String deleteByIds(List<String> list);

    String saveBatch(List<DepositCard> list);

    String updateBatch(List<DepositCard> list);

    List<DepositCard> queryAllDepositCards(@Param("customerId") String customerId);

    void decrementMoney(@Param("depositCardId") String depositCardId, @Param("money") Double money);

    void incrementMoney(@Param("depositCardId") String depositCardId, @Param("money") Double money);

    Long countRowsByCustomerId(@Param("customerId") String customerId);

}