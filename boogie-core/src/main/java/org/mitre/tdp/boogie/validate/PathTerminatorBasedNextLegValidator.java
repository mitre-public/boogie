package org.mitre.tdp.boogie.validate;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.PathTerminator.*;

import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;

/**
 * This class implements ARINC 424 Supplement 22's next leg table. Legs can only be put together in reasonable ways based
 * on the actual paths and instructions they represent. The tables in Attachment 5 represent this reality.
 * In this instance, we don't care about the actual arinc version.
 * This is about combinations that actually work from the perspective of having a single continuous route of flight per our use of the ARINC 424 data in MITRE code.
 */
public final class PathTerminatorBasedNextLegValidator implements BiPredicate<Leg, Leg> {
  public static final Set<PathTerminator> AF_NEXT_LEG = Set.of(AF, CA, CD, CF, CI, CR, FA, FC, FD, FM, HA, HF, HM, RF, TF, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> CA_CR_FA_VA_VM_VR_NEXT_LEG = Set.of(CA, CD, CF, CI, CR, DF, FA, FC, FD, FM, IF, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> CD_VD_NEXT_LEG = Set.of(AF, CA, CD, CF, CI, CR, DF, FA, FC, FD, FM, IF, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> CF_DF_IF_TF_NEXT_LEG = Set.of(AF, CA, CD, CF, CI, CR, DF, FA, FC, FD, FM, HA, HF, HM, PI, RF, TF, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> CI_VI_NEXT_LEG = Set.of(AF, CF, FA, FC, FD, FM, IF);
  public static final Set<PathTerminator> FC_NEXT_LEG = Set.of(CA, CD, CF, CI, CR, DF, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> FD_NEXT_LEG = Set.of(AF, CA, CD, CF, CI, CR, DF, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> FM_NEXT_LEG = Set.of(CA, CD, CF, CI, CR, DF, FA, FC, FD, FM, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> HA_HF_HM_NEXT_LEG = Set.of(AF, CA, CD, CF, CI, CR, DF, FA, FC, FD, FM, RF, TF, VA, VD, VI, VM, VR);
  public static final Set<PathTerminator> PI_NEXT_LEG = Set.of(CF);
  public static final Set<PathTerminator> RF_NEXT_LEG = Set.of(CA, CD, CF, CI, CR, FA, FC, FD, FM, HA, HF, HM, RF, TF);

  public static final Map<PathTerminator, Set<PathTerminator>> NEXT_LEGS = Map.ofEntries(
       Map.entry(AF, AF_NEXT_LEG),
       Map.entry(CA, CA_CR_FA_VA_VM_VR_NEXT_LEG),
       Map.entry(CR, CA_CR_FA_VA_VM_VR_NEXT_LEG),
       Map.entry(FA, CA_CR_FA_VA_VM_VR_NEXT_LEG),
       Map.entry(VA, CA_CR_FA_VA_VM_VR_NEXT_LEG),
       Map.entry(CD, CD_VD_NEXT_LEG),
       Map.entry(VD, CD_VD_NEXT_LEG),
       Map.entry(CF, CF_DF_IF_TF_NEXT_LEG),
       Map.entry(DF, CF_DF_IF_TF_NEXT_LEG),
       Map.entry(IF, CF_DF_IF_TF_NEXT_LEG),
       Map.entry(TF, CF_DF_IF_TF_NEXT_LEG),
       Map.entry(CI, CI_VI_NEXT_LEG),
       Map.entry(VI, CI_VI_NEXT_LEG),
       Map.entry(FC, FC_NEXT_LEG),
       Map.entry(FD, FD_NEXT_LEG),
       Map.entry(FM, FM_NEXT_LEG),
       Map.entry(VM, CA_CR_FA_VA_VM_VR_NEXT_LEG),
       Map.entry(VR, CA_CR_FA_VA_VM_VR_NEXT_LEG),
       Map.entry(HA, HA_HF_HM_NEXT_LEG),
       Map.entry(HF, HA_HF_HM_NEXT_LEG),
       Map.entry(HM, HA_HF_HM_NEXT_LEG),
       Map.entry(PI, PI_NEXT_LEG),
       Map.entry(RF, RF_NEXT_LEG)
      );

  public PathTerminatorBasedNextLegValidator newInstance() {
    return new PathTerminatorBasedNextLegValidator();
  }

  public PathTerminatorBasedNextLegValidator() {
    checkArgument(NEXT_LEGS.size() == 23, "Wrong number of path terminators in the map");
  }

  /**
   * The test for the pair of legs.
   * @param current the first input argument
   * @param next the second input argument
   * @return true if the next leg is valid because its path terminator.
   */
  @Override
  public boolean test(Leg current, Leg next) {
    return NEXT_LEGS.get(current.pathTerminator()).contains(next.pathTerminator());
  }
}
