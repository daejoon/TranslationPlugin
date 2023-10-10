package cn.yiiguxing.plugin.translate.action

import cn.yiiguxing.plugin.translate.message
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.util.NlsActions
import java.util.function.Supplier

/**
 * TranslatorActionGroup
 */
class TranslationEngineActionGroup(
    name: Supplier<@NlsActions.ActionText String> = Supplier { message("action.TranslationEngineActionGroup.name") },
    popup: Boolean = true
) : DefaultActionGroup(name, popup) {

    init {
        val (availableActions, unavailableActions) = TranslationEngineAction.actionsGroupedByAvailability()
        addAll(availableActions)
        if (unavailableActions.isNotEmpty()) {
            addSeparator(message("action.TranslationEngineActionGroup.separator.inactivated"))
            addAll(unavailableActions)
        }

        addSeparator()
        add(SettingsAction(message("action.TranslationEngineActionGroup.manage.translators"), null))
    }

    override fun isDumbAware(): Boolean = true

    override fun actionPerformed(e: AnActionEvent) {
        val dataContext = e.dataContext
        val popup = createActionPopup(dataContext)
        PlatformDataKeys.CONTEXT_COMPONENT
            .getData(dataContext)
            ?.let { component -> popup.showUnderneathOf(component) }
            ?: popup.showInBestPositionFor(dataContext)
    }

    fun createActionPopup(
        context: DataContext,
        title: String? = message("translation.engines.popup.title"),
        disposeCallback: Runnable? = null
    ): ListPopup = JBPopupFactory
        .getInstance()
        .createActionGroupPopup(
            title,
            this,
            context,
            false,
            true,
            false,
            disposeCallback,
            10,
            TranslationEngineAction.PRESELECT_CONDITION
        )

}