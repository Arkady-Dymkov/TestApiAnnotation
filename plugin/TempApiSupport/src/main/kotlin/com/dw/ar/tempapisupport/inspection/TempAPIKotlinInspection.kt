package com.dw.ar.tempapisupport.inspection

import com.intellij.analysis.AnalysisScope
import com.intellij.codeInspection.*
import com.intellij.codeInspection.reference.RefEntity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid


class TempAPIKotlinInspection: GlobalInspectionTool() {
    override fun getDisplayName(): String {
        return "TempAPI Usage Inspection"
    }

    override fun getGroupDisplayName(): String {
        return "TempAPI"
    }

    override fun runInspection(
        scope: AnalysisScope,
        manager: InspectionManager,
        globalContext: GlobalInspectionContext,
        problemDescriptionsProcessor: ProblemDescriptionsProcessor
    ) {
        scope.accept(object: PsiElementVisitor() {
            override fun visitFile(file: PsiElement) {
                if (file is KtFile) {
                    file.acceptChildren(object: KtTreeVisitorVoid() {
                        override fun visitElement(element: PsiElement) {
                            super.visitElement(element)
                            if (element is KtAnnotated) {
                                element.annotationEntries.forEach { annotationEntry ->
                                    if (annotationEntry.shortName?.asString() == "TempAPI") {
                                        // Create a RefEntity from the KtAnnotated element
                                        val refEntity =
                                        problemDescriptionsProcessor.addProblemElement(
                                            refEntity, // Use the RefEntity here
                                            manager.createProblemDescriptor(
                                                element,
                                                "TempAPI usage: This element is marked as temporary.",
                                                true, // isOnTheFly
                                                ProblemHighlightType.WARNING,
                                                true // onTheFly
                                            )
                                        )
                                        findUsages(element, manager, problemDescriptionsProcessor)
                                    }
                                }
                            }
                        }

                        private fun findUsages(
                            element: PsiElement,
                            manager: InspectionManager,
                            problemsHolder: ProblemDescriptionsProcessor
                        ) {
                            element.references.forEach { reference ->
                                val resolvedElement = reference.resolve()
                                if (resolvedElement!= null) {
                                    // Create a RefEntity for the usage reference

                                    val refEntity = RefEntity.create(reference.element)
                                    problemsHolder.addProblemElement(
                                        refEntity, // Use the RefEntity here
                                        manager.createProblemDescriptor(
                                            reference.element,
                                            "Usage of TempAPI element: This usage is temporary.",
                                            true, // isOnTheFly
                                            ProblemHighlightType.WARNING,
                                            true // onTheFly
                                        )
                                    )
                                }
                            }
                        }
                    })
                }
            }
        })
    }
}