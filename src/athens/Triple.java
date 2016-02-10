package athens;

import java.io.IOException;
import java.io.Writer;

/**
 * Reference implementation for the class "Web Search - Information Extraction"
 * at Telecom ParisTech, Paris, France in Spring 2011
 * 
 * @author Fabian M. Suchanek
 * 
 * This class holds a triple of subject, predcate and object
 */

public class Triple implements Comparable<Triple> {

  /** Subject*/
  public String subject;

  /** Predicate*/
  public String predicate;

  /** Object*/
  public String object;

  /** String representation*/
  protected String asString;

  /** Constructs a triple*/
  public Triple(String subject, String predicate, String object) {
    super();
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
    asString = "<" + subject + ", " + predicate + ", " + object + ">";
  }

  @Override
  public String toString() {
    return asString;
  }

  @Override
  public int compareTo(Triple arg0) {
    return arg0.asString.compareTo(asString);
  }

  @Override
  public boolean equals(Object arg0) {
    return arg0 instanceof Triple && ((Triple) arg0).asString.equals(asString);
  }

  @Override
  public int hashCode() {
    return asString.hashCode();
  }

  /** Writes the triple in TAB separated format to a writer*/
  public void writeTo(Writer w) throws IOException {
    w.write(subject);
    w.write("\t");
    w.write(predicate);
    w.write("\t");
    w.write(object);
    w.write("\n");
  }
}
