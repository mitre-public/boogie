package org.mitre.boogie.cli;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A single XML element with names stripped of namespace prefixes.
 */
record XmlElement(String name, Map<String, String> attributes, List<XmlElement> children, String text) {

  XmlElement {
    attributes = Map.copyOf(attributes);
    children = List.copyOf(children);
  }

  Optional<XmlElement> child(String childName) {
    return children.stream().filter(child -> child.name().equals(childName)).findFirst();
  }

  List<XmlElement> children(String childName) {
    return children.stream().filter(child -> child.name().equals(childName)).toList();
  }

  /**
   * Returns the trimmed text of a descendant reached through direct child names.
   */
  Optional<String> value(String path) {
    Optional<XmlElement> current = Optional.of(this);
    for (String part : path.split("/")) {
      current = current.flatMap(element -> element.child(part));
    }

    return current.map(XmlElement::text)
        .map(String::strip)
        .filter(value -> !value.isEmpty());
  }
}
