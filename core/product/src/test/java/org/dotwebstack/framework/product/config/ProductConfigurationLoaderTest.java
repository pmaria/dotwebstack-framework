package org.dotwebstack.framework.product.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.dotwebstack.framework.product.ProductRegistry;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.junit.Before;
import org.junit.Test;

public class ProductConfigurationLoaderTest {

  ProductConfigurationLoader productConfigurationLoader;

  ProductProperties productProperties;

  ProductRegistry productRegistry;

  @Before
  public void setUp() {
    productProperties = new ProductProperties();
    productRegistry = new ProductRegistry();
    productConfigurationLoader = new ProductConfigurationLoader(productProperties, productRegistry);
    productConfigurationLoader.setResourceLoader(null);
  }

  @Test
  public void testLoadEmptyConfiguration() throws IOException {
    productProperties.setConfigPath("empty");
    productConfigurationLoader.loadConfiguration();

    assertEquals(0, productRegistry.getNumberOfProducts());
  }

  @Test
  public void testLoadSingleConfigurationFile() throws IOException {
    productProperties.setConfigPath("single");
    productConfigurationLoader.loadConfiguration();

    IRI movies = SimpleValueFactory.getInstance().createIRI("http://moviedb.org/product#Movies");
    IRI actors = SimpleValueFactory.getInstance().createIRI("http://moviedb.org/product#Actors");

    assertEquals(2, productRegistry.getNumberOfProducts());
    assertEquals(movies, productRegistry.getProduct(movies).getIdentifier());
    assertEquals(actors, productRegistry.getProduct(actors).getIdentifier());
  }

  @Test(expected = IOException.class)
  public void testLoadNonExistingPath() throws IOException {
    productProperties.setConfigPath("non-existing");
    productConfigurationLoader.loadConfiguration();
  }

  @Test(expected = ProductConfigurationException.class)
  public void testLoadInvalidFormat() throws IOException {
    productProperties.setConfigPath("invalid");
    productConfigurationLoader.loadConfiguration();
  }

}
