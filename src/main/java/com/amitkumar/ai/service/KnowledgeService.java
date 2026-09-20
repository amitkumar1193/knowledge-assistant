package com.amitkumar.ai.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amitkumar.ai.model.SemanticSearchResult;

@Service
public class KnowledgeService {

	private final Path knowledgeFilePath;
	private final VectorStore vectorStore;
	
	public KnowledgeService(
			@Value("${knowledge.file.path}")
			String filePath,VectorStore vectorStore)
	{
		this.knowledgeFilePath = Path.of(filePath);
		this.vectorStore = vectorStore;
		
	}

	public String loadKnowledge() throws IOException {
		if(!Files.exists(knowledgeFilePath)) {
			throw new IOException("File does not exist: " + knowledgeFilePath.toAbsolutePath());
		}
		return Files.readString(knowledgeFilePath);
	}

	public List<String> searchKnowledge(String query) throws IOException {
		String fileContent = loadKnowledge();
		List<String> chunks = Arrays.stream(fileContent.split("\\R\\s*\\R"))
				                                       .map(String::trim)
				                                       .filter(chunk -> !chunk.isBlank())
				                                       .toList();
		
		Set<String> stopWords = Set.of("a","an","the","and","or","but","is","are","was","were","be","been","being","of","in","on","to","for","with","as","at","by");

		List<String> queryWords = Arrays.stream(query.toLowerCase().split("\\W+"))
				.map(String::trim)
				.filter(word -> !word.isBlank())
				.filter(word -> !stopWords.contains(word)).distinct().toList();
		
		return chunks.stream().map(chunk -> new SearchResult(chunk, calculateStore(chunk, queryWords)))
				                                                   .filter(result -> result.score() > 0)
				                                                   .sorted(Comparator.comparingInt(SearchResult::score)
				                                                   .reversed()).map(SearchResult::content).toList();
		
	}

	public List<SemanticSearchResult> semanticSearch(String query) {
		// TODO Auto-generated method stub
		SearchRequest searchRequest = SearchRequest.builder().query(query)
				.topK(5).similarityThreshold(0.3).build();
		
		List<Document> documents = vectorStore.similaritySearch(searchRequest);
		
		return documents.stream().map(document -> 
		new SemanticSearchResult(document.getId(), document.getText(), document.getScore(), document.getMetadata())).toList();
	}
	
	
	private int calculateStore(String chunk, List<String> queryWords) {
		String normalizedChunk = chunk.toLowerCase();
		
		return (int) queryWords.stream().filter(normalizedChunk::contains).count();
	}
	
	private record SearchResult(String content, int score) {
		
	}
	
	public String getKnowledgeFileName() {
		return knowledgeFilePath.getFileName().toString();
	}
	
}
