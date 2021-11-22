# Boogie Routes

## Module overview

This module resolves the physical planned path of an aircraft through the NAS based on the route strings filed in it’s flightplans. Depending on the configured infrastructure data this route expansions can 
be generated both domestically and internationally.

## Quick start

The entry point to the code is the ```RouteExpanderFactory.java``` class. This class provides (among others) a pair of methods for generating a ```RouteExpander``` based on either 
collections of cached infrastructure data or based on various ```LookupService(s)``` which can be used to lookup infrastructure elements by identifier (as one would see them 
referenced by in a flightplan).

Assuming you have a collection of infrastructure records on hand implementing the associated ```boogie-core``` interfaces instantiating a ```RouteExpander``` is as simple as:

```java
Collection<Procedure> procedures...
Collection<Airway> airways...
Collection<Fix> fixes...
Collection<Airport> airports...

String myRouteString....

RouteExpander routeExpander = RouteExpanderFactory.newGraphicalRouteExpander(fixes, airways, airports, procedures);

// expansion through the common portion of the SID/STAR (if present)
Optional<ExpandedRoute> expandedRoute = routeExpander.apply(myRouteString);

// expansion through the appropriate runway transitions of the SID/STAR given the arr/dep runway
Optional<ExpandedRoute> expandedRoute = routeExpander.apply(myRouteString, myDepartureRunway, myArrivalRunway);

// expansion through the appropriate runway transitions and then onto the conventional approach serving the arrival runway
Optional<ExpandedRoute> expandedRoute = routeExpander.apply(myRouteString, myDepartureRunway, myArrivalRunway, CONV);

// expansion through the appropriate runway transitions and then onto the RNP approach (if one exists) or else the RNAV (if one exists) or else the CONV (if one exists)
Optional<ExpandedRoute> expandedRoute = routeExpander.apply(myRouteString, myDepartureRunway, myArrivalRunway, RNP, RNAV, CONV);
```

## What are route strings?

What is a route string? Generally speaking it's a formatted string giving an indication of the path a flight intends to take to get from its origin to its destination. This route may be 
updated several times over the course of the flight as the aircraft makes progress along its filed path or in response to changes in the current state of the NAS (e.g. severe weather, 
space launches, high demand, etc.). Typically the last filed pre-departure route (LFPD route for short) is the finalized intent of the aircraft given conditions just prior to departure and 
will contain some collection of airways, fixes, a SID, and a STAR as well as the arrival and departure airport. Over the course of the flight the aircraft will file amendments to this route 
in response to changing conditions.

Lets break down some example routes:

1. ```KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054```
2. ```KDFW.LOWGN7.ROLLS.J20.SEA.J502.YZT.J523.YZP.TR18.KATCH..HMPTN..GRIZZ..CJAYY..5800N/16000W..OGGOE..NANDY..NATES..NIKLL..NYMPH..NUZAN..NRKEY..NIPPI.R220.NANAC.Y810.KETAR.Y811.MELON..RJAA/1252```

Routes can be of varying lengths and may continue internationally - regardless all routes can be broken down generally into 6 types of elements:

<table class="table table-bordered">
  <thead>
    <tr>
      <th class="text-left">Name</th>
      <th class="text-left">Example</th>
      <th class="text-left">Location</th>
    </tr>
  </thead>
  <tbody>
    <tr><td>Airport</td><td>KBDL</td><td>Typically show up at the start and end of the route string, however it's not uncommon to file the reference navaid for the airport instead (in this case BDL)</td></tr>
    <tr><td>Fix (Waypoint/Navaid)</td><td>SHERL</td><td>Both waypoints and navaids show up in 3 primary cases in the route, two (as tailored waypoints, as entry/exit fixes) will be covered in other sections. The final is as a direct-to, this is denoted as GRIZZ..CJAYY which simply means once you arrive at GRIZZ proceed directly to CJAYY.</td></tr>
    <tr><td>Procedure</td><td>CSTL6</td><td>Procedures are typically referenced as trios of the form APT.PROC.FIX (SID) or FIX.PROC.APT (STAR) though they may be filed as FIX.PROC.FIX where one of the fixes refers the airport reference navaid. In the standard case the fix specifies the entry/exit point at which the aircraft intends to join or leave the procedure and typically ends up specifying the enroute transition the flight will take. Note the SID/STAR and rest of the route give no information about the approach procedure and arrival runway that will be used.</td></tr>
    <tr><td>Airway</td><td>J121</td><td>The first is as an entry/exit waypoint to an airway or procedure in which case they will be filed as FIX.AWY.FIX (i.e. SHERL.J121.BRIGS indicates the aircraft will join airway J121 at fix SHERL and exit it at BRIGS).</td></tr>
    <tr><td>Tailored Waypoint/Navaid</td><td>HTO354018</td><td>Tailored waypoint are generally filed with respect to a VOR but may be referring to an NDB or waypoint instead. In any case the first 3-5 characters indicate the infrastructure element the following 6 numbers are to be processed with respect to. Of the following 6 numbers the first three refer a bearing from the referenced waypoint/navaid and the following 3 give a distance in nm. That is to say the tailored waypoint HTO354018 is the point 18 miles away from navaid HTO at bearing 354 (to convert this to true course add the station declination).</td></tr>
    <tr><td>Lat/Lon</td><td>5800N/16000W</td><td>In particular for oceanic routes the filed route may contain simple lat/lon coordinates in the format DDMM{N,S}/DDDMM{E,W}. That is to say 5800N/16000W refers to coordinates (58.0,-160.0) in decimal degrees.</td></tr>
  </tbody>
