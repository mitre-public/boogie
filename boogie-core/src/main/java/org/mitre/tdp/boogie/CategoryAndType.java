package org.mitre.tdp.boogie;

import java.util.Objects;
import java.util.Optional;

public final class CategoryAndType {
  /**
   * The category this expansion is for
   */
  CategoryOrType category;
  /**
   * The type this expansion is for
   */
  CategoryOrType type;

  public CategoryAndType(CategoryOrType category, CategoryOrType type) {
    this.category = category;
    this.type = type;
  }

  public static final CategoryAndType NULL = new CategoryAndType(null, null);

  public Optional<CategoryOrType> category() {
    return Optional.ofNullable(category);
  }

  public Optional<CategoryOrType> type() {
    return Optional.ofNullable(type);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CategoryAndType that = (CategoryAndType) o;
    return category == that.category && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, type);
  }

  @Override
  public String toString() {
    return "CategoryAndType{" +
        "category=" + category +
        ", type=" + type +
        '}';
  }
}
