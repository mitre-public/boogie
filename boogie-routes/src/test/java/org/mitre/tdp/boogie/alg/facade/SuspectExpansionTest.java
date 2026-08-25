package org.mitre.tdp.boogie.alg.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.maps.MapBuilder;
import org.mitre.caasd.commons.maps.MapFeature;
import org.mitre.caasd.commons.maps.MapFeatures;
import org.mitre.tdp.boogie.*;

class SuspectExpansionTest {

  private static FluentRouteExpander expander;

  @BeforeAll
  static void setup() throws IOException {
//    try (InputStream inputStream = EmbeddedLidoFile.getInputStream()) {
//      var records = OneshotRecordParser.standard(ArincVersion.V22).assembleFrom(inputStream);
//      expander = FluentRouteExpander.inMemoryBuilder(
//          records.airports(),
//          records.procedures(),
//          records.airways(),
//          records.fixes()
//      ).build();
//    }
    expander = FluentRouteExpander.inMemoryBuilder(BznSuspectFixture.airports(), BznSuspectFixture.procedures(), BznSuspectFixture.airways(), BznSuspectFixture.fixes()).build();
  }

  @Test
  void testExpandRoute_KBZN_BZN6_V365_KHLN() {
    String route = "KBZN.BZN6.BZN.V365.CUSRI..KHLN";
    RouteDetails details = RouteDetails.builder().departureRunway("RW12").arrivalRunway("RW30").build();
    Optional<ExpandedRoute> result = expander.expand(route, details);
    ExpandedRoute expandedRoute = result.orElseThrow(() -> new AssertionError("Expansion produced nothing - the route graph is disconnected."));

    assertAll(
        () -> assertFalse(expandedRoute.legs().isEmpty(), "Expansion produced no legs."),
        () -> assertTrue(
            expandedRoute.legs().stream()
                .flatMap(leg -> leg.associatedFix().stream())
                .allMatch(fix -> fix.latLong().longitude() >= -115. && fix.latLong().longitude() <= -108.),
            "The empty common/enroute SID should avoid the same-named BZN6 and BZN fixes in England."
        ),
        () -> assertTrue(
            expandedRoute.legs().stream().anyMatch(leg -> "BZN6".equals(leg.section()) && SID.equals(leg.elementType())),
            "The no-leg common/enroute path must retain the inferred BZN6 runway transition."
        )
    );

    mapExpandedRoute(expandedRoute, "KBZN_BZN6_V365_KHLN.png");
  }

  /**
   * Maps an expanded route to a PNG file using CAASD commons map tooling.
   *
   * @param expandedRoute  the expanded route to visualize
   * @param outputFileName the name of the output file
   */
  private void mapExpandedRoute(ExpandedRoute expandedRoute, String outputFileName) {
    List<ExpandedRouteLeg> legs = expandedRoute.legs();
    if (legs.isEmpty()) {
      System.out.println("No legs to map");
      return;
    }

    List<MapFeature> features = new ArrayList<>();
    List<LatLong> routePath = new ArrayList<>();

    for (int i = 0; i < legs.size(); i++) {
      ExpandedRouteLeg leg = legs.get(i);
      Optional<? extends Fix> fix = leg.associatedFix();

      if (fix.isPresent()) {
        LatLong latLong = fix.get().latLong();
        String label = fix.get().fixIdentifier();
        System.out.printf("%s: %.4f, %.4f%n", label, latLong.latitude(), latLong.longitude());

        // Fixes well outside the Montana area are the erroneous ones - draw them loudly rather than hiding them
        boolean outOfRegion = latLong.longitude() < -130 || latLong.longitude() > -100;
        if (outOfRegion) {
          System.out.println("  ^ OUT OF REGION");
        }

        routePath.add(latLong);

        // Add a labeled point for each fix
        Color color = outOfRegion ? Color.RED : colorForSection(leg.section());
        features.add(MapFeatures.circle(latLong, color, outOfRegion ? 16 : 8, outOfRegion ? 4f : 2f));
        features.add(MapFeatures.text(label, latLong, Color.BLACK));
      }
    }

    // Add the route path as a line
    if (routePath.size() >= 2) {
      features.add(MapFeatures.path(routePath, Color.BLUE, 2f));
    }

    // Calculate center and extent of the route
    LatLong center = calculateCenter(routePath);
    Distance width = calculateWidth(routePath);

    // Build and save the map
    MapBuilder.newMapBuilder().mapBoxLightMode().width(width).center(center).addFeatures(features).toFile(new File(outputFileName));

    System.out.println("Map saved to: " + outputFileName);
    System.out.println("Route has " + legs.size() + " legs and " + routePath.size() + " mapped fixes");
  }

  /**
   * Midpoint of the bounding box rather than the mean of the points - averaging lets a dense cluster (the 16 Montana fixes) drag
   * the centre away from the outliers and push them off-frame.
   */
  private LatLong calculateCenter(List<LatLong> points) {
    if (points.isEmpty()) {
      return LatLong.of(45.0, -110.0); // Default center (Montana area)
    }
    double minLat = points.stream().mapToDouble(LatLong::latitude).min().orElse(45.0);
    double maxLat = points.stream().mapToDouble(LatLong::latitude).max().orElse(45.0);
    double minLon = points.stream().mapToDouble(LatLong::longitude).min().orElse(-110.0);
    double maxLon = points.stream().mapToDouble(LatLong::longitude).max().orElse(-110.0);
    return LatLong.of((minLat + maxLat) / 2, (minLon + maxLon) / 2);
  }

  private Distance calculateWidth(List<LatLong> points) {
    if (points.size() < 2) {
      return Distance.ofNauticalMiles(50);
    }
    double minLat = points.stream().mapToDouble(LatLong::latitude).min().orElse(0);
    double maxLat = points.stream().mapToDouble(LatLong::latitude).max().orElse(0);
    double minLon = points.stream().mapToDouble(LatLong::longitude).min().orElse(0);
    double maxLon = points.stream().mapToDouble(LatLong::longitude).max().orElse(0);

    LatLong sw = LatLong.of(minLat, minLon);
    LatLong ne = LatLong.of(maxLat, maxLon);
    Distance diagonal = sw.distanceTo(ne);

    // Add 20% padding and ensure minimum size
    double paddedNm = diagonal.inNauticalMiles() * 1.4;
    return Distance.ofNauticalMiles(Math.max(paddedNm, 30));
  }

  private Color colorForSection(String section) {
    if (section == null) {
      return Color.GRAY;
    }
    return switch (section) {
      case "KBZN", "KHLN" -> Color.RED;        // Airports
      case "BZN6" -> Color.GREEN;              // SID
      case "V365" -> Color.ORANGE;             // Airway
      default -> Color.MAGENTA;                // Direct/other
    };
  }
}
