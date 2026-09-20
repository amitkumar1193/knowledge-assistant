package com.amitkumar.ai.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amitkumar.ai.model.SemanticSearchResult;
import com.amitkumar.ai.service.KnowledgeService;

@RestController
public class KnowledgeController {

	public final KnowledgeService knowledgeService;
	
	public KnowledgeController(KnowledgeService knowledgeService) {
		this.knowledgeService = knowledgeService;
	}
	
	@GetMapping("/knowledge")
	public String knowledge() throws IOException {
		return knowledgeService.loadKnowledge();
	}
	
	@GetMapping("/search")
	public List<String> search(@RequestParam String query) throws IOException {
		return knowledgeService.searchKnowledge(query);
	}
	
	
	@GetMapping("/semantic-search")
	public List<SemanticSearchResult> semanticSearch(@RequestParam String query) {
		return knowledgeService.semanticSearch(query);
	}
	
}
