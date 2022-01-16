package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@RestController
@SpringBootApplication
public class DemoApplication {
	ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * @description json 형변환 string <-> json <-> object
	 * @summary
	 */
	@GetMapping(value = "/json")
	public String json() {
		DemoObj dObj = new DemoObj();
		dObj.userId = "user1";
		dObj.userName = "사용자1";
		dObj.isActive = true;
		dObj.userLevel = 2;
		dObj.skilltree = DemoObj.POSITION.CEO;

		String jsonString = "";
		String un = "";
		DemoObj jsonToObj = null;

		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dObj);
			//mapper.writeValue(new File("target/j.json"), dObj);

			JsonNode jn = mapper.readTree(jsonString);
			un = jn.get("userName").asText();

			jsonToObj = mapper.readValue(jsonString, DemoObj.class);

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return jsonToObj.userId;
	}

	/**
	 * @description 오브젝트 merge 확인
	 * @summary
	 */
	@GetMapping(value = "/merge")
	public String mergeTest() {

		DemoObj dObj1 = new DemoObj();
		dObj1.userId = "user1";
		dObj1.userName = "사용자1";
		dObj1.isActive = true;
		dObj1.userLevel = 2;
		dObj1.skilltree = DemoObj.POSITION.CEO;

		DemoObj dObj2 = new DemoObj();
		dObj2.userId = "user1";
		dObj2.userName = "사용자1";
		dObj2.isActive = true;
		dObj2.userLevel = 3;
		dObj2.skilltree = DemoObj.POSITION.CEO;

		/*
		for(Field field : dObj1.getClass().getDeclaredFields()) {
			System.out.println(field.getAnnotation(Merge.class));
		}

		System.out.println(dObj1);
		System.out.println(dObj2);
		*/

		System.out.println(CommonUtil.merge(dObj1, dObj2));

		return "";
	}
}

