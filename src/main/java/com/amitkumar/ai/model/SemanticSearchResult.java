package com.amitkumar.ai.model;

import java.util.Map;

public record SemanticSearchResult (String id, String content, Double score, Map<String, Object> metadata){

}
