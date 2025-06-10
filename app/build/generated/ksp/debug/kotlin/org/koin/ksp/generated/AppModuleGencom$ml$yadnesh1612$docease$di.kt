package org.koin.ksp.generated

import org.koin.core.module.Module
import org.koin.dsl.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel

public val com_ml_yadnesh1612_docease_di_AppModule : Module = module {
	single() { com.ml.yadnesh1612.docease.data.ChunksDB() } 
	single() { com.ml.yadnesh1612.docease.data.DocumentsDB() } 
	single() { com.ml.yadnesh1612.docease.data.GeminiAPIKey(context=get()) } 
	single() { com.ml.yadnesh1612.docease.domain.embeddings.SentenceEmbeddingProvider(context=get()) } 
	viewModel() { com.ml.yadnesh1612.docease.ui.screens.chat.ChatViewModel(documentsDB=get(),chunksDB=get(),geminiAPIKey=get(),sentenceEncoder=get()) } 
	viewModel() { com.ml.yadnesh1612.docease.ui.screens.docs.DocsViewModel(app=get(),documentsDB=get(),chunksDB=get(),sentenceEncoder=get()) } 
	viewModel() { com.ml.yadnesh1612.docease.ui.screens.edit_api_key.EditAPIKeyViewModel(geminiAPIKey=get()) } 
}
public val com.ml.yadnesh1612.docease.di.AppModule.module : org.koin.core.module.Module get() = com_ml_yadnesh1612_docease_di_AppModule