/*
 * Copyright 2013 Xebia and Séven Le Mesle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package fr.xebia.extras.selma.codegen;

import com.squareup.javawriter.JavaWriter;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.xebia.extras.selma.codegen.MappingSourceNode.*;

/**
 *
 */
public class MapperMethodGenerator {


    private static final Pattern STANDARD_JAVA_PACKAGE = Pattern.compile("^(java|javax)\\.+$");
    private final JavaWriter writer;
    private final MethodWrapper mapperMethod;
    private final MapperGeneratorContext context;
    private final SourceConfiguration configuration;
    private final MapsWrapper maps;

    public MapperMethodGenerator(JavaWriter writer, MethodWrapper method, MapperWrapper mapperWrapper) {
        this.writer = writer;
        this.mapperMethod = method;
        this.context = mapperWrapper.context();
        this.configuration = mapperWrapper.configuration();

        this.maps = new MapsWrapper(method, mapperWrapper);
    }

    public void build() throws IOException {

        buildMappingMethod(writer, mapperMethod.inOutType(), mapperMethod.getSimpleName(), true);

        // Do we need to build other methods for this mapping ?
        if (context.hasMappingMethods()) {
            buildMappingMethods(writer);
        }

        // Report unused custom fields mapping
        maps.reportUnused();
    }

    private void buildMappingMethods(JavaWriter writer) throws IOException {

        MapperGeneratorContext.MappingMethod method;
        while ((method = context.popMappingMethod()) != null) {
            buildMappingMethod(writer, method.inOutType(), method.name(), false);
        }

    }

    private void buildMappingMethod(JavaWriter writer, InOutType inOutType, String name, boolean override) throws IOException {

        boolean isPrimitiveOrImmutable = false;
        final MappingSourceNode methodRoot;
        if (configuration.isFinalMappers()) {
            methodRoot = mapMethod(inOutType, name, override);
        } else {
            methodRoot = mapMethodNotFinal(inOutType, name, override);
        }

        MappingSourceNode ptr = blank(), blankRoot = ptr;

        MappingSourceNode methodNode = (inOutType.isOutPutAsParam() ? methodRoot.body(blank()) : methodRoot.body(declareOut(inOutType.out())));

        MappingBuilder mappingBuilder = findBuilderFor(inOutType);

        if (mappingBuilder != null) {

            ptr.body(mappingBuilder.build(context, new SourceNodeVars().withInOutType(inOutType).withAssign(true)));
            generateStack(context);
            // set isPrimitiveOrImmutable so we can skip the null check
            isPrimitiveOrImmutable = !inOutType.differs() && mappingBuilder.isNullSafe();

        } else if (inOutType.areDeclared() && isSupported(inOutType.out())) {

            ptr = ptr.body(instantiateOut(inOutType, context.newParams()));
            context.depth++;
            ptr.child(generate(inOutType));
            context.depth--;
        } else {
            handleNotSupported(inOutType, ptr);
        }

        isPrimitiveOrImmutable = isPrimitiveOrImmutable || inOutType.inIsPrimitive();
        if (!isPrimitiveOrImmutable) {
            methodNode = methodNode.child(controlNull("in", inOutType.isOutPutAsParam()));
            methodNode.body(blankRoot.body);
        } else {
            methodNode.child(blankRoot.body);
        }

        // Call the interceptor if it exist
        MappingBuilder interceptor = maps.mappingInterceptor(inOutType);
        if (interceptor != null) {
            methodNode.child(interceptor.build(context, new SourceNodeVars()));
        }

        // Give it a try
        methodRoot.write(writer);
    }

    /**
     * Method called when encountered a bean not known in registry so considered as
     * custom bean to map.
     *
     * @param out TypeMirror that will be checked
     * @return boolean telling whether this type can be supported by mapping code generation
     */
    private boolean isSupported(TypeMirror out) {
        boolean res = false;
        Matcher matcher = STANDARD_JAVA_PACKAGE.matcher(out.toString());
        if (!matcher.matches()) {
            TypeElement outTypeElement = (TypeElement) context.type.asElement(out);
            BeanWrapper outBean = new BeanWrapper(context, outTypeElement);
            res = outBean.hasCallableConstructor();
        }

        return res;
    }

