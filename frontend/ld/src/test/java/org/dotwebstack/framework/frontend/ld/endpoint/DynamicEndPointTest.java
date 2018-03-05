package org.dotwebstack.framework.frontend.ld.endpoint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.dotwebstack.framework.frontend.http.stage.Stage;
import org.dotwebstack.framework.frontend.ld.parameter.ParameterMapper;
import org.dotwebstack.framework.test.DBEERPEDIA;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DynamicEndPointTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private final String pathPattern = "pathPattern";

  private final String label = "label";

  @Mock
  private ParameterMapper parameterMapper;

  @Mock
  private Stage stage;

  @Test
  public void build_CreateDynamicEndPoint_WithValidData() {
    // Assert
    DynamicEndPoint dynamicEndPoint =
        new DynamicEndPoint.DynamicEndpointBuilder(DBEERPEDIA.DOC_ENDPOINT, pathPattern,
            parameterMapper).build();

    // Act
    assertThat(dynamicEndPoint.getIdentifier(), equalTo(DBEERPEDIA.DOC_ENDPOINT));
    assertThat(dynamicEndPoint.getPathPattern(), equalTo(pathPattern));
    assertThat(dynamicEndPoint.getParameterMapper(), equalTo(parameterMapper));
  }

  @Test
  public void build_ThrowsException_WithMissingIdentifier() {
    // Assert
    thrown.expect(NullPointerException.class);

    // Act
    new DynamicEndPoint.DynamicEndpointBuilder(null, pathPattern, parameterMapper).build();
  }

  @Test
  public void build_ThrowsException_WithMissingPathPattern() {
    // Assert
    thrown.expect(NullPointerException.class);

    // Act
    new DynamicEndPoint.DynamicEndpointBuilder(DBEERPEDIA.DOC_ENDPOINT, null,
        parameterMapper).build();
  }

  @Test
  public void build_ThrowsException_WithMissingParameterMapper() {
    // Assert
    thrown.expect(NullPointerException.class);

    // Act
    new DynamicEndPoint.DynamicEndpointBuilder(DBEERPEDIA.DOC_ENDPOINT, pathPattern, null).build();
  }

  @Test
  public void build_CreateDynamicEndPointComplete_WithValidData() {
    // Assert
    DynamicEndPoint dynamicEndPoint =
        (DynamicEndPoint) new DynamicEndPoint.DynamicEndpointBuilder(DBEERPEDIA.DOC_ENDPOINT,
            pathPattern, parameterMapper).label(label).stage(stage).build();

    // Act
    assertThat(dynamicEndPoint.getIdentifier(), equalTo(DBEERPEDIA.DOC_ENDPOINT));
    assertThat(dynamicEndPoint.getPathPattern(), equalTo(pathPattern));
    assertThat(dynamicEndPoint.getParameterMapper(), equalTo(parameterMapper));
    assertThat(dynamicEndPoint.getLabel(), equalTo(label));
    assertThat(dynamicEndPoint.getStage(), equalTo(stage));
  }
}