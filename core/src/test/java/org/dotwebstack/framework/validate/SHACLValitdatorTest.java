package org.dotwebstack.framework.validate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SHACLValitdatorTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private final String shapesContent = "@prefix sh:\t\t\t<http://www.w3.org/ns/shacl#> .\n"
      + "@prefix kkg:\t\t<http://bp4mc2.org/def/kkg/id/begrip> .\n"
      + "@prefix uml:\t\t<http://www.omg.org/spec/UML/20131001/>.\n"
      + "@prefix rdfs:\t\t<http://www.w3.org/2000/01/rdf-schema#> .\n"
      + "@prefix owl:\t\t<http://www.w3.org/2002/07/owl#> .\n"
      + "@prefix ex:\t\t\t<http://example.org#> .\n"
      + "@prefix exs:\t\t<http://example.org/shape#> .\n"
      + "@prefix skos:\t\t<http://www.w3.org/2004/02/skos/core#>.\n"
      + "@prefix prov:\t\t<http://www.w3.org/ns/prov#>.\n"
      + "@prefix xsd:\t\t<http://www.w3.org/2001/XMLSchema#>.\n"
      + "@prefix dash:   <http://datashapes.org/dash#>.\n"
      + "\n"
      + "ex:Persoon a owl:Class;\n"
      + "\tuml:stereotype kkg:ObjectType;\n"
      + "\tskos:prefLabel \"Persoon\";\n"
      + "\tskos:definition \"Een mens van vlees en bloed\";\n"
      + "\tprov:generatedAtTime \"2017-08-11\"^^xsd:datetime;\n"
      + ".\n"
      + "\n"
      + "ex:Huwelijk a owl:Class;\n"
      + "\tuml:stereotype kkg:Relatieklasse;\n"
      + ".\n"
      + "\n"
      + "exs:Persoon a sh:NodeShape;\n"
      + "\tsh:targetClass ex:Persoon;\n"
      + "\tsh:property [\n"
      + "\t\tsh:name \"Naam\";\n"
      + "\t\tsh:path ex:naam;\n"
      + "\t\tsh:minCount 1;\n"
      + "\t\tsh:maxCount 1;\n"
      + "\t]\n"
      + ".\n"
      + "\n"
      + "exs:Huwelijk a sh:NodeShape;\n"
      + "\tsh:targetClass ex:Huwelijk;\n"
      + "\tsh:property [\n"
      + "\t\tsh:naam \"Lid\";\n"
      + "\t\tsh:path ex:lid;\n"
      + "\t\tsh:node exs:Persoon;\n"
      + "\t\tsh:minCount 2;\n"
      + "\t\tsh:maxCount 2;\n"
      + "\t]\n"
      + ".";

  private SHACLValidator shaclValidator;

  @Mock
  private Resource dataResource;

  @Mock
  private Resource shapesResource;

  @Before
  public void setUp() throws Exception {
    shaclValidator = new SHACLValidator();
    dataResource = mock(Resource.class);
    shapesResource = mock(Resource.class);
    final File dummyFile = mock(File.class);
    final String dummyFilePath = "/this/is/sparta.dummy";

    when(shapesResource.getInputStream()).thenReturn(new ByteArrayInputStream(
        shapesContent.getBytes(StandardCharsets.UTF_8)));
    when(dataResource.getFile()).thenReturn(dummyFile);
    when(dataResource.getFile().getAbsolutePath()).thenReturn(dummyFilePath);
    when(shapesResource.getFile()).thenReturn(dummyFile);
    when(shapesResource.getFile().getAbsolutePath()).thenReturn(dummyFilePath);
  }

  @Test
  public void validate_NoError_validConfiguration() throws Exception {
    // Arrange
    String dataContent = "@prefix sh: <http://www.w3.org/ns/shacl#> .\n"
        + "@prefix kkg: <http://bp4mc2.org/def/kkg/id/begrip> .\n"
        + "@prefix uml: <http://bp4mc2.org/def/uml#> .\n"
        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n"
        + "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n"
        + "@prefix ex: <http://example.org#> .\n"
        + "\n"
        + "ex:Marco a ex:Persoon;\n"
        + "\tex:naam \"Marco\";\n"
        + ".\n"
        + "\n"
        + "ex:Nanda a ex:Persoon;\n"
        + " \tex:naam \"Nanda\";\n"
        + " .\n"
        + "\n"
        + "ex:HuwelijkMarcoNanda a ex:Huwelijk;\n"
        + "\tex:lid ex:Marco;\n"
        + "\tex:lid ex:Nanda;\n"
        + ".";

    when(dataResource.getInputStream()).thenReturn(new ByteArrayInputStream(
        dataContent.getBytes(StandardCharsets.UTF_8)));

    // Act / Assert
    shaclValidator.validate(dataResource, shapesResource);
  }

  @Test
  public void validate_throwSHACLValidationException_invalidConfiguration() throws Exception {
    // Arrange
    String dataContent = "@prefix sh: <http://www.w3.org/ns/shacl#> .\n"
        + "@prefix kkg: <http://bp4mc2.org/def/kkg/id/begrip> .\n"
        + "@prefix uml: <http://bp4mc2.org/def/uml#> .\n"
        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n"
        + "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n"
        + "@prefix ex: <http://example.org#> .\n"
        + "\n"
        + "ex:Marco a ex:Persoon;\n"
        + "\tex:naam \"Marco\";\n"
        + ".\n"
        + "\n"
        + "ex:Nanda a ex:Persoon;\n"
        + " \tex:naam \"Nanda\";\n"
        + " .\n"
        + "\n"
        + " ex:Bobby a ex:Persoon;\n"
        + " \tex:naam \"Bobby\";\n"
        + " .\n"
        + "\n"
        + "ex:HuwelijkMarcoNanda a ex:Huwelijk;\n"
        + "\tex:lid ex:Marco;\n"
        + "\tex:lid ex:Nanda;\n"
        + "\tex:lid ex:Bobby;\n"
        + ".";

    when(dataResource.getInputStream()).thenReturn(new ByteArrayInputStream(
        dataContent.getBytes(StandardCharsets.UTF_8)));

    // Assert
    thrown.expect(SHACLValdiationException.class);

    // Act
    shaclValidator.validate(dataResource, shapesResource);
  }

  @Test
  public void validate_throwSHACLValidationException_WithIOException() throws Exception {
    // Arrange
    when(dataResource.getFile()).thenThrow(IOException.class);

    // Assert
    thrown.expect(SHACLValdiationException.class);
    thrown.expectMessage("File could not read during the validation process");

    // Act
    shaclValidator.validate(dataResource, shapesResource);
  }

}
