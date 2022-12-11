package com.dvd.intellij.d2.ide.format

import com.dvd.intellij.d2.ide.file.D2File
import com.dvd.intellij.d2.ide.service.D2Service
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.openapi.components.service
import com.intellij.psi.PsiFile

class D2FormatterService : AsyncDocumentFormattingService() {

  override fun getName(): String = "D2 Formatter"
  override fun getNotificationGroupId(): String = "D2_INFO"
  override fun getFeatures(): MutableSet<FormattingService.Feature> = mutableSetOf()
  override fun canFormat(file: PsiFile): Boolean = file is D2File

  override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
    val service = service<D2Service>()

    if (!service.isCompilerInstalled) return null
    val file = request.ioFile ?: return null

    return object : FormattingTask {
      override fun run() {
        val out = service.format(file)
        request.onTextReady(out)
      }

      override fun cancel(): Boolean = true
    }
  }

}