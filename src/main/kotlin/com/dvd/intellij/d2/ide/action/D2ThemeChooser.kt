package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.components.D2Theme
import com.dvd.intellij.d2.ide.editor.images.D2FileEditorImpl.Companion.D2_FILE_THEME
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.d2FileEditor
import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.*

class D2ThemesActionGroup : ActionGroup() {
  override fun getChildren(e: AnActionEvent?): Array<AnAction> = arrayOf(
    *D2Theme.values().map(::D2ThemeAction).toTypedArray(),
    Separator(),
    OpenThemeOverviewAction()
  )
}

class D2ThemeAction(private val theme: D2Theme) : ToggleAction(theme.tName) {

  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

  override fun isSelected(e: AnActionEvent): Boolean =
    (e.d2FileEditor.getUserData(D2_FILE_THEME) ?: D2Theme.DEFAULT) == theme

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    e.d2FileEditor.putUserData(D2_FILE_THEME, theme)
    e.d2FileEditor.refreshD2()
  }
}

class OpenThemeOverviewAction : AnAction(
  D2Bundle["d2.open.theme.documentation"],
  D2Bundle["d2.open.theme.documentation"],
  AllIcons.General.Web
) {
  companion object {
    private const val THEME_DOCS = "https://d2lang.com/tour/themes"
  }

  override fun actionPerformed(e: AnActionEvent) = BrowserUtil.browse(THEME_DOCS)
}