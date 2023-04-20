package com.linkkids.annotationshortcut;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/**
 * @author qianqi
 * @date 2023/4/18 14:11
 * @description TODO
 */
public class ShortCutAction extends AnAction {

    private String shortCut = "str";

    @Override
    public void actionPerformed(AnActionEvent e) {
        String shortCut = Messages.showInputDialog("Shortcut", "Modify the Shortcut", Messages.getQuestionIcon());
        if(shortCut != null && !"".equals(shortCut.trim())){
            this.shortCut = shortCut;
        }
    }

    public String getShortCut() {
        return shortCut;
    }
}
