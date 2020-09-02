package com.wyttlb.brouter_annotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.wyttlb.brouter_annotation.annotation.Route;
import com.wyttlb.brouter_annotation.utils.Consts;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RouteProcessor extends BaseProcessor {

    /**
     * 指定解析哪个注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(Route.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (moduleName != null && !moduleName.isEmpty()) {
            //获取所有注解

            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Route.class);
            if (elements.size() <= 0) {
                return false;
            } else {
                Map<String, TypeElement> routeMap = new HashMap<>();
                Iterator iterator = elements.iterator();
                while (iterator.hasNext()) {
                    Element route = (Element) iterator.next();
                    if (route instanceof TypeElement)
                        //获取注解element所在的类Element
                        routeMap.put(route.getAnnotation(Route.class).path(), (TypeElement) route);
                }
                messager.printMessage(Diagnostic.Kind.WARNING, routeMap.toString());
                genJavaFile(routeMap);
                return true;
            }
        }
        return false;
    }

    private void genJavaFile(Map<String, TypeElement> routeMap) {
        //  private static final HashMap<String, Class<?>> ROUTE_MAP = new HashMap<>();
        ParameterizedTypeName mapType = ParameterizedTypeName.get(ClassName.get("java.util", "HashMap"),
                ClassName.get("java.lang", "String"),
                ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)));

        FieldSpec mapField = FieldSpec.builder(mapType, "ROUTE_MAP", new Modifier[]{Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL}).initializer("new HashMap<>()", new Object[0]).build();
        CodeBlock.Builder staticBlock = CodeBlock.builder();
        Iterator iterator = routeMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, TypeElement> entry = (Map.Entry) iterator.next();
            staticBlock.addStatement("ROUTE_MAP.put($S, $T.class)",
                    new Object[]{entry.getKey(), ClassName.get((TypeElement) entry.getValue())});

        }

        TypeSpec clazz = TypeSpec.classBuilder(Consts.GENERATE_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addField(mapField)
                .addStaticBlock(staticBlock.build()).addMethod(MethodSpec.methodBuilder(Consts.METHOD_NAME)
                        .addModifiers(new Modifier[]{Modifier.PUBLIC, Modifier.STATIC})
                        .returns(mapType).addCode("return ROUTE_MAP;\n", new Object[0]).build()).build();


        try {
            JavaFile.builder(Consts.FACADE_PACKAGE, clazz).build().writeTo(mFiler);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
