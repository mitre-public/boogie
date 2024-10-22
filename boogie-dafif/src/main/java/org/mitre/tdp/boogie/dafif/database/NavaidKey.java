package org.mitre.tdp.boogie.dafif.database;

import java.util.Objects;

public class NavaidKey {
  private final String navaidIdentifier;
  private final int type;
  private final String country;
  private final int navaidKeyCode;

  public NavaidKey(String navaidIdentifier, int type, String country, int navaidKeyCode) {
    this.navaidIdentifier = navaidIdentifier;
    this.type = type;
    this.country = country;
    this.navaidKeyCode = navaidKeyCode;
  }

  public String navaidIdentifier() {
    return navaidIdentifier;
  }

  public int type() {
    return type;
  }

  public String country() {
    return country;
  }

  public int navaidKeyCode() {
    return navaidKeyCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    NavaidKey navaidKey = (NavaidKey) o;
    return type == navaidKey.type && navaidKeyCode == navaidKey.navaidKeyCode && Objects.equals(navaidIdentifier, navaidKey.navaidIdentifier) && Objects.equals(country, navaidKey.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(navaidIdentifier, type, country, navaidKeyCode);
  }

  @Override
  public String toString() {
    return "NavaidKey{" +
        "navaidIdentifier='" + navaidIdentifier + '\'' +
        ", type=" + type +
        ", country='" + country + '\'' +
        ", navaidKeyCode=" + navaidKeyCode +
        '}';
  }
}
