package com.dw.ar.tempapisupport.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.caches.resolve.resolveToDescriptorIfAny
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtSimpleNameExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.annotationClass
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.source.getPsi
import org.jetbrains.kotlin.utils.IDEAPluginsCompatibilityAPI


class TempAPIKotlinInspection : AbstractKotlinInspection() {

    @OptIn(IDEAPluginsCompatibilityAPI::class)
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : KtVisitorVoid() {
            // Check classes, functions, properties that have @TempAPI
            override fun visitDeclaration(declaration: KtDeclaration) {
                super.visitDeclaration(declaration)

                val descriptor = declaration.resolveToDescriptorIfAny()

                val (hasAnnotation, annotation) = descriptor.tempApiAnnotation()
                if (hasAnnotation && annotation != null) {
                    val annotationPsi = annotation.source.getPsi() ?: return
                    holder.registerProblem(annotationPsi, "This API is marked as temporary")
                }
            }

            // Check constructor calls: A()
            override fun visitCallExpression(expression: KtCallExpression) {
                super.visitCallExpression(expression)

                val bindingContext = expression.analyze()
                val resolvedCall = expression.getResolvedCall(bindingContext)
                val resultingDescriptor = resolvedCall?.resultingDescriptor

                val (hasAnnotation, annotation) = resultingDescriptor.tempApiAnnotation()
                if (hasAnnotation && annotation != null) {
                    holder.registerProblem(expression, "This API is marked as temporary")
                }
            }

            // Check references like variable usage
            override fun visitSimpleNameExpression(expression: KtSimpleNameExpression) {
                super.visitSimpleNameExpression(expression)

                val bindingContext = expression.analyze()
                val descriptor = bindingContext[BindingContext.REFERENCE_TARGET, expression]

                val typeDescriptor = descriptor?.containingDeclaration

                val (hasAnnotation, annotation) = typeDescriptor.tempApiAnnotation()
                if (hasAnnotation && annotation != null) {
                    holder.registerProblem(expression, "This API is marked as temporary")
                }
            }
        }
    }


    private fun DeclarationDescriptor?.tempApiAnnotation(): Pair<Boolean, AnnotationDescriptor?> {
        val annotation = this?.annotations?.firstOrNull {
            it.annotationClass?.fqNameSafe?.asString()?.endsWith("TempAPI") == true
        }

        return (annotation != null) to annotation
    }


}