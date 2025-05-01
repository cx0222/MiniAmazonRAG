## 🔎 RAG for MiniAmazon (CS 516 Project)
This module enhances the "MiniAmazon" project (CS 516) with Retrieval-Augmented Generation (RAG), enabling AI-driven question answering and semantic search over product data. As an extra feature, it brings intelligent, contextual responses similar to those seen in advanced systems like QWen.

### Use Case
Users can interact with the system using natural language — for example:  
> *"Which gaming chairs are comfortable and under $200?"*

The backend retrieves relevant product information and *may* generate a coherent answer based on actual product descriptions and metadata.

Every time a product is added or updated, the following fields are stored and indexed:
- `id` (int -> UUID format)  
- `category_id`  
- `name`  
- `description`  
- `price`  

Only `name` and `description` are embedded into vector space for retrieval; other fields are stored as metadata.

### Technical Stack
- **Database**: PostgreSQL with `pgvector` extension  
- **Vector Index Type**: HNSW (Hierarchical Navigable Small World)  
- **Distance Metric**: Cosine distance  
- **ID Format**: stored as UUID

### 💬 LLM & Embedding Configuration (via Ollama)
```yaml
ollama:
  embedding:
    model: mxbai-embed-large
  chat:
    model: qwen3
    options:
      # Custom options (e.g., temperature, max_tokens) can be configured here
```

### ✨ Qwen3
We use [Qwen3](https://qwenlm.github.io/blog/qwen3/), an advanced open-source language model developed by Alibaba Group. It powers the generation component of our RAG system with strong multilingual and reasoning capabilities, enabling more accurate and context-aware responses.

### 🌐 Workflow Overview
1. User query → Embedded using `mxbai-embed-large`  
2. Top-k relevant documents retrieved from the vector store  
3. Context passed to `Qwen3` for answer generation  
4. Final response sent back to frontend

### 🖥️ Frontend Integration
- Fully integrated with MiniAmazon’s UI  
- Results displayed in a chat-style or Q&A view  
- Fast and responsive — designed to work smoothly with the full-text search feature

### 🙏 Acknowledgement
This project is inspired by and adapted from:  
👉 [benayat/rag-with-spring-ai](https://github.com/benayat/rag-with-spring-ai)  
Special thanks to the open-source community and the [Qwen team](https://qwenlm.github.io) 💗