    private void handleNotSupported(InOutType inOutType, MappingSourceNode ptr) {
        final String message = String.format("Failed to generate mapping method for type %s to %s not supported on %s.%s !\n" +
                "--> Add a custom mapper or @IgnoreFields to fix this ! If you think this a Bug in Selma please report issue here [https://github.com/xebia-france/selma/issues].", inOutType.in(), inOutType.out(), mapperMethod.element().getEnclosingElement(), mapperMethod.element().toString());
        ptr.body(notSupported(message));
        if (configuration.isIgnoreNotSupported()) {
            context.warn(message, mapperMethod.element());
        } else {
            context.error(mapperMethod.element(), message);
        }
    }

    private MappingSourceNode generateStack(MapperGeneratorContext context) throws IOException {

        MappingSourceNode root = blank();
        MapperGeneratorContext.StackElem stackElem;
        while ((stackElem = context.popStack()) != null) {
            InOutType inOutType = stackElem.sourceNodeVars().inOutType;
            context.depth++;
            MappingBuilder mappingBuilder = findBuilderFor(inOutType);
            if (stackElem.child) {

                lastChild(stackElem.lastNode).child(mappingBuilder.build(context, stackElem.sourceNodeVars()));
            } else {
                stackElem.lastNode.body(mappingBuilder.build(context, stackElem.sourceNodeVars()));
            }
            context.depth--;
        }

        return root;
    }

    MappingBuilder findBuilderFor(InOutType inOutType) {
        return maps.findMappingFor(inOutType);
    }

    private MappingSourceNode generate(InOutType inOutType) throws IOException {

        MappingSourceNode root = blank();
        MappingSourceNode ptr = root;


        TypeElement outTypeElement = (TypeElement) context.type.asElement(inOutType.out());


        BeanWrapper outBean = new BeanWrapper(context, outTypeElement);
        BeanWrapper inBean = new BeanWrapper(context, (TypeElement) context.type.asElement(inOutType.in()));

        Set<String> outFields = outBean.getSetterFields();
        for (String field : inBean.getGetterFields()) {

            String outField = maps.getFieldFor(field, inOutType.inAsDeclaredType());

            boolean isMissingInDestination = !outBean.hasFieldAndSetter(outField);

            if (maps.isIgnoredField(field, inOutType.inAsDeclaredType())) {
                continue; // Skip ignored in field
            }

            if (isMissingInDestination && maps.isIgnoreMissingProperties()) {
                continue;  // Skip ignored missing properties in destination bean
            } else {


                if (isMissingInDestination) {
                    context.error(inBean.getFieldElement(field), String.format("setter for field %s from source bean %s is missing in destination bean %s using field %s !\n" +
                            " --> Add @IgnoreFields(\"%s.%s\") to mapper interface / method or add missing getter", field, inOutType.in(), inOutType.out(), outField, inOutType.in(), field));
                    continue;
                }

                try {
                    MappingBuilder mappingBuilder = findBuilderFor(new InOutType(inBean.getTypeFor(field), outBean.getTypeFor(outField), inOutType.isOutPutAsParam()));
                    if (mappingBuilder != null) {
                        ptr = ptr.child(mappingBuilder.build(context, new SourceNodeVars(field, outField, inBean, outBean)
                                .withInOutType(new InOutType(inBean.getTypeFor(field), outBean.getTypeFor(outField), inOutType.isOutPutAsParam())).withAssign(false)));

                        generateStack(context);
                    } else {
                        handleNotSupported(inOutType, ptr);
                    }
                } catch (Exception e) {
                    System.out.printf("Error while searching builder for field %s on %s mapper", field, inOutType.toString());
                    e.printStackTrace();
                }


                ptr = lastChild(ptr);
            }
            outFields.remove(outField);
        }

        if (!maps.isIgnoreMissingProperties()) { // Report destination bean fields not mapped
            for (String outField : outFields) {

                if (!maps.isIgnoredField(outField, inOutType.outAsDeclaredType())) {
                    context.error(outBean.getSetterElement(outField), "setter for field %s from destination bean %s has no getter in source bean %s !\n" +
                            "-->  Add @IgnoreFields(\"%s.%s\") to mapper interface / method or add missing setter", outField, inOutType.out(), inOutType.in(), inOutType.out(), outField);
                }
            }
        }
        return root.child;
    }

    private MappingSourceNode lastChild(MappingSourceNode ptr) {
        while (ptr.child != null) {
            ptr = ptr.child;
        }
        return ptr;
    }

    public MapsWrapper maps() {
        return maps;
    }
}
