package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Identifies issues in a value and resolves those findings into a replacement value.
 * Identification can also be called independently to inspect data without repairing it.
 *
 * <p>An identifier supplies typed findings; a resolver applies a repair policy to the value and those
 * findings. {@link #apply(Object)} makes one identification pass and calls the resolver only when issues
 * are present. Findings passed to the resolver are immutable and refer to that same input value.
 * Implementations must not mutate the input. The caller remains responsible for validating the
 * result; a resolver can reject issues it cannot safely repair with an {@link IllegalArgumentException}.
 * Additional policies can be composed with {@link #andThen(Function)}, so each policy identifies
 * issues in the preceding policy's result.
 *
 * @param <T> value being inspected and repaired
 * @param <I> finding type, including any locations or measurements needed by the resolver
 */
public class IssueResolver<T, I> implements Function<T, T> {
  private final Function<? super T, ? extends List<? extends I>> identifier;
  private final BiFunction<? super T, ? super List<I>, ? extends T> resolver;

  /**
   * @param identifier function that inspects a value without changing it
   * @param resolver function that resolves findings into a replacement value
   */
  public IssueResolver(Function<? super T, ? extends List<? extends I>> identifier, BiFunction<? super T, ? super List<I>, ? extends T> resolver) {
    requireNonNull(identifier, "identifier");
    requireNonNull(resolver, "resolver");
    this.identifier = identifier;
    this.resolver = resolver;
  }

  /**
   * Identifies issues without changing the value.
   * @param value input to inspect
   * @return findings for this value, or an empty list when no issues were found
   */
  public final List<I> identify(T value) {
    requireNonNull(value, "value");
    return List.copyOf(identifier.apply(value));
  }

  /**
   * Resolves findings without mutating the input.
   * @param value input in which the findings were identified
   * @param issues findings to resolve in this value
   * @return replacement value
   */
  public final T resolve(T value, List<I> issues) {
    requireNonNull(value, "value");
    requireNonNull(issues, "issues");
    T resolved = resolver.apply(value, List.copyOf(issues));
    requireNonNull(resolved, "Resolver returned null");
    return resolved;
  }

  @Override
  public T apply(T value) {
    return Optional.of(identify(value))
        .filter(issues -> !issues.isEmpty())
        .map(issues -> resolve(value, issues))
        .orElse(value);
  }

  /**
   * Combines independent identification and resolution functions into one reusable policy.
   * @param identifier function that inspects a value without changing it
   * @param resolver function that resolves findings into a replacement value
   * @param <T> value type
   * @param <I> finding type
   * @return policy supporting inspection, explicit resolution, and function composition
   */
  public static <T, I> IssueResolver<T, I> of(Function<? super T, ? extends List<? extends I>> identifier, BiFunction<? super T, ? super List<I>, ? extends T> resolver) {
    return new IssueResolver<>(identifier, resolver);
  }
}