</table>

In both the routes above there is a section after the final airport containing a / and 4 numbers. This can be one of two things, either an estimated enroute time (typically in pre-departure 
flightplans) or the estimated time of arrival (typically in post-departure flightplans). In both the above routes those are estimated enroute times, for the KBDL-KPHL flight the flight is 
estimated to take 54 minutes, while the KDFW-RJAA flight is estimated at 12 hours and 52 minutes. If the second was an ETA it would mean the flight was expected to arrive at 12:52 PM. Note 
there is no indication of the day of arrival.

There is also a collection of wildcard characters that can appear in flightplans next to the elements outlined above. There are a reasonable number of them but the vast majority are 
extremely uncommon to see filed, the two you're most likely to come across are (+ and *). * indicates that an ADR/ADAR (adapted arrival/departure route) has been suppressed in the 
flightplan, while + can be either military in nature in which case it means to expect multiple flyovers of the associated fix or it may occur in pairs and indicate special route printing. 

<div class="img-with-text">
<p>Domestic routes by arrival airport (<span style="color: red;">KLAX</span>, <span style="color: lime;">KSEA</span>, <span style="color: purple;">KORD</span>,
 <span style="color: cyan;">KJFK</span>, <span style="color: gold;">KDFW</span>):</p>
</div>
<br />

<img align="float: left;" height="500" src="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-routes/domestic-filed-routes.png?at=refs%2Fheads%2Fmain"/>
<br />
<div class="img-with-text">
<p>International expansions  (<span style="color: red;">WSSS</span>, <span style="color: lime;">EGLL</span>, <span style="color: purple;">EHAM</span>,
 <span style="color: cyan;">RJAA</span>, <span style="color: gold;">LEMD</span>):</p>
</div>
<br />
<img align="float: left;" height="500" src="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-routes/international-filed-routes-1.png?at=refs%2Fheads%2Fmain"/>
<img align="float: left;" height="500" src="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-routes/international-filed-routes-2.png?at=refs%2Fheads%2Fmain"/>
<br />

## How the algorithm works

The expansion algorithm itself is relatively simple and can be split into 3 primary parts:

1. <b>Section Splitting</b> - this process performs the initial subdivision of the route string along the ```.``` character to prepare for matching the subsections against known infrastructure.
e.g. ```KDFW*..LOPEZ → (id=KDFW, wildcards=*), (id=LOPEZ)```
2. <b>Section Resolution</b> - this portion takes the cleaned sections from the splitting process and preforms a broad match against all known pieces of infrastructure with the same primary 
identifier, while allowing for some simple filters to be applied on top (e.g. SID exists at departure airport, etc.).
3. <b>Shortest Path Resolution</b> - the matched sections are then fed into a directed graph and zipped together based on their declared order in the route string. Some simple rules are then applied 
to determine edge weights between adjacent sections (e.g. distance to nearest fix) and the shortest path is computed. The shortest path through the candidate sequence of infrastructure elements 
is then taken to be the assigned route.

The approach above allows us to keep the algorithm itself reasonably generic and relatively robust without needing to add too many rules/special cases to the underlying code as the graphically 
resolved path generally does a good job of handling things like repeated airways in the route string, etc.

Additionally boogie provides the legs within this path in a "flyable" form - this means the leg types accurately reflect the way the aircraft should actually fly them.
