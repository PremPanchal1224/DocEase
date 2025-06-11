# 📄 DocEase – Your Intelligent Document Assistant

DocEase is an Android application that simplifies working with documents by providing intelligent summaries and answering questions based on document content. It leverages a Retrieval-Augmented Generation (RAG) pipeline, combining local processing with a powerful hosted Large Language Model (LLM) for accurate, context-aware responses.

---

## 🚀 Features

- 📂 **Upload PDF/DOCX** – Seamlessly import documents in PDF and DOCX formats.
- 🧠 **Natural Language Q&A** – Ask questions in plain language and receive accurate answers based on the document content.
- 📝 **Automatic Summarization** – Generate concise summaries of long documents.
- 🔍 **Context-Aware Answers** – View the source text from which the answer was derived.
- 💬 **Fast and Interactive** – Uses semantic search and LLM integration for fast and coherent responses.

---

## 🔄 System Workflow

### 📥 Importing Documents
Users can upload PDF and DOCX documents.  
- `PDFReader.kt` uses **iTextPDF**
- `DOCXReader.kt` uses **Apache POI**

### ✂️ Chunking and Text Preprocessing
- Extracted text is split into overlapping "chunks" using `WhiteSpaceSplitter.kt`.  
- Ensures semantic continuity across segments  
- Enhances retrieval accuracy during semantic search

### 📊 Embedding and Vector Storage
- Each chunk is embedded using the **all-MiniLM-L6-V2** model via `SentenceEmbeddingProvider.kt`, and stored in **ObjectBox**, a fast on-device NoSQL vector DB.  
- Embeddings indexed with `chunkId` for efficient search

### 🔎 Semantic Search and Query Handling
- User queries are embedded and compared against stored chunks using cosine similarity.  
- Retrieves top-K relevant chunks based on semantic similarity

### 🤖 LLM-based Answer Generation
- Retrieved chunks and the query are integrated into a dynamic prompt template.  
- Sent to **Gemini-1.5-Flash** via the **Gemini Android SDK**  
- Returns a fluent and contextually accurate answer

---

## 🧰 Tech Stack

- Android (Kotlin)
- iTextPDF, Apache POI – Document parsing
- ObjectBox – Local vector storage
- all-MiniLM-L6-V2 – Embedding model
- Gemini Android SDK – Hosted LLM for answer generation

---

## 📌 Future Enhancements

- Support for additional file types
- Enhanced UI/UX for document navigation
- Integration with speech-to-text for voice-based queries


