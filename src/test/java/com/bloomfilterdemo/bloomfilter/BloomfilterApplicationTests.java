package com.bloomfilterdemo.bloomfilter;

import com.bloomfilterdemo.bloomfilter.service.AdvancedHashService;
import com.bloomfilterdemo.bloomfilter.service.FilterService;
import com.bloomfilterdemo.bloomfilter.service.HashService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest
class BloomfilterApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(BloomfilterApplicationTests.class);
	@Autowired
	private FilterService bloomFilterService;

	@Autowired
	private List<AdvancedHashService> advancedHashServices;

	@Autowired
	private List<HashService> allHashServices;


	@Test
	void usingAdvancedHashes() {

		int sampleSize = 100000, querySize = 100000;
		int falsePositive = 0, trueNegative = 0;
		int hashSize = 100000;
		logger.info("***************************");
		logger.info("Advanced hashes with sample size {} querySize {} hashSize {}", sampleSize, querySize, hashSize);
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		bloomFilterService.setHashServicesAndHashSize(advancedHashServices.stream().map(x -> (HashService) x).collect(Collectors.toList()), hashSize);
		for(int i = 0; i < sampleSize; i += 1) {
			int randomLength = random.nextInt(10);
			String s = generateRandomString(randomLength + 1, characters);
			bloomFilterService.add(s);
		}

		for(int i = 0; i < querySize; i += 1) {
			int randomLength = random.nextInt(10);
			String s = generateRandomString(randomLength + 1, characters);
			if(bloomFilterService.exists(s)) {
				falsePositive += 1; // taking it as a falsePositive as we won't be sure if it exists
			} else {
				trueNegative += 1; // taking it as a trueNegative as we will be sure that it does not exists
			}

		}
		double probability = (double) trueNegative / querySize;
		System.out.printf("False positives: %d%n", falsePositive);
		System.out.printf("True Negatives: %d%n", trueNegative);
		System.out.printf("probability of correct result: %.4f%n", probability);
		System.out.printf("Mathematical probability: %.4f%n", getMathematicalProbability(hashSize, sampleSize, allHashServices.size()));
		logger.info("***************************");
	}

	@Test
	void usingAllHashes() {
		int sampleSize = 1000, querySize = 10000;
		int falsePositive = 0, trueNegative = 0;
		int hashSize = 100000;
		logger.info("***************************");
		logger.info("All hashes with sample size {} querySize {} hashSize {}", sampleSize, querySize, hashSize);
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345";
		Random random = new Random();
		bloomFilterService.setHashServicesAndHashSize(allHashServices, hashSize);
		for(int i = 0; i < sampleSize; i += 1) {
			int randomLength = random.nextInt(10);
			String s = generateRandomString(randomLength + 1, characters);
			bloomFilterService.add(s);
		}

		for(int i = 0; i < querySize; i += 1) {
			int randomLength = random.nextInt(10);
			String s = generateRandomString(randomLength + 1, characters);
			if(bloomFilterService.exists(s)) {
				falsePositive += 1; // taking it as a falsePositive as we won't be sure if it exists
			} else {
				trueNegative += 1; // taking it as a trueNegative as we will be sure that it does not exists
			}

		}
		double probability = (double) trueNegative / querySize;
		System.out.printf("False positives: %d%n", falsePositive);
		System.out.printf("True Negatives: %d%n", trueNegative);
		System.out.printf("probability of correct result: %.4f%n", probability);
		System.out.printf("Mathematical probability: %.4f%n", getMathematicalProbability(hashSize, sampleSize, allHashServices.size()));
		logger.info("***************************");
	}

	@Test
	void withLessCharacters() {
		// here total sample is 3125 i.e we can generate at max 3125 unique strings
		// and we are generating 100 strings out of 3125
		// so probability of collision is 100 / 3125 ~ 0.032
		//
		int hashSize = 256;
		int sampleSize = 100, querySize = 10000;
		int falsePositive = 0, trueNegative = 0;
		String characters = "ABCDE";
		logger.info("***************************");
		logger.info("All hashes with sample size {} querySize {} hashSize {}", sampleSize, querySize, hashSize);
		bloomFilterService.setHashServicesAndHashSize(allHashServices, hashSize);
		for(int i = 0; i < sampleSize; i += 1) {
			String s = generateRandomString(5, characters);
			bloomFilterService.add(s);
		}

		for(int i = 0; i < querySize; i += 1) {
			String s = generateRandomString(5, characters);
			if(bloomFilterService.exists(s)) {
				falsePositive += 1;
			} else {
				trueNegative += 1;
			}

		}
		double probability = (double) trueNegative / querySize;
		System.out.printf("False positives: %d%n", falsePositive);
		System.out.printf("True Negatives: %d%n", trueNegative);
		System.out.printf("probability of correct result: %.4f%n", probability);
		System.out.printf("Mathematical probability: %.4f%n", getMathematicalProbability(hashSize, sampleSize, allHashServices.size()));
		logger.info("***************************");
	}

	public static String generateRandomString(int length, String characters) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			char randomChar = characters.charAt(index);
			sb.append(randomChar);
		}
		return sb.toString();
	}

	public static double getMathematicalProbability(int numberOfBitsInBloomFilter, int numberOfItemsInserted, int numberOfHashFunctionsUsed) {
		double falsePositiveProbability = Math.pow(
				1 - Math.exp(-numberOfHashFunctionsUsed * (double) numberOfItemsInserted / numberOfBitsInBloomFilter),
				numberOfHashFunctionsUsed
		);
		return 1 - falsePositiveProbability;
	}

}
