package org.notima.util.json;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A utility class to centralize the Json-formatting options. Mainly the date format and the field naming policy.
 * 
 * @author Daniel Tamm
 *
 */
public class JsonUtil {

	private GsonBuilder gsonBuilder;
	private Gson gson;
	private final String dfmtStr = "yyyy-MM-dd";
	private DateFormat dfmt;
	private final String shortdfmtStr = "yyMMdd";
	private DateFormat shortdfmt;

	public JsonUtil() {
		dfmt = new SimpleDateFormat(dfmtStr);
		shortdfmt = new SimpleDateFormat(shortdfmtStr);
	}
	
	private void initDefaultGson() {
		initDefaultGsonBuilder();
		gson = gsonBuilder.create();
	}
	
	private void initDefaultGsonBuilder() {
		gsonBuilder = new GsonBuilder().setPrettyPrinting().setDateFormat(dfmtStr);
		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
		gsonBuilder.setLenient();
		gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonLocalDateAdapter());
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeAdapter());
	}

	public static DateFormat getDateFormat() {
		JsonUtil j = new JsonUtil();
		return j.dfmt;
	}
	
	public static DateFormat getShortDateFormat() {
		JsonUtil j = new JsonUtil();
		return j.shortdfmt;
	}
	
	public Gson getDefaultGson() {
		if (gson==null)
			initDefaultGson();
		return gson;
	}

	public Gson getGsonWithExclusionStrategyForEntities() {
		initDefaultGsonBuilder();
		ExclusionStrategy strategy = new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				if (f==null || f.getName()==null) return true;
				if (f.getName().startsWith("_")) {
					return true;
				}
				return false;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		};
		gsonBuilder.addSerializationExclusionStrategy(strategy);

		return gsonBuilder.create();
	}
	
	public Gson getGsonWithInterfaceAdapterFor(Type typeOfClass, Object adapter) {
		
		initDefaultGsonBuilder();
		gsonBuilder.registerTypeAdapter(typeOfClass, adapter);
		return gsonBuilder.create();
		
	}

	/**
	 * Returns a default gson.
	 * 
	 * @return	A default gson.
	 */
	public static Gson buildGson() {
		JsonUtil j = new JsonUtil();
		return j.getDefaultGson();
	}
	
}
