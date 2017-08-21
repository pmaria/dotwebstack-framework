package org.dotwebstack.framework.config;

import java.io.IOException;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

public class FileConfigurationBackend implements ConfigurationBackend, ResourceLoaderAware {

  private static final Logger LOG = LoggerFactory.getLogger(FileConfigurationBackend.class);

  private SailRepository repository;

  private ResourceLoader resourceLoader;

  public FileConfigurationBackend(SailRepository repository) {
    this.repository = repository;
    repository.initialize();
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = Objects.requireNonNull(resourceLoader);
  }

  @Override
  public SailRepository getRepository() {
    return repository;
  }

  @PostConstruct
  public void loadResources() throws IOException {
    Resource[] resources =
        ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(
            "classpath:**/model/*");

    if (resources.length == 0) {
      LOG.info("No configuration files found");
      return;
    }

    RepositoryConnection repositoryConnection;

    try {
      repositoryConnection = repository.getConnection();
    } catch (RepositoryException e) {
      throw new ConfigurationException("Error while getting repository connection.", e);
    }

    try {
      for (Resource resource : resources) {
        String extension = FilenameUtils.getExtension(resource.getFilename());

        if (!FileFormats.containsExtension(extension)) {
          LOG.debug("File extension not supported, ignoring file: \"{}\"", resource.getFilename());
          continue;
        }

        repositoryConnection.add(resource.getInputStream(), "#", FileFormats.getFormat(extension));
        LOG.info("Loaded configuration file: \"{}\"", resource.getFilename());
      }
    } catch (RDF4JException e) {
      throw new ConfigurationException("Error while loading RDF data.", e);
    } finally {
      repositoryConnection.close();
    }
  }

}