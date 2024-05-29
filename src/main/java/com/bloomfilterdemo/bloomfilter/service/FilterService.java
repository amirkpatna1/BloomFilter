package com.bloomfilterdemo.bloomfilter.service;

import java.util.List;

public interface FilterService {
    void setHashServicesAndHashSize(List<HashService> hashServices, int hashSize);

    boolean exists(String item);

    void add(String item);
}
