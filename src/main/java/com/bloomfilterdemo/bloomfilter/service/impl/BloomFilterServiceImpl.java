package com.bloomfilterdemo.bloomfilter.service.impl;

import com.bloomfilterdemo.bloomfilter.service.FilterService;
import com.bloomfilterdemo.bloomfilter.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BloomFilterServiceImpl implements FilterService {

//    @Value("${hash.hash-size}")
    private int hashSize = 256;

    List<Boolean> hashBucket = new ArrayList<>(Collections.nCopies(hashSize, false));

    @Autowired
    private List<HashService> hashServices;

    public BloomFilterServiceImpl() {
    }

    @Override
    public void setHashServicesAndHashSize(List<HashService> hashServices, int hashSize) {
        this.hashServices = hashServices;
        this.hashSize = hashSize;
        this.hashBucket = new ArrayList<>(Collections.nCopies(hashSize, false));
    }

    @Override
    public boolean exists(String item) {
        for(HashService hashService : hashServices) {
            int value = hashService.getHash(item);
            if(!hashBucket.get(value % hashSize)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void add(String item) {
        for(HashService hashService : hashServices) {
            int value = hashService.getHash(item);
            hashBucket.set(value % hashSize, true);
        }
    }
}
