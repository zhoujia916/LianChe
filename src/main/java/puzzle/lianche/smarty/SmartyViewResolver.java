package puzzle.lianche.smarty;


import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

public class SmartyViewResolver extends AbstractTemplateViewResolver{

    public SmartyViewResolver()
    {
        setViewClass(requiredViewClass());
    }

    @Override
    protected Class<SmartyView> requiredViewClass()
    {
        return SmartyView.class;
    }
}
