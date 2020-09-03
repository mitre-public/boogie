package org.mitre.tdp.boogie.conformance.alg.assign.dp;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntersectionTransitions {

  public static List<HmmTransition> getTransition(IntersectionStates state) {

    List<HmmTransition> transitions = new ArrayList<>();
//    if (stage % 2 == 0) {
      switch (state) {
        case A:
          transitions.addAll(
              Arrays.asList(
                  new IntersectionTransition(IntersectionStates.A, 1.0),
                  new IntersectionTransition(IntersectionStates.B, 1.0)
              )
          );
          break;
        case B:
          transitions.addAll(
              Arrays.asList(
                  new IntersectionTransition(IntersectionStates.B, 1.0),
                  new IntersectionTransition(IntersectionStates.C, 1.0)
              )
          );
          break;
        case C:
          transitions.addAll(
              Arrays.asList(
                  new IntersectionTransition(IntersectionStates.C, 1.0),
                  new IntersectionTransition(IntersectionStates.D, 1.0)
              )
          );
          break;
        case D:
          transitions.addAll(
              Arrays.asList(
                  new IntersectionTransition(IntersectionStates.D, 1.0),
                  new IntersectionTransition(IntersectionStates.E, 1.0)
              )
          );
          break;
        case E:
          transitions.addAll(
              Arrays.asList(
                  new IntersectionTransition(IntersectionStates.E, 1.0),
                  new IntersectionTransition(IntersectionStates.F, 1.0)
              )
          );
          break;
        case F:
          transitions.addAll(
              Arrays.asList(
                  new IntersectionTransition(IntersectionStates.F, 1.0)
              )
          );
          break;
      }
//    } else {
//      switch (state) {
//        case A:
//          transitions.addAll(
//              Arrays.asList(
//                  new IntersectionTransition(IntersectionStates.A, 1.0)
//              )
//          );
//          break;
//        case B:
//          transitions.addAll(
//              Arrays.asList(
//                  new IntersectionTransition(IntersectionStates.A, 1.0),
//                  new IntersectionTransition(IntersectionStates.B, 1.0)
//              )
//          );
//          break;
//        case C:
//          transitions.addAll(
//              Arrays.asList(
//                  new IntersectionTransition(IntersectionStates.B, 1.0),
//                  new IntersectionTransition(IntersectionStates.C, 1.0)
//              )
//          );
//          break;
//        case D:
//          transitions.addAll(
//              Arrays.asList(
//                  new IntersectionTransition(IntersectionStates.C, 1.0),
//                  new IntersectionTransition(IntersectionStates.D, 1.0)
//              )
//          );
//          break;
//        case E:
//          transitions.addAll(
//              Arrays.asList(
//                  new IntersectionTransition(IntersectionStates.D, 1.0),
//                  new IntersectionTransition(IntersectionStates.E, 1.0)
//              )
//          );
//          break;
//        case F:
//          transitions.addAll(
//              Arrays.asList(
//                  new IntersectionTransition(IntersectionStates.E, 1.0),
//                  new IntersectionTransition(IntersectionStates.F, 1.0)
//              )
//          );
//          break;
//      }
//    }

    return transitions;

  }

}