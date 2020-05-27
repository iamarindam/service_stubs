package com.sm.mastercard.send.swagger;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootTest
public class Swagger3docApplicationTests {
	private TestRestTemplate restTemplate = new TestRestTemplate();


	public void testHome() throws Exception {
		ResponseEntity<String> res = restTemplate.getForEntity("http://localhost:8089/v3/api-docs", String.class);
		String swagger = res.getBody();
		this.writeFile("swagger.yml", swagger);
	}

	public void writeFile(String fileName, String content) {
		File theDir = new File("target/generated-sources");
		if (!theDir.exists()) {
			try {
				theDir.mkdir();
			} catch (SecurityException se) {
			}
		}
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(theDir + "/" + fileName);
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
