package com.amitkumar.ai.embedding;


import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Component;

import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

@Component
public class LangChain4jEmbeddingAdapter implements EmbeddingModel {

    private final AllMiniLmL6V2EmbeddingModel delegate = new AllMiniLmL6V2EmbeddingModel();

    // --- Small embed(Document) method ---
    @Override
    public float[] embed(Document document) {
        return delegate.embed(document.getText()).content().vector();
       
    }


    // --- call(EmbeddingRequest) method ---
    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<String> instructions = request.getInstructions();
        List<Embedding> embeddings = new ArrayList<>();

        for (int index = 0; index< instructions.size();index++) {
            float[] vector = delegate.embed(instructions.get(index)).content().vector();
            embeddings.add(new Embedding(vector, index));
        }

        return new EmbeddingResponse(
                embeddings
        );
    }

    @Override
    public int dimensions() {
        return 384;
    }
}
