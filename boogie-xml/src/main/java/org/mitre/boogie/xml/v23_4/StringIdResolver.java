package org.mitre.boogie.xml.v23_4;

import java.util.concurrent.Callable;

import org.glassfish.jaxb.runtime.IDResolver;
import org.xml.sax.SAXException;

/**
 * Custom {@link IDResolver} that returns the raw ID string for IDREF fields instead of attempting
 * to resolve them to their target objects. This is useful in streaming unmarshalling where referenced
 * objects may not be in scope.
 */
final class StringIdResolver extends IDResolver {

  static final StringIdResolver INSTANCE = new StringIdResolver();

  private StringIdResolver() {
  }

  @Override
  public void bind(String id, Object obj) throws SAXException {
  }

  @Override
  public Callable<?> resolve(String id, Class targetType) throws SAXException {
    return () -> id;
  }
}
