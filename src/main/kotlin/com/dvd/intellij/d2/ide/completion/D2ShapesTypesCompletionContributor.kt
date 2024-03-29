package com.dvd.intellij.d2.ide.completion

import com.dvd.intellij.d2.ide.utils.Shapes
import com.dvd.intellij.d2.lang.D2ElementTypes
import com.dvd.intellij.d2.lang.psi.impl.D2ShapeDefinitionImpl
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext

class D2ShapesTypesCompletionContributor : CompletionContributor() {

  private val shapes = Shapes.values().map {
    LookupElementBuilder.create(it.prettyName)
      .withIcon(AllIcons.Nodes.Constant)
  }

  init {
    extend(
      CompletionType.BASIC,
      PlatformPatterns.psiElement(D2ElementTypes.IDENTIFIER),
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
          parameters: CompletionParameters,
          context: ProcessingContext,
          result: CompletionResultSet
        ) {
          val parent = parameters.position.parentOfType<D2ShapeDefinitionImpl>() ?: return
          parent.childrenOfType<D2ShapeDefinitionImpl>().firstOrNull { it.text == "shape" } ?: return

          result.addAllElements(shapes)
        }
      }
    )
  }

}

