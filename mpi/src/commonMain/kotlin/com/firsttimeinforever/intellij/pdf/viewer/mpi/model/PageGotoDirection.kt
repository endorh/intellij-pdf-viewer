package com.firsttimeinforever.intellij.pdf.viewer.mpi.model

import kotlinx.serialization.Serializable

@Serializable
enum class PageGotoDirection {
  FORWARD,
  BACKWARD
}