package rxjava;


/**
 * A cold Observable is lazy and never begins to emit events until someone is actually subscribed.
 * This also implies that every subscriber receives its own copy of the stream because events are
 * produced lazily but also not likely cached in any way.
 * Cold Observables typically come from Observable.create(), which should not start any logic but
 * instead postpone it until someone actually listens.
 * <p>
 * Common examples of cold Observables are create(), Observable.just(), from(), range(), etc..
 * <p>
 * Hot Observables are different. After you get a hold of such an Observable it might already be
 * emitting events no matter how many Subscribers they have. Observable pushes events downstream,
 * even if no one listens and events are possibly missed.
 * <p>
 * Difference b/t hot and cold Observables:
 * - No matter when you subscribe to a cold Observable—immediately or after hours—you always receive
 *   a complete and consistent set of events. On the other hand, if the Observable is hot, you can
 *   never be sure you received all events from the beginning.
 * - A cold Observable produces values on demand and possibly multiple times so the exact instant
 *   when an item was created is irrelevant. Conversely, hot Observables places events as they come.
 */
public class TestHotCodeObservable {



}
