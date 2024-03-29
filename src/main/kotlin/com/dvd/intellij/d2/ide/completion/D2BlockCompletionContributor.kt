package com.dvd.intellij.d2.ide.completion

import com.dvd.intellij.d2.ide.utils.D2Icons
import com.dvd.intellij.d2.ide.utils.KEYWORD_HOLDERS
import com.dvd.intellij.d2.ide.utils.RESERVED_KEYWORDS
import com.dvd.intellij.d2.lang.D2ElementTypes
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

class D2BlockCompletionContributor : CompletionContributor() {
  private val keywords = listOf(*RESERVED_KEYWORDS, *KEYWORD_HOLDERS).map {
    LookupElementBuilder.create(it)
      .withIcon(D2Icons.ATTRIBUTE)
      .withInsertHandler { context, _ ->
        with(context) {
          if (it in RESERVED_KEYWORDS) {
            val caretOffset = editor.caretModel.offset

            val colon = ": "
            document.insertString(selectionEndOffset, colon)
            editor.caretModel.moveToOffset(caretOffset + colon.length)
          }
        }
      }
  }

  // todo
  init {
    extend(CompletionType.BASIC,
      PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(D2ElementTypes.BLOCK_DEFINITION)),
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
          parameters: CompletionParameters,
          context: ProcessingContext,
          result: CompletionResultSet
        ) {
          // todo: add defined shapes
          result.addAllElements(keywords)
        }
      }
    )
  }
}