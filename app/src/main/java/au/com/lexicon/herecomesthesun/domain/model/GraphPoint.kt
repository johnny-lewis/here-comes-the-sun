package au.com.lexicon.herecomesthesun.domain.model

import au.com.lexicon.herecomesthesun.presentation.viewmodel.UVRatingGrades

data class GraphPoint(
    val time: Int,
    val value: Int,
    val grade: UVRatingGrades
)