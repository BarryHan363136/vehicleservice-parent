package com.bmw.vehicleservice.parse.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class JsonUtils {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static JsonFactory jsonFactory = new JsonFactory();

	public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass){
		Object object = null;
		try {
			object = jsonMapper.readValue(jsonAsString, pojoClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	public static <T> Object fromJson2(String jsonAsString, Class<T> pojoClass){
		Object object = null;
		try {
			jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
			object = jsonMapper.readValue(jsonAsString, pojoClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	public static <T> Object fromJson(FileReader fr, Class<T> pojoClass)
			throws JsonParseException, IOException {
		return jsonMapper.readValue(fr, pojoClass);
	}

	@SuppressWarnings("deprecation")
	public static String toJson(Object pojo, boolean prettyPrint) throws IOException {
		StringWriter sw = new StringWriter();
		JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
		if (prettyPrint) {
			jg.useDefaultPrettyPrinter();
		}
		jsonMapper.writeValue(jg, pojo);
		return sw.toString();
	}

	@SuppressWarnings("deprecation")
	public static void toJson(Object pojo, FileWriter fw, boolean prettyPrint) throws IOException {
		JsonGenerator jg = jsonFactory.createJsonGenerator(fw);
		if (prettyPrint) {
			jg.useDefaultPrettyPrinter();
		}
		jsonMapper.writeValue(jg, pojo);
	}

	/**
	 * 属性为 空（“”） 或者为 NULL 是否序列化
	 * @param obj
	 * @return
	 */
	public static String writeObjectAsString(Object obj, Boolean flag){
		String jsonstr = "";
		try {
			if(obj!=null){
				if(flag == true) {
					//属性为 空（“”） 或者为 NULL 都不序列化
					jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				}
				jsonstr = jsonMapper.writeValueAsString(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonstr;
	}

	/**
	 * 属性为 空（“”） 或者为 NULL 都序列化
	 * @param obj
	 * @return
	 */
	public static String writeObjectAsString(Object obj){
		return writeObjectAsString(obj,false);
	}

	public static void main(String[] args) {

	}
}