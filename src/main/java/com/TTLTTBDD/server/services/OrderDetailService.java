package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;


    public List<Object[]> getRevenueByCategory(){
        return orderDetailRepository.getRevenueByCategory();
    }
}
