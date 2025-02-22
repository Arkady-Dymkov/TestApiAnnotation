package com.dw.ar.tempapisupport.folding

import com.dw.ar.TempAPI
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.toUElement

class TempAPICodeFoldingBuilder : FoldingBuilderEx() {

    override fun getPlaceholderText(node: ASTNode): String = "..."

    override fun buildFoldRegions(
        root: PsiElement,
        document: Document,
        quick: Boolean,
    ): Array<out FoldingDescriptor?> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        PsiTreeUtil.processElements(root) { element ->
            val uElement = element.toUElement()
            if (uElement is UAnnotated) {
                val annotation = uElement.findAnnotation(TempAPI::class.java.canonicalName)
                if (annotation != null) {
                    val shortDescription =
                        annotation.findAttributeValue("shortDescription")?.evaluate() as? String ?: "Temporary solution"
                    val range = element.textRange
                    descriptors.add(FoldingDescriptor(element.node, range, null, "TempAPI: $shortDescription"))
                }
            }
            true
        }
        return descriptors.toTypedArray()
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true
}