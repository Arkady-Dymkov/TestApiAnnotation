package com.dw.ar.tempapisupport.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtValueArgumentList

class TempAPIFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        val annotations = PsiTreeUtil.findChildrenOfType(root, KtAnnotationEntry::class.java)

        for (annotation in annotations) {
            if (annotation.shortName?.asString() == "TempAPI") {
                val valueArgumentList = PsiTreeUtil.findChildOfType(annotation, KtValueArgumentList::class.java)
                val shortDescription = valueArgumentList?.arguments?.getOrNull(0)?.getArgumentExpression()?.text?.trim('"') ?: "TempAPI"

                val range = TextRange(annotation.textRange.startOffset, valueArgumentList?.textRange?.endOffset ?: annotation.textRange.endOffset)
                descriptors.add(FoldingDescriptor(annotation.node, range, null, shortDescription))
            }
        }
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}