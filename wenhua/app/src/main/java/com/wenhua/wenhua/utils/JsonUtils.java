package com.wenhua.wenhua.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Google Gson解析Json数据实例
 * 
 * @author  zoujunda
 * @date  2016-1-12 上午10:06:24
 */
public class JsonUtils {

	private static JsonUtils instance;
	private Gson gson = null;
	private GsonBuilder gsonBuilder = null;

	public static JsonUtils getInstance(){
		if(instance == null){
			instance = new JsonUtils();
		}
		return instance;
	}

	/**
	 * gsonBuilder.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS") 时间转化为特定格式
	 */
	private JsonUtils(){
		gsonBuilder = new GsonBuilder();
		// 不导出实体中没有用@Expose注解的属性
		// gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		// 支持Map的key为复杂对象的形式
		gsonBuilder.enableComplexMapKeySerialization();
		gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		gson = gsonBuilder.create();
	}

	/**
	 * 
	 * @Description JAVA集合对象转换成JSON字符串 
	 * @param map JAVA集合对象
	 * @return JSON字符串
	 */
	public <T> String obj2Json(Map<String, T> map){
		if (map != null && map.size() > 0){
			Type type = new com.google.gson.reflect.TypeToken<T>()
					{}.getType();
					return gson.toJson(map, type);
		}
		return null;
	}
	
	/**
	 * 
	 * @Description JAVA数组对象转换成JSON字符串 
	 * @param list JAVA数组对象
	 * @return JSON字符串
	 */
	public <T> String obj2Json(List<T> list){
		if (list != null && list.size()>0){
			Type type = new com.google.gson.reflect.TypeToken<T>(){}.getType();
			return  gson.toJson(list, type);
		}
		return null ;
	}
	
	/**
	 * 
	 * @Description JAVA对象转换成JSON字符串 
	 * @param object JAVA对象
	 * @return JSON字符串
	 */
	public String obj2Json(Object object){
		if (object != null){
			return gson.toJson(object);
		}
		return null;
	}


	/**
	 * 
	 * @Description JSON字符串转换成JAVA集合对象
	 * @param mapJson Map形式JSON字符串
	 * @return JAVA集合对象
	 */
	public <T> Map<String, T> json2Map(String mapJson){
		if (mapJson != null && !"".equals(mapJson)){
			Type type = new com.google.gson.reflect.TypeToken<T>(){}.getType();
			return gson.fromJson(mapJson, type);
		}
		return null;
	}


	/**
	 * 
	 * @Description JSON字符串转换成JAVA数组对象
	 * @param arrayJson array形式JSON字符串
	 * @param token
	 * @return JAVA数组对象
	 */
	public <T> List<T> json2List(String arrayJson,Type token){
		if (arrayJson != null && !"".equals(arrayJson)){
			return  gson.fromJson(arrayJson,token);
		}
		return null ;
	}


	/**
	 * 
	 * @Description JSON字符串转换成JAVA对象
	 * @param jsonObject JSON字符串
	 * @param clazz JAVA对象
	 * @return T JAVA对象
	 */
	public <T> T json2Obj(String jsonObject, Class<T> clazz){
		if (jsonObject != null && !"".equals(jsonObject)){
			return gson.fromJson(jsonObject, clazz);
		}
		return null;
	}


	/**
	 * 
	 * @Description  获取JSON字符串中的值
	 * @param jsonObject JSON字符串
	 * @param key 键
	 * @return Object JAVA对象
	 */
	public Object getJsonValue(String jsonObject, String key){
		try{
			JSONObject json = new JSONObject(jsonObject);
			return json.get(key);
		} catch (JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期解序列实用工具类
	 */
	public class DateSerializerUtils implements JsonSerializer<Date> {
		@Override
		public JsonElement serialize(Date date, Type type,
									 JsonSerializationContext content){
			return new JsonPrimitive(date.getTime());
		}
	}

	/**
	 * 日期序列化实用工具类
	 */
	public class DateDeserializerUtils implements JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonElement json, Type type,
								JsonDeserializationContext context) throws JsonParseException {
			return new Date(json.getAsJsonPrimitive().getAsLong());
		}
	}
}

