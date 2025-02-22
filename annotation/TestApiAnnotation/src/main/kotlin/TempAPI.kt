package com.dw.ar


@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.TYPE,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FILE
)
public annotation class TempAPI(val shortDescription: String, val longDescription: String)