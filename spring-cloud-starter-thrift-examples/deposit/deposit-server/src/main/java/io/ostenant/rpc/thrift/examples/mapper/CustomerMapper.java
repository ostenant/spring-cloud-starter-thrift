package io.ostenant.rpc.thrift.examples.mapper;

import io.ostenant.rpc.thrift.examples.http.entities.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface CustomerMapper {

    int deleteById(String id);

    int save(Customer record);

    Customer findById(String id);

    int update(Customer record);

    List<Customer> findAll();

    List<Customer> findByIds(List<String> list);

    String deleteByIds(List<String> list);

    String saveBatch(List<Customer> list);

    String updateBatch(List<Customer> list);

}