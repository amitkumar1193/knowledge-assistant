package com.amitkumar.ai.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class KnowledgeIndexer {

	private final KnowledgeService knowledgeService;
	private final VectorStore vectorStore;
	
	public KnowledgeIndexer(
			KnowledgeService knowledgeService,
			VectorStore vectorStore
			) {
		
		this.knowledgeService = knowledgeService;
		this.vectorStore = vectorStore;
	}
	
	@PostConstruct
	public void loadDocuments() throws IOException {
		String content = knowledgeService.loadKnowledge();
		String fileName = knowledgeService.getKnowledgeFileName();
		AtomicInteger chunkNumber = new AtomicInteger(1);
		
		List<Document> documents = 
				 
				Arrays.stream(content.split("\\R"))
				      .map(String::trim).filter(text -> !text.isBlank())
				      .map(text -> Document.builder().text(text).metadata("source",fileName)
				    		  .metadata("chunkNumber", chunkNumber.getAndIncrement()).build()).toList();
		vectorStore.add(documents);

		System.out.println("Indexed " + documents.size() + " documents from: " + fileName);
	}
}
