This is the boogie module primarily centered around dynamic programming 
concepts. In the case of boogie this is typically used to evaluate the
conformance of an aircraft to a particular set of legs. These legs could
 be from multiple or a single procedure, include airway legs or etc. All 
 that matters is they implement the boogie-conformance scorable interface.
 
The algorithm itself is a simple two-step computation:
1) Each individual ConformablePoint is scored against the set of 