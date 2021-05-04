package com.firsttimeinforever.intellij.pdf.viewer.ui.editor

import com.firsttimeinforever.intellij.pdf.viewer.settings.PdfViewerSettings
import com.firsttimeinforever.intellij.pdf.viewer.settings.PdfViewerSettingsListener
import com.firsttimeinforever.intellij.pdf.viewer.ui.editor.view.PdfEditorViewComponent
import com.firsttimeinforever.intellij.pdf.viewer.ui.editor.view.PdfViewStateChangedListener
import com.intellij.diff.util.FileEditorBase
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import javax.swing.JComponent

class PdfFileEditor(project: Project, private val virtualFile: VirtualFile) : FileEditorBase(), DumbAware {
  val viewComponent = PdfEditorViewComponent(project, virtualFile)
  private val messageBusConnection = project.messageBus.connect()
  private val fileChangedListener = FileChangedListener()

  init {
    Disposer.register(this, viewComponent)
    Disposer.register(this, messageBusConnection)
    messageBusConnection.subscribe(VirtualFileManager.VFS_CHANGES, fileChangedListener)
  }

  override fun getName(): String = NAME

  override fun getComponent(): JComponent = viewComponent

  override fun getPreferredFocusedComponent(): JComponent = viewComponent.controlPanel

  private inner class FileChangedListener(var isEnabled: Boolean = false) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
      if (!PdfViewerSettings.instance.enableDocumentAutoReload) {
        return
      }
      if (viewComponent.controller == null) {
        logger.warn("FileChangedListener was called for view with controller == null!")
      } else if (isEnabled && events.any { it == virtualFile }) {
        logger.debug("Target file ${virtualFile.path} changed. Reloading current view.")
        viewComponent.controller.reload(tryToPreserveState = true)
      }
    }
  }

  companion object {
    private const val NAME = "Pdf Viewer File Editor"
    private val logger = logger<PdfFileEditor>()
  }
}
