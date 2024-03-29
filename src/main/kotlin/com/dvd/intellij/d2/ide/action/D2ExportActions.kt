package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.*
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.LocalFileSystem

enum class ConversionOutput { SVG, PNG, JPG, TIFF }

class D2ExportAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val defaultFileName = FileUtilRt.getNameWithoutExtension(e.file.presentableName)

    val vfw = FileChooserFactory.getInstance().createSaveFileDialog(
      FileSaverDescriptor(
        D2Bundle["d2.export.image.title"],
        """${D2Bundle["d2.export.image.description"]} ${ConversionOutput.values().joinToString(", ")}""",
        *ConversionOutput.values().map { it.name.lowercase() }.toTypedArray()
      ),
      e.project
    ).save(e.file.parent, defaultFileName) ?: return

    ApplicationManager.getApplication().runWriteAction {
      val d2File = e.d2FileEditor.generatedFile ?: error("no d2 file")
      val converted = service<D2Service>().convert(d2File, ConversionOutput.valueOf(vfw.file.extension.uppercase()))
      vfw.file.writeBytes(converted)

      LocalFileSystem.getInstance().refresh(true)
    }
  }
}

class D2LiveBrowserAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val port = e.d2FileEditor.generatedCommand?.port ?: error("port not found")
    BrowserUtil.browse("http://localhost:$port")
  }
}