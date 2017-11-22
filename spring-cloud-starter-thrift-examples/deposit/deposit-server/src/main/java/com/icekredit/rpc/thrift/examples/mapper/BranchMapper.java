package com.icekredit.rpc.thrift.examples.mapper;

import com.icekredit.rpc.thrift.examples.http.entities.Branch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BranchMapper {

    int deleteById(Long id);

    int save(Branch record);

    Branch findById(Long id);

    int update(Branch record);

    List<Branch> findAll();

    List<Branch> findByIds(List<Long> list);

    long deleteByIds(List<Long> list);

    long saveBatch(List<Branch> list);

    long updateBatch(List<Branch> list);

    List<Branch> queryAllBranchesByBankId(@Param("bankId") Long bankId);
}