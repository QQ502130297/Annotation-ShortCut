// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.linkkids.annotationshortcut;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author qianqi
 */
public class AnnotationStringHintCompletionContributor extends CompletionContributor {

    public AnnotationStringHintCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.or(
                PlatformPatterns.psiElement().afterLeaf(".").withSuperParent(5, PsiAnnotation.class),
                PlatformPatterns.psiElement().afterLeaf(".").withSuperParent(4, PsiAnnotation.class)),
            new CompletionProvider<>() {
                @Override
                public void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
                    String shortCut = "str";
                    AnAction shortCutAction = ActionManager.getInstance().getAction("ShortCutAction");
                    if(shortCutAction instanceof ShortCutAction){
                        shortCut = ((ShortCutAction)shortCutAction).getShortCut();
                    }
                    resultSet.addElement(LookupElementBuilder.create(shortCut).withInsertHandler((insertionContext, item) -> {
                        String text = insertionContext.getDocument().getText();
                        PsiElement position = parameters.getPosition();
                        int startOffset = getStartOffset(text, insertionContext.getTailOffset());
                        String word = text.substring(startOffset, parameters.getOffset() - 1).trim();
                        if (word.length() != 0) {
                            word = "\"" + word + "\"";
                        }
                        insertionContext.getDocument().replaceString(
                                startOffset,
                                insertionContext.getTailOffset(),
                                word);
                    }));
                }
        });
    }

    private int getStartOffset(String text, int offset) {
        int start = offset - 1;
        for(int i = start; i >= 0; i--){
            char c = text.charAt(i);
            if(c == '.' || (c >= '0' && c <= '9') || c == '$' || c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
                continue;
            }
            return i + 1;
        }
        return 0;
    }
}
