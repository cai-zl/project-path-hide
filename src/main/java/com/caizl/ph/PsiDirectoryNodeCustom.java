package com.caizl.ph;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.ProjectRootsUtil;
import com.intellij.ide.projectView.impl.nodes.ProjectViewDirectoryHelper;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtilRt;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

/**
 * @author cai zl
 * @since 2022/5/11 16:27
 */
public class PsiDirectoryNodeCustom extends PsiDirectoryNode {

    protected static final Logger LOG = Logger.getInstance(PsiDirectoryNodeCustom.class);

    public PsiDirectoryNodeCustom(Project project, PsiDirectory value, ViewSettings viewSettings) {
        super(project, value, viewSettings);
    }

    @Override
    protected void updateImpl(@NotNull PresentationData data) {
        Project project = this.getProject();
        assert project != null : this;
        PsiDirectory psiDirectory = this.getValue();
        assert psiDirectory != null : this;
        VirtualFile directoryFile = psiDirectory.getVirtualFile();
        Object parentValue = this.getParentValue();
        if (ProjectRootsUtil.isModuleContentRoot(directoryFile, project)) {
            ProjectFileIndex name = ProjectRootManager.getInstance(project).getFileIndex();
            Module module = name.getModuleForFile(directoryFile);
            data.setPresentableText(directoryFile.getName());
            if (module != null) {
                if (!(parentValue instanceof Module)) {
                    if (!this.shouldShowModuleName()) {
                        data.addText(directoryFile.getName() + " ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                    } else if (Comparing.equal(module.getName(), directoryFile.getName())) {
                        data.addText(directoryFile.getName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                    } else {
                        data.addText(directoryFile.getName() + " ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        data.addText("[" + module.getName() + "]", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                    }
                } else {
                    data.addText(directoryFile.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                }

                data.setLocationString(ProjectViewDirectoryHelper.getInstance(project).getLocationString(psiDirectory, false, this.shouldShowSourcesRoot()));
                this.setupIcon(data, psiDirectory);
                return;
            }
        }
        String name1 = parentValue instanceof Project ? psiDirectory.getVirtualFile().getPresentableUrl() : ProjectViewDirectoryHelper.getInstance(psiDirectory.getProject()).getNodeName(this.getSettings(), parentValue, psiDirectory);
        if (StringUtilRt.isEmpty(name1)) {
            this.setValue(null);
            LOG.debug("PsiDirectoryNodeCustom:{}", data.getLocationString());
        } else {
            data.setPresentableText(name1);
            data.setLocationString(ProjectViewDirectoryHelper.getInstance(project).getLocationString(psiDirectory, false, false));
            this.setupIcon(data, psiDirectory);
        }
    }
}
