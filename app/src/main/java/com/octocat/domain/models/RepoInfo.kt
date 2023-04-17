package com.octocat.domain.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class RepoInfo(
    val repoId: Long,
    val name: String,
    val description: String,
    val updatedAt: Date?,
    val starsCount: Int,
    val forksCount: Int
) : Parcelable