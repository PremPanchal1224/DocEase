# 📄 DocEase – Intelligent Document Assistant

DocEase is an Android app that helps you understand documents faster using summaries and Q&A powered by AI.

---

## 🚀 Key Features

- 📂 Import PDF & DOCX files
- 🧠 Ask questions in natural language
- 📝 Get instant summaries
- 🔍 See source text for each answer
- ⚡ Fast, interactive responses

---

## ⚙️ How It Works

1. **Document Import**  
   - Uses iTextPDF (PDF) & Apache POI (DOCX)

2. **Text Chunking**  
   - Breaks text into overlapping parts for better context

3. **Embeddings + Storage**  
   - Uses `all-MiniLM-L6-V2` to embed chunks  
   - Stores them in ObjectBox (on-device vector DB)

4. **Semantic Search + Answering**  
   - Finds relevant chunks with cosine similarity  
   - Sends them + your question to Gemini 1.5 Flash for a smart answer

---

## 🧰 Tech Stack

- Kotlin (Android)
- iTextPDF, Apache POI
- ObjectBox
- Sentence-Transformers (MiniLM)
- Gemini Android SDK
- 
## 🎥 Demo Video

<video src="media/DocEase_WorkingDemo.mp4" controls width="600">
  Your browser does not support the video tag.
</video>


