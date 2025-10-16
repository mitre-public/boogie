package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ArincDatumCode {
  /**
   * Adindan
   *
   */
  ADI,

  /**
   * Afgooye
   *
   */
  AFG,

  /**
   * Antigua Island Astro 1943
   *
   */
  AIA,

  /**
   * Ain El Abd 1970
   *
   */
  AIN,

  /**
   * American Samoa 1962
   *
   */
  AMA,

  /**
   * Anna 1 Astro 1965
   *
   */
  ANO,

  /**
   * Austria NS
   *
   */
  ANS,

  /**
   * Arc 1950
   *
   */
  ARF,

  /**
   * Arc 1960
   *
   */
  ARS,

  /**
   * Ascension Island 1958
   *
   */
  ASC,

  /**
   * Montserrat Island Astro 1958
   *
   */
  ASM,

  /**
   * Astronomical Station 1952
   *
   */
  ASQ,

  /**
   * Astro Beacon â€œEâ€? 1945
   *
   */
  ATF,

  /**
   * Australian Geodetic 1966
   *
   */
  AUA,

  /**
   * Australian Geodetic 1984
   *
   */
  AUG,

  /**
   * Djakarta (Batavia)
   *
   */
  BAT,

  /**
   * Bermuda 1957
   *
   */
  BER,

  /**
   * Bissau
   *
   */
  BID,

  /**
   * Bogota Observatory
   *
   */
  BOO,

  /**
   * Bukit Rimpah
   *
   */
  BUR,

  /**
   * Cape Canaveral
   *
   */
  CAC,

  /**
   * Campo Inchauspe 1969
   *
   */
  CAI,

  /**
   * Canton Astro 1966
   *
   */
  CAO,

  /**
   * Cape
   *
   */
  CAP,

  /**
   * Camp Area Astro
   *
   */
  CAZ,

  /**
   * S-JTSK
   *
   */
  CCD,

  /**
   * Carthage
   *
   */
  CGE,

  /**
   * Chatham Island Astro 1971
   *
   */
  CHI,

  /**
   * Chua Astro
   *
   */
  CHU,

  /**
   * Corrego Alegre
   *
   */
  COA,

  /**
   * Dabola
   *
   */
  DAL,

  /**
   * Danish Geodetic Institute 1934 System
   *
   */
  DAN,

  /**
   * Deception Island
   *
   */
  DID,

  /**
   * GUX 1 Astro
   *
   */
  DOB,

  /**
   * Easter Island 1967
   *
   */
  EAS,

  /**
   * Wake-Eniwetok, 1960
   *
   */
  ENW,

  /**
   * Co-Ordinate System 1937 of Estonia
   *
   */
  EST,

  /**
   * European 1950
   *
   */
  EUR,

  /**
   * European 1979
   *
   */
  EUS,

  /**
   * Oman
   *
   */
  FAH,

  /**
   * Observatorio Meteorologico 1939
   *
   */
  FLO,

  /**
   * Fort Thomas 1955
   *
   */
  FOT,

  /**
   * Gan 1970
   *
   */
  GAA,

  /**
   * Gandajika Base
   *
   */
  GAN,

  /**
   * Geodetic Datum 1949
   *
   */
  GEO,

  /**
   * DOS 1968
   *
   */
  GIZ,

  /**
   * Graciosa Base SW 1948
   *
   */
  GRA,

  /**
   * Greek Geodetic Reference System 1987
   *
   */
  GRX,

  /**
   * Gunuung Segara
   *
   */
  GSE,

  /**
   * Guam 1963
   *
   */
  GUA,

  /**
   * Herat North
   *
   */
  HEN,

  /**
   * Hermannskogel
   *
   */
  HER,

  /**
   * Provisional South Chilean 1963 (also known as Hito XVIII 1963)
   *
   */
  HIT,

  /**
   * Hjorsey 1955
   *
   */
  HJO,

  /**
   * Hong Kong 1963
   *
   */
  HKD,

  /**
   * Hu-Tzu-Shan
   *
   */
  HTN,

  /**
   * Bellevue (IGN)
   *
   */
  IBE,

  /**
   * Indonesian 1974
   *
   */
  IDN,

  /**
   * Nouvelle Triangulation de France (FRANCE)
   *
   */
  IGF,

  /**
   * Indian
   *
   */
  IND,

  /**
   * Indian 1954
   *
   */
  INF,

  /**
   * Indian 1960
   *
   */
  ING,

  /**
   * Indian 1975
   *
   */
  INH,

  /**
   * Ireland 1965
   *
   */
  IRL,

  /**
   * ISTS 061 Astro 1968
   *
   */
  ISG,

  /**
   * ISTS 073 Astro 1969
   *
   */
  IST,

  /**
   * Johnston Island 1961
   *
   */
  JOH,

  /**
   * Kandawala
   *
   */
  KAN,

  /**
   * Kertau 1948
   *
   */
  KEA,

  /**
   * Kerguelen Island 1949
   *
   */
  KEG,

  /**
   * Kusaie Astro 1951
   *
   */
  KUS,

  /**
   * L.C. 5 Astro 1961
   *
   */
  LCF,

  /**
   * Leigon
   *
   */
  LEH,

  /**
   * Liberia 1964
   *
   */
  LIB,

  /**
   * Luzon
   *
   */
  LUZ,

  /**
   * Massawa
   *
   */
  MAS,

  /**
   * Manchurian Principal System
   *
   */
  MCN,

  /**
   * Merchich
   *
   */
  MER,

  /**
   * Midway Astro 1961
   *
   */
  MID,

  /**
   * Mahe 1971
   *
   */
  MIK,

  /**
   * Minna
   *
   */
  MIN,

  /**
   * Rome 1940
   *
   */
  MOD,

  /**
   * Montjong Lowe
   *
   */
  MOL,

  /**
   * M'Poraloko
   *
   */
  MPO,

  /**
   * Viti Levu 1916
   *
   */
  MVS,

  /**
   * Nahrwan
   *
   */
  NAH,

  /**
   * Nanking 1960
   *
   */
  NAN,

  /**
   * Naparima, BWI
   *
   */
  NAP,

  /**
   * North American 1983
   *
   */
  NAR,

  /**
   * North American 1927
   *
   */
  NAS,

  /**
   * North Sahara 1959
   *
   */
  NSD,

  /**
   * Old Egyptian 1907
   *
   */
  OEG,

  /**
   * Ordnance Survey of Great Britain 1936
   *
   */
  OGB,

  /**
   * Old Hawaiian
   *
   */
  OHA,

  /**
   * Palmer Astro
   *
   */
  PAM,

  /**
   * Potsdam
   *
   */
  PDM,

  /**
   * Ayabelle Lighthouse
   *
   */
  PHA,

  /**
   * Pitcairn Astro 1967
   *
   */
  PIT,

  /**
   * Pico de las Nieves
   *
   */
  PLN,

  /**
   * Porto Santo 1936
   *
   */
  POS,

  /**
   * Portuguese Datum 1973
   *
   */
  PRD,

  /**
   * Provisional South American 1956
   *
   */
  PRP,

  /**
   * Point 58
   *
   */
  PTB,

  /**
   * Point Noire 1948
   *
   */
  PTN,

  /**
   * Pulkovo 1942
   *
   */
  PUK,

  /**
   * Puerto Rico
   *
   */
  PUR,

  /**
   * Qatar National
   *
   */
  QAT,

  /**
   * Qornoq
   *
   */
  QUO,

  /**
   * Reunion
   *
   */
  REU,

  /**
   * Parametrop Zemp 1990 (English translation) The Parameters of the Earth 1990
   *
   */
  RPE,

  /**
   * RT90
   *
   */
  RTS,

  /**
   * Santo (DOS) 1965
   *
   */
  SAE,

  /**
   * South American 1969
   *
   */
  SAN,

  /**
   * Sao Braz
   *
   */
  SAO,

  /**
   * Sapper Hill 1943
   *
   */
  SAP,

  /**
   * Schwarzeck
   *
   */
  SCK,

  /**
   * Selvagem Grande 1938
   *
   */
  SGM,

  /**
   * Astro DOS 71/4
   *
   */
  SHB,

  /**
   * South Asia
   *
   */
  SOA,

  /**
   * S-42 (Pulkovo 1942)
   *
   */
  SPK,

  /**
   * Sierra Leone 1960
   *
   */
  SRL,

  /**
   * Stockholm 1938 (RT38)
   *
   */
  STO,

  /**
   * Sydney Observatory
   *
   */
  SYO,

  /**
   * Tananarive Observatory 1925
   *
   */
  TAN,

  /**
   * Tristan Astro 1968
   *
   */
  TDC,

  /**
   * Timbalai 1948
   *
   */
  TIL,

  /**
   * Tokyo
   *
   */
  TOY,

  /**
   * Trinidad Trigonometrical Survey
   *
   */
  TRI,

  /**
   * Astro Tern Island (Frig) 1961
   *
   */
  TRN,

  /**
   * Unknown
   *
   */
  UNK,

  /**
   * Voirol 1874
   *
   */
  VOI,

  /**
   * Voirol 1960
   *
   */
  VOR,

  /**
   * Wake Island Astro 1952
   *
   */
  WAK,

  /**
   * World Geodetic System 1960
   *
   */
  WGA,

  /**
   * World Geodetic System 1966
   *
   */
  WGB,

  /**
   * World Geodetic System 1972
   *
   */
  WGC,

  /**
   * World Geodetic System 1984
   *
   */
  WGE,

  /**
   * Yacare
   *
   */
  YAC,

  /**
   * Zanderij
   *
   */
  ZAN;

  public static Set<String> VALID = Arrays.stream(ArincDatumCode.values()).map(ArincDatumCode::name).collect(Collectors.toSet());
}
