package org.dotwebstack.framework.frontend.ld.writer.graph;

import javax.ws.rs.Produces;
import org.dotwebstack.framework.backend.ResultType;
import org.dotwebstack.framework.frontend.http.MediaTypes;
import org.dotwebstack.framework.frontend.ld.writer.EntityWriter;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.stereotype.Service;

@Service
@EntityWriter(resultType = ResultType.GRAPH)
@Produces(MediaTypes.TURTLE)
public class TurtleGraphEntityWriter extends GraphEntityWriter {

  TurtleGraphEntityWriter() {
    super(RDFFormat.TURTLE);
  }

}
