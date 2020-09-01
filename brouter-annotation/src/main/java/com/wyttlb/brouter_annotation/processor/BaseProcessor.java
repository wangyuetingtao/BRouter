package com.wyttlb.brouter_annotation.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.wyttlb.brouter_annotation.utils.Consts.KEY_MODULE_NAME;

public abstract class BaseProcessor extends AbstractProcessor {
    Filer mFiler;
    Types types;
    Elements elmentsUtils;
    Messager messager ;
    String moduleName;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        elmentsUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();

        Map<String ,String> options = processingEnvironment.getOptions();
        if (options != null && options.size() > 0) {
            moduleName = options.get(KEY_MODULE_NAME);
        }

        if(moduleName != null && !moduleName.equals("")) {
            messager.printMessage(Diagnostic.Kind.NOTE, "module name =" + moduleName);
        } else {
            messager.printMessage(Diagnostic.Kind.ERROR, "module name not config");

            throw new RuntimeException("module name not config");
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return new HashSet<String>() { { this.add(KEY_MODULE_NAME);}};
    }
}
