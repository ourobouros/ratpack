/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.sse;

import org.reactivestreams.Publisher;

/**
 * A {@link ratpack.handling.Context#render(Object) renderable} object for streaming server side events.
 * <p>
 * A {@link ratpack.render.Renderer renderer} for this type is implicitly provided by Ratpack core.
 * <p>
 * Example usage:
 * <pre class="java">{@code
 * import ratpack.handling.Handler;
 * import ratpack.http.client.ReceivedResponse;
 * import ratpack.sse.ServerSentEvent;
 * import ratpack.test.embed.EmbeddedApplication;
 *
 * import java.util.Arrays;
 * import java.util.stream.Collectors;
 *
 * import static java.util.concurrent.TimeUnit.MILLISECONDS;
 * import static ratpack.sse.ServerSentEvents.serverSentEvents;
 * import static ratpack.stream.Streams.periodically;
 *
 * public class Example {
 *
 *   public static void main(String[] args) throws Exception {
 *     Handler handler = context ->
 *       context.render(serverSentEvents(periodically(context.getLaunchConfig(), 5, MILLISECONDS, i ->
 *           i < 5
 *             ? ServerSentEvent.builder().id(i.toString()).type("counter").data("event " + i).build()
 *             : null
 *       )));
 *
 *     String expectedOutput = Arrays.asList(0, 1, 2, 3, 4)
 *       .stream()
 *       .map(i -> "event: counter\ndata: event " + i + "\nid: " + i + "\n")
 *       .collect(Collectors.joining("\n"))
 *       + "\n";
 *
 *     EmbeddedApplication.fromHandler(handler).test(httpClient -> {
 *       ReceivedResponse response = httpClient.get();
 *       assert response.getHeaders().get("Content-Type").equals("text/event-stream;charset=UTF-8");
 *       assert response.getBody().getText().equals(expectedOutput);
 *     });
 *   }
 *
 * }
 * }</pre>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Server-sent_events" target="_blank">Wikipedia - Using server-sent events</a>
 * @see <a href="https://developer.mozilla.org/en-US/docs/Server-sent_events/Using_server-sent_events" target="_blank">MDN - Using server-sent events</a>
 */
public class ServerSentEvents {

  public static ServerSentEvents serverSentEvents(final Publisher<? extends ServerSentEvent> publisher) {
    return new ServerSentEvents(publisher);
  }

  private final Publisher<? extends ServerSentEvent> publisher;

  private ServerSentEvents(Publisher<? extends ServerSentEvent> publisher) {
    this.publisher = publisher;
  }

  public Publisher<? extends ServerSentEvent> getPublisher() {
    return publisher;
  }
}
