package com.caizl.ph;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author cai zl
 * @since 2022/5/11 16:29
 */
public class CustomTreeStructureProvider implements TreeStructureProvider {

    @Override
    public @NotNull Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                           @NotNull Collection<AbstractTreeNode<?>> children,
                                                           ViewSettings settings) {
        List<AbstractTreeNode<?>> nodes = new ArrayList<AbstractTreeNode<?>>();
        for (AbstractTreeNode<?> child : children) {
            Project project = child.getProject();
            if (project != null) {
                Object value = child.getValue();
                if (value instanceof PsiDirectory) {
                    PsiDirectory directory = (PsiDirectory) value;
                    nodes.add(new PsiDirectoryNodeCustom(project, directory, settings));
                    continue;
                }
                nodes.add(child);
            }
        }
        return nodes;
    }
}
