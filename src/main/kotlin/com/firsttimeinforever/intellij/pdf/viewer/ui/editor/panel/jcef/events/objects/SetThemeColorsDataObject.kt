package com.firsttimeinforever.intellij.pdf.viewer.ui.editor.panel.jcef.events.objects

import com.firsttimeinforever.intellij.pdf.viewer.ui.editor.panel.jcef.transformColorRgba
import kotlinx.serialization.Serializable
import java.awt.Color

@Serializable
class SetThemeColorsDataObject(val background: String, val foreground: String) {
    companion object {
        fun from(background: Color, foreground: Color) =
            SetThemeColorsDataObject(transformColorRgba(background), transformColorRgba(foreground))
    }
}
