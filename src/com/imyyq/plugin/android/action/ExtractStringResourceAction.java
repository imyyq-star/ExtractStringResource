package com.imyyq.plugin.android.action;

import com.imyyq.plugin.android.ExtractStringResourceConfigure;
import com.imyyq.plugin.android.entity.Selection;
import com.imyyq.plugin.android.entity.TransResult;
import com.imyyq.plugin.android.entity.WriteObj;
import com.imyyq.plugin.android.util.TextUtils;
import com.imyyq.plugin.android.util.TransApi;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.command.undo.UndoUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;

import org.jetbrains.annotations.NotNull;

/**
 * Created by yyq on 18/4/5.
 */
public class ExtractStringResourceAction
        extends AnAction
{
    private TransApi mTransApi = new TransApi();

    private Selection mSelection;

    @Override
    public void actionPerformed(AnActionEvent e)
    {
        // 拿到当前操作的编辑，项目，文件
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        if (psiFile == null)
        {
            Messages.showErrorDialog(project, "发生意外错误", "错误");
            return;
        }

        // 获取前缀，比如 getApplication()，如果为空则需要填入
        String prefix = PropertiesComponent.getInstance().getValue(
                ExtractStringResourceConfigure.KEY_PREFIX, "");
        String appID = PropertiesComponent.getInstance().getValue(
                ExtractStringResourceConfigure.KEY_APP_ID, "");
        String securityKey = PropertiesComponent.getInstance().getValue(
                ExtractStringResourceConfigure.KEY_SECURITY_KEY, "");
        if (TextUtils.isEmpty(prefix) || TextUtils.isEmpty(appID) || TextUtils.isEmpty(securityKey))
        {
            Messages.showMessageDialog(
                    "请打开：File -> Settings -> Other Settings -> Auto extract string resource\r\n" + "设置前缀，百度翻译的AppID和密钥",
                    "提示", null);
            return;
        }

        // 当前操作的必须是 java 文件或者是 xml 文件
        if (!(psiFile instanceof PsiJavaFile) && !(psiFile instanceof XmlFile))
        {
            return;
        }

        final Document document = editor.getDocument();

        // 取得双引号内的文本
        if (mSelection == null)
        {
            mSelection = TextUtils.getSelectionByCaret(editor, psiFile);
        }

        if (mSelection == null || TextUtils.isEmpty(mSelection.getText()))
        {
            Messages.showErrorDialog(project, "无法取得可抽取的文本", "错误");
            return;
        }

        // 翻译
        TransResult transResult = TransApi.getTransResult(appID, securityKey, mSelection.getText(),
                "zh", "en");

        if (transResult == null)
        {
            Messages.showErrorDialog(project, "翻译出错，请检查 AppID 和 密钥", "错误");
            return;
        }
        else if (transResult.getTrans_result() == null || transResult.getTrans_result().isEmpty() || TextUtils.isEmpty(
                transResult.getTrans_result().get(0).getDst()))
        {
            Messages.showErrorDialog(project,
                    "翻译出错，请检查 AppID 和 密钥。\\r\\n错误信息：" + transResult.getError_msg() + "\r\n" + "错误码：" + transResult.getError_code(),
                    "错误");
            return;
        }

        // 生成目标
        WriteObj writeObj = new WriteObj(appID, securityKey, psiFile, prefix, mSelection.getText(),
                transResult.getTrans_result().get(0).getDst().toLowerCase().trim().replace(
                        ' ', '_').replaceAll("[^A-Za-z0-9]+", "_"));

        // 目标 xml 文件
        String fileName = "strings.xml";
        PsiFile[] files = FilenameIndex.getFilesByName(project, fileName,
                GlobalSearchScope.moduleScope(ModuleUtil.findModuleForPsiElement(psiFile)));
        if (files == null || files.length == 0)
        {
            Messages.showErrorDialog(project, "在项目中无法找到文件 " + fileName, "错误");
            return;
        }

        PsiFile valueFile = null;

        for (PsiFile file : files)
        {
            if (file.getParent().getName().equals("values"))
            {
                valueFile = file;
                break;
            }
        }

        if (valueFile == null)
        {
            return;
        }

        final PsiFile file = valueFile;

        boolean isValueInXML = false;
        // 查看 name 和 Value 是否在 xml 中了
        if (file instanceof XmlFile)
        {
            String name = TextUtils.getNameInXmlByValue(((XmlFile) file).getRootTag(),
                    writeObj.getValue());
            if (name != null)
            { // 值已经在 xml 中了，直接取得 name 即可
                writeObj.setTranslationName(name);
                isValueInXML = true;
            }
            // 否则 name 重复了，只是提示
            else if (TextUtils.isNameInXml(((XmlFile) file).getRootTag(),
                    writeObj.getTranslationName()))
            {
                Messages.showInfoMessage(project,
                        "name = " + writeObj.getTranslationName() + " 已经在 strings.xml 中存在了，但是值不一样，请手动处理。",
                        "提示");
            }
        }

        // 写入到 xml 中
        boolean finalIsValueInXML = isValueInXML;
        WriteCommandAction action = new WriteCommandAction(project, "Auto Add Resource", file)
        {
            @Override
            protected void run(
                    @NotNull
                            Result result) throws Throwable
            {

                if (file instanceof XmlFile)
                {

                    // 值不在 xml 中，才需要写入
                    if (!finalIsValueInXML)
                    {
                        XmlTag tagFromText = XmlElementFactory.getInstance(
                                project).createTagFromText(writeObj.getWriteToStringsXMLStr());

                        XmlTag rootTag = ((XmlFile) file).getRootTag();
                        if (rootTag == null)
                        {
                            return;
                        }
                        PsiElement whiteSpace = PsiTreeUtil.getChildOfType(
                                XmlElementFactory.getInstance(project).createTagFromText(
                                        "<imyyq>\r\n</imyyq>"), XmlText.class);

                        rootTag.add(whiteSpace);
                        rootTag.add(tagFromText);
                    }

                    document.replaceString(mSelection.getStart(), mSelection.getEnd(),
                            writeObj.getReplaceWriteString());

                    UndoUtil.markPsiFileForUndo(psiFile);
                    UndoUtil.markPsiFileForUndo(file);
                }
            }
        };
        action.execute();

        editor.getSelectionModel().removeSelection();
    }


    // 当右键时，此方法触发，更新 action 是显示还是隐藏
    @Override
    public void update(AnActionEvent e)
    {

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        // 不是 java 或 xml，不显示action
        if (!(psiFile instanceof PsiJavaFile) && !(psiFile instanceof XmlFile))
        {
            e.getPresentation().setVisible(false);
            return;
        }

        if (editor == null || project == null || psiFile == null)
        {
            e.getPresentation().setVisible(false);
            return;
        }

        mSelection = TextUtils.getSelectionByCaret(editor, psiFile);

        // 是否是空字符串
        if (mSelection == null || TextUtils.isEmpty(mSelection.getText()))
        {
            e.getPresentation().setVisible(false);
            return;
        }

        e.getPresentation().setVisible(true);
    }

}
