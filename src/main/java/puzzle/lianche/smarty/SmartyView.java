package puzzle.lianche.smarty;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.Engine;
import org.lilystudio.smarty4j.Template;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.AbstractTemplateView;

public class SmartyView extends AbstractTemplateView{
    private static String webRootPath;

    private static String webRootUrl;

    @Autowired
    private static Engine smartyEngine;

    private static boolean initialized = false;

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!initialized){
            init(request);
        }
        Context context = new Context();
        for (Entry<String, Object> entry : model.entrySet()){
            context.set(entry.getKey(), entry.getValue());
        }
        context.set("contextPath", webRootUrl);
        response.setCharacterEncoding(smartyEngine.getEncoding());
        Template template = smartyEngine.getTemplate(this.getUrl());
        template.merge(context, response.getOutputStream());
    }

    private synchronized void init(HttpServletRequest request) {
        extractConfigs(request);
        detectEngine();
        initialized = true;
    }

    private void detectEngine() throws NoSuchBeanDefinitionException{
        if (smartyEngine == null){
            smartyEngine = BeanFactoryUtils.beanOfTypeIncludingAncestors(getApplicationContext(), Engine.class, true, false);
            smartyEngine.setTemplatePath(webRootPath + smartyEngine.getTemplatePath());
        }
    }

    private void extractConfigs(HttpServletRequest request) {
        if (webRootPath == null){
            webRootPath = request.getSession().getServletContext().getRealPath("/");
        }
        if (webRootUrl == null){
            webRootUrl = request.getSession().getServletContext().getContextPath();
        }
    }
}
