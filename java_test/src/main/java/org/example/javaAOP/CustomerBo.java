package org.example.javaAOP;

public interface CustomerBo {

        void addCustomer();

        String addCustomerReturnValue();

        void addCustomerThrowException() throws Exception;

        void addCustomerAround(String name);

}

