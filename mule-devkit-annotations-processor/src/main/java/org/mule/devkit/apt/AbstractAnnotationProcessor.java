package org.mule.devkit.apt;

import com.sun.codemodel.JCodeModel;
import org.mule.devkit.annotations.Module;
import org.mule.devkit.apt.generator.GenerationException;
import org.mule.devkit.apt.generator.Generator;
import org.mule.devkit.apt.generator.schema.FileTypeSchema;
import org.mule.devkit.apt.generator.schema.Schema;
import org.mule.devkit.apt.validation.TypeValidator;
import org.mule.devkit.apt.validation.ValidationException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class AbstractAnnotationProcessor extends AbstractProcessor {
    private AnnotationProcessorContext context;

    private void createContext() {
        context = new AnnotationProcessorContext();
        context.setCodeModel(new JCodeModel());
        context.setElements(processingEnv.getElementUtils());
        context.setCodeWriter(new FilerCodeWriter(processingEnv.getFiler()));
    }

    protected AnnotationProcessorContext getContext() {
        return context;
    }

    protected void note(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    protected void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

        createContext();

        note("AbstractAnnotationProcessor: annotations=" + annotations + ", roundEnv=" + env);

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = env.getElementsAnnotatedWith(annotation);
            Set<TypeElement> typeElements = ElementFilter.typesIn(elements);
            for (TypeElement e : typeElements) {

                try {
                    getValidator().validate(e);
                    preCodeGeneration(e);
                    generateCode(e);
                    postCodeGeneration(e);
                } catch (ValidationException tve) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, tve.getMessage(), tve.getElement());
                }
            }
        }

        try {
            FilerCodeWriter filerCodeWriter = new FilerCodeWriter(processingEnv.getFiler());
            context.getCodeModel().build(filerCodeWriter);
        } catch (IOException e) {
            error(e.getMessage());
        }

        if (getContext().getSchemas().size() > 0) {
            for (Module mod : getContext().getSchemas().keySet()) {
                FileTypeSchema fileTypeSchema = getContext().getSchemas().get(mod);
                try {
                    //File schemaFile = new File(generatedSources, "mule-" + name + ".xsd");
                    JAXBContext jaxbContext = JAXBContext.newInstance(Schema.class);
                    Marshaller marshaller = jaxbContext.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
                    marshaller.marshal(fileTypeSchema.getSchema(), fileTypeSchema.getOs());
                } catch (JAXBException e) {
                    error(e.getCause().getMessage());
                }
            }
        }

        return true;
    }

    public void generateCode(TypeElement e) {
        List<Generator> generators = getCodeGenerators();

        for (Generator generator : generators) {
            try {
                generator.generate(e);
            } catch (GenerationException cge) {
                error(cge.getMessage());
            }
        }
    }

    public abstract void preCodeGeneration(TypeElement e);

    public abstract void postCodeGeneration(TypeElement e);

    public abstract TypeValidator getValidator();

    public abstract List<Generator> getCodeGenerators();
}
