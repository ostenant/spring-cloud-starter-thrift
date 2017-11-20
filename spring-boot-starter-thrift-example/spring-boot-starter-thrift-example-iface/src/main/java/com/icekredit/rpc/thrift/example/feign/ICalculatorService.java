package com.icekredit.rpc.thrift.example.feign;

public interface ICalculatorService {

    int add(int arg1, int arg2) throws Exception;

    int subtract(int arg1, int arg2) throws Exception;

    int multiply(int arg1, int arg2) throws Exception;

    int division(int arg1, int arg2) throws Exception;

}

