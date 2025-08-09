package com.planningpoker.web.socket;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import com.planningpoker.io.JsonSerializable;

public class JSFunction implements JsonSerializable {
	private final String functionName;
	private final JsonSerializable params;

	public JSFunction(String functionName, final String paramName, final Object param) {
		this(functionName, new JsonSerializable() {
			@Override
			public JsonObject toJson() {
				JsonObjectBuilder builder = Json.createObjectBuilder();
				if(param==null){
					builder.addNull(paramName);
				} else {
					if (param instanceof Integer)
						builder.add(paramName, (Integer)param);
					else
						builder.add(paramName, param.toString());
				}

				return builder.build();
			}
		});
	}

	public JSFunction(String functionName, JsonSerializable params) {
		this.functionName = functionName;
		this.params = params;
	}

	@Override
	public JsonObject toJson() {
		JsonObjectBuilder builder = Json.createObjectBuilder()
		.add("name", functionName );

		if(params==null){
			builder.addNull("params");
		} else {
			builder.add("params", params.toJson());
		}

		return builder.build();
	}

	@Override
	public String toString() {
		return toJson().toString();
	}

}
