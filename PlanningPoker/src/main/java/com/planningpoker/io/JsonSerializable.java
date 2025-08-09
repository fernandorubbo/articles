package com.planningpoker.io;

import jakarta.json.JsonObject;


public interface JsonSerializable {

	JsonObject toJson();
}
