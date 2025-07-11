package com.riders.thelab.feature.mlkit.ui.compose.translate

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.riders.thelab.feature.mlkit.data.local.compose.translate.TranslateDownloadModelState
import com.riders.thelab.feature.mlkit.data.local.compose.translate.TranslateResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class TranslateManager(private val activity: TranslateActivity) {

    private var translatorOptions: TranslatorOptions = TranslatorOptions.Builder()
        .setSourceLanguage(DEFAULT_SOURCE_LANGUAGE)
        .setTargetLanguage(DEFAULT_TARGET_LANGUAGE)
        .build()
    private var translator: Translator = Translation.getClient(translatorOptions)

    private val downloadConditions = DownloadConditions.Builder()
        // .requireWifi()
        .build()

    init {
        translator.let { activity.lifecycle.addObserver(it) }
    }

    fun updateLanguageOptions(fromLanguage: String, toLanguage: String) {
        Timber.d("updateLanguageOptions() | fromLanguage: $fromLanguage | toLanguage: $toLanguage")

        translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(fromLanguage)
            .setTargetLanguage(toLanguage)
            .build()

        setTranslationClient()
    }

    private fun setTranslationClient() {
        Timber.d("setTranslationClient()")
        translator = Translation.getClient(translatorOptions)
    }

    fun downloadModelIfNeeded(): Flow<TranslateDownloadModelState> = callbackFlow {
        Timber.d("downloadModelIfNeeded()")

        translator
            .downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener(activity) {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
                Timber.i("downloadModelIfNeeded() | addOnSuccessListener | model downloaded")

                trySend(TranslateDownloadModelState.Success)
            }
            .addOnFailureListener(activity) { exception ->
                // Model couldn’t be downloaded or other internal error.
                exception.printStackTrace()
                Timber.e("downloadModelIfNeeded() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")

                trySend(TranslateDownloadModelState.Failed(exception.message!!, exception))
            }

        awaitClose {
            Timber.d("downloadModelIfNeeded() | awaitClose")
        }
    }
        .distinctUntilChanged()
        .catch { exception ->
            // Model couldn’t be downloaded or other internal error.
            exception.printStackTrace()
            Timber.e("downloadModelIfNeeded() | catch | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
        }
        .flowOn(Dispatchers.Main)


    fun translate(text: String): Flow<TranslateResultState> = callbackFlow {
        Timber.d("translate() | trimmed text: ${text.trim()}")

        translator
            .downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener(activity) {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
                Timber.i("downloadModelIfNeeded() | addOnSuccessListener | model downloaded")

                translator
                    .translate(text.trim())
                    .addOnSuccessListener(activity) { result ->
                        // string result is returned
                        Timber.d("translate() | addOnSuccessListener | text: $result")

                        trySend(TranslateResultState.Translated(result))
                    }
                    .addOnFailureListener(activity) { exception ->
                        // Model couldn’t be downloaded or other internal error.
                        exception.printStackTrace()
                        Timber.e("translate() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                        trySend(TranslateResultState.Error(exception.message!!, exception))
                    }
            }
            .addOnFailureListener(activity) { exception ->
                // Model couldn’t be downloaded or other internal error.
                exception.printStackTrace()
                Timber.e("translate() | addOnFailureListener | message: ${exception.message} (class: ${exception::class.java.canonicalName})")

                trySend(TranslateResultState.Error(exception.message!!, exception))
            }

        awaitClose {
            Timber.d("translate() | awaitClose")
        }
    }
        .distinctUntilChanged()
        .catch { exception ->
            // Model couldn’t be downloaded or other internal error.
            exception.printStackTrace()
            Timber.e("downloadModelIfNeeded() | catch | message: ${exception.message} (class: ${exception::class.java.canonicalName})")
        }
        .flowOn(Dispatchers.Main)

    companion object {
        val LANGUAGES: List<String> = TranslateLanguage.getAllLanguages().toList()
        const val DEFAULT_SOURCE_LANGUAGE = TranslateLanguage.ENGLISH
        const val DEFAULT_TARGET_LANGUAGE = TranslateLanguage.FRENCH
    }
}