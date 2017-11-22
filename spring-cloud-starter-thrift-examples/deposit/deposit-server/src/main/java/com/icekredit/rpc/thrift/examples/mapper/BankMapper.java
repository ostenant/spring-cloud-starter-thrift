package com.icekredit.rpc.thrift.examples.mapper;

import com.icekredit.rpc.thrift.examples.http.entities.Bank;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BankMapper {

    int deleteById(Long id);

    int save(Bank record);

    Bank findById(Long id);

    int update(Bank record);

    List<Bank> findAll();

    List<Bank> findByIds(List<Long> list);

    long deleteByIds(List<Long> list);

    long saveBatch(List<Bank> list);

    long updateBatch(List<Bank> list);

}