package com.ml.shubham0204.docease.ui.screens.docs

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ml.shubham0204.docease.data.Chunk
import com.ml.shubham0204.docease.data.ChunksDB
import com.ml.shubham0204.docease.data.Document
import com.ml.shubham0204.docease.data.DocumentsDB
import com.ml.shubham0204.docease.domain.embeddings.SentenceEmbeddingProvider
import com.ml.shubham0204.docease.domain.readers.Readers
import com.ml.shubham0204.docease.domain.splitters.WhiteSpaceSplitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import setProgressDialogText
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.min

@KoinViewModel
class DocsViewModel(
    private val documentsDB: DocumentsDB,
    private val chunksDB: ChunksDB,
    private val sentenceEncoder: SentenceEmbeddingProvider,
) : ViewModel() {
    suspend fun addDocument(
        inputStream: InputStream,
        fileName: String,
        documentType: Readers.DocumentType,
    ) = withContext(Dispatchers.IO) {
        val text =
            Readers.getReaderForDocType(documentType).readFromInputStream(inputStream)
                ?: return@withContext
        val newDocId =
            documentsDB.addDocument(
                Document(
                    docText = text,
                    docFileName = fileName,
                    docAddedTime = System.currentTimeMillis(),
                ),
            )
        setProgressDialogText("Creating chunks...")
        val chunks = WhiteSpaceSplitter.createChunks(text, chunkSize = 500, chunkOverlap = 50)
        setProgressDialogText("Adding chunks to database...")
        val size = chunks.size
        chunks.forEachIndexed { index, s ->
            setProgressDialogText("Added ${index + 1}/$size chunk(s) to database...")
            val embedding = sentenceEncoder.encodeText(s)
            chunksDB.addChunk(
                Chunk(
                    docId = newDocId,
                    docFileName = fileName,
                    chunkData = s,
                    chunkEmbedding = embedding,
                ),
            )
        }
    }

    suspend fun addDocumentFromUrl(
        url: String,
        context: Context,
        onDownloadComplete: (Boolean) -> Unit,
    ) = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val fileName = getFileNameFromURL(url)
                val file = File(context.cacheDir, fileName)

                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                // Determine the document type based on the file extension
                // Add handle for unknown types if supported
                val documentType =
                    when (fileName.substringAfterLast(".", "").lowercase()) {
                        "pdf" -> Readers.DocumentType.PDF
                        "docx" -> Readers.DocumentType.MS_DOCX
                        "doc" -> Readers.DocumentType.MS_DOCX
                        else -> Readers.DocumentType.UNKNOWN
                    }

                // Pass file to your document handling logic
                addDocument(file.inputStream(), fileName, documentType)

                withContext(Dispatchers.Main) {
                    onDownloadComplete(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onDownloadComplete(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onDownloadComplete(false)
            }
        }
    }

    fun getAllDocuments(): Flow<List<Document>> = documentsDB.getAllDocuments()

    fun removeDocument(docId: Long) {
        documentsDB.removeDocument(docId)
        chunksDB.removeChunks(docId)
    }

    fun getDocsCount(): Long = documentsDB.getDocsCount()

    // Extracts the file name from the URL
    // Source: https://stackoverflow.com/a/11576046/13546426
    private fun getFileNameFromURL(url: String?): String {
        if (url == null) {
            return ""
        }
        try {
            val resource = URL(url)
            val host = resource.host
            if (host.isNotEmpty() && url.endsWith(host)) {
                return ""
            }
        } catch (e: MalformedURLException) {
            return ""
        }
        val startIndex = url.lastIndexOf('/') + 1
        val length = url.length
        var lastQMPos = url.lastIndexOf('?')
        if (lastQMPos == -1) {
            lastQMPos = length
        }
        var lastHashPos = url.lastIndexOf('#')
        if (lastHashPos == -1) {
            lastHashPos = length
        }
        val endIndex = min(lastQMPos.toDouble(), lastHashPos.toDouble()).toInt()
        return url.substring(startIndex, endIndex)
    }
}
