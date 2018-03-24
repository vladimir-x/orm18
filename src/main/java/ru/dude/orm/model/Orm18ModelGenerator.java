package ru.dude.orm.model;

import ru.dude.orm.model.annotations.Entity;
import ru.dude.orm.model.annotations.Table;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Maven генератор файлов, для описания статичной модели сущностей для ORM
 *
 * @author dude.
 */
@SupportedAnnotationTypes({"ru.dude.orm.model.annotations.Table"})
public class Orm18ModelGenerator extends AbstractProcessor {

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        System.out.println("Orm18ModelGenerator: INIT");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {
            System.out.println("Orm18ModelGenerator: PROCESS");

            for (Element element : roundEnv.getRootElements()) {
                Entity entityAnnotate = element.getAnnotation(Entity.class);
                if (entityAnnotate != null) {

                    MetaEntity metaEntity = new MetaEntity(element);
                    System.out.println("Generating model for " + metaEntity.getModelClassName());

                    try {
                        FileObject fo = processingEnv.getFiler().createSourceFile(metaEntity.getGeneratedFilePath());
                        OutputStream os = fo.openOutputStream();

                        try (PrintWriter pw = new PrintWriter(os)) {

                            pw.println(metaEntity.generateCode());

                            pw.flush();
                            pw.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;

    }
}
