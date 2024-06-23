package com.bloomfilterdemo.bloomfilter;

import com.bloomfilterdemo.bloomfilter.service.FilterService;
import com.bloomfilterdemo.bloomfilter.service.HashService;
import com.bloomfilterdemo.bloomfilter.service.impl.MD5HashService;
import com.bloomfilterdemo.bloomfilter.service.impl.SHA256HashServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class BloomfilterApplication implements CommandLineRunner {

	private final FilterService filterService;

	public BloomfilterApplication(FilterService filterService) {
		this.filterService = filterService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BloomfilterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<HashService> hashServices = Arrays.asList(
				new MD5HashService(), new SHA256HashServiceImpl()
		);
		filterService.setHashServicesAndHashSize(hashServices, 256);
		filterService.add("apple");
		filterService.add("banana");
		filterService.add("cherry");
		System.out.println(filterService.exists("apple"));  // Output: true
		System.out.println(filterService.exists("grape"));  // Output: false
		System.out.println(filterService.exists("banana")); // Output: true
	}
}
