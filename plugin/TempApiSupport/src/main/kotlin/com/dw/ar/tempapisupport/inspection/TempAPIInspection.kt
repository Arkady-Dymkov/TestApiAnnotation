package com.dw.ar.tempapisupport.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.toUElement

class TempAPIInspection : LocalInspectionTool() {

    override fun getDisplayName(): String {
        return "TempAPI usage inspection" // Replace with your desired display name
    }

    override fun getGroupDisplayName(): String {
        return "TempAPI" // Or a more descriptive name like "API Versioning"
    }

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ): PsiElementVisitor = object : PsiElementVisitor() {

        override fun visitElement(element: PsiElement) {
            val uElement = element.toUElement() ?: return
            if (uElement is UAnnotated) {
                val annotation = uElement.findAnnotation(TempAPI::class.java.canonicalName)
                if (annotation != null) {
                    val shortDescription = annotation.findAttributeValue("shortDescription")?.evaluate() as? String ?: "Temporary solution"
                    holder.registerProblem(
                        element,
                        "TempAPI: $shortDescription",
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                    )
                }
            }
        }
    }
}