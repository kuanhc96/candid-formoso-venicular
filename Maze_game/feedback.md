# Feedback

Group lemon: liuj77, log2301

Commit hash: 5fd7746b8225c743a997b7509cb4e200f2194230

Raw score: 34 / 60

## Checkstyle

Score: 0 / 5

❌ <b>FAIL</b>: (weight=1.0) `ArrayDisjointSets.java:5:8 [UnusedImportsCheck]`  
> Unused import - datastructures.sets.ISet.

❌ <b>FAIL</b>: (weight=1.0) `ArrayDisjointSets.java:6:8 [UnusedImportsCheck]`  
> Unused import - misc.exceptions.NotYetImplementedException.

❌ <b>FAIL</b>: (weight=1.0) `ArrayDisjointSets.java:24:0 [TodoCommentCheck]`  
> Found a stray TODO/FIXME comment

❌ <b>FAIL</b>: (weight=1.0) `ArrayDisjointSets.java:35:0 [TodoCommentCheck]`  
> Found a stray TODO/FIXME comment

❌ <b>FAIL</b>: (weight=1.0) `ArrayDisjointSets.java:65:0 [TodoCommentCheck]`  
> Found a stray TODO/FIXME comment

❌ <b>FAIL</b>: (weight=1.0) `ArrayDisjointSets.java:87:0 [TodoCommentCheck]`  
> Found a stray TODO/FIXME comment

❌ <b>FAIL</b>: (weight=1.0) `KruskalMazeCarver.java:11:8 [UnusedImportsCheck]`  
> Unused import - misc.exceptions.NotYetImplementedException.

❌ <b>FAIL</b>: (weight=1.0) `KruskalMazeCarver.java:49:32 [LocalVariableNameCheck]`  
> Name 'MST' must match pattern '^[a-z][a-zA-Z0-9]*$'.

## ArrayDisjointSets

Score: 10 / 20

<details open>
<summary>❌ <b>FAIL</b>: (weight=1.0) <code>testFindSetNonexistentItemThrowsException()</code></summary>

> <details>
> <summary>
> 
> ``` java
> org.opentest4j.AssertionFailedError: Unexpected exception type thrown ==> expected: <java.lang.IllegalArgumentException> but was: <datastructures.dictionaries.NoSuchKeyException>
> ```
> </summary>
> 
> ``` java
>     at org.junit.jupiter.api.AssertThrows.assertThrows(AssertThrows.java:65)
>     at org.junit.jupiter.api.AssertThrows.assertThrows(AssertThrows.java:37)
>     at org.junit.jupiter.api.Assertions.assertThrows(Assertions.java:2952)
> ```
> </details>
> <details>
> <summary>
> 
> ``` java
>     at datastructures.disjointsets.TestSecretArrayDisjointSets.testFindSetNonexistentItemThrowsException(TestSecretArrayDisjointSets.java:63)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>
> <details>
> <summary>
> 
> ``` java
> Caused by: datastructures.dictionaries.NoSuchKeyException
>     at datastructures.dictionaries.ArrayDictionary.get(ArrayDictionary.java:71)
>     at datastructures.disjointsets.ArrayDisjointSets.findSet(ArrayDisjointSets.java:67)
>     at datastructures.disjointsets.TestSecretArrayDisjointSets.lambda$testFindSetNonexistentItemThrowsException$3(TestSecretArrayDisjointSets.java:63)
> ```
> </summary>
> 
> ``` java
>     at org.junit.jupiter.api.AssertThrows.assertThrows(AssertThrows.java:55)
> ```
> </details>
> 
> ``` java
>     ... 15 more
> ```

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testMakeSetAndFindSetSimple()</code></summary>
</details>

<details>
<summary>✔ <b>PASS</b>: (weight=2.0) <code>testPathCompression()</code> <i>[expand for description]</i></summary>

> Checks the internal array to test whether the path-compression optimization is correctly
> implemented.

</details>

<details open>
<summary>❌ <b>FAIL</b>: (weight=2.0) <code>testUnionByRank()</code> <i>[expand for description]</i></summary>

> Checks the internal array to test whether the union-by-rank optimization is correctly implemented.
>
> <details>
> <summary>
> 
> ``` java
> java.lang.AssertionError: Check that internal array values are correct
> Expected: is [<-2>, <0>, <0>] 
>      but: item 0: was <2>
>     at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:18)
>     at datastructures.disjointsets.TestSecretArrayDisjointSets.testUnionByRank(TestSecretArrayDisjointSets.java:225)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testUnionDifferentSetsReturnValue()</code></summary>
</details>

<details open>
<summary>❌ <b>FAIL</b>: (weight=1.0) <code>testUnionManySetsAndFindRepeatedly()</code></summary>

> <details>
> <summary>
> 
> ``` java
> org.opentest4j.AssertionFailedError: execution timed out after 4000 ms
> ```
> </summary>
> 
> ``` java
>     at org.junit.jupiter.api.AssertTimeout.assertTimeoutPreemptively(AssertTimeout.java:144)
>     at org.junit.jupiter.api.AssertTimeout.assertTimeoutPreemptively(AssertTimeout.java:115)
>     at org.junit.jupiter.api.AssertTimeout.assertTimeoutPreemptively(AssertTimeout.java:97)
>     at org.junit.jupiter.api.AssertTimeout.assertTimeoutPreemptively(AssertTimeout.java:93)
>     at org.junit.jupiter.api.Assertions.assertTimeoutPreemptively(Assertions.java:3268)
> ```
> </details>
> <details>
> <summary>
> 
> ``` java
>     at datastructures.disjointsets.TestArrayDisjointSets.testUnionManySetsAndFindRepeatedly(TestArrayDisjointSets.java:82)
>     at datastructures.disjointsets.TestSecretArrayDisjointSets.testUnionManySetsAndFindRepeatedly(TestSecretArrayDisjointSets.java:55)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>

</details>

<details open>
<summary>❌ <b>FAIL</b>: (weight=1.0) <code>testUnionNonexistentItemThrowsException()</code></summary>

> <details>
> <summary>
> 
> ``` java
> org.opentest4j.AssertionFailedError: Unexpected exception type thrown ==> expected: <java.lang.IllegalArgumentException> but was: <datastructures.dictionaries.NoSuchKeyException>
> ```
> </summary>
> 
> ``` java
>     at org.junit.jupiter.api.AssertThrows.assertThrows(AssertThrows.java:65)
>     at org.junit.jupiter.api.AssertThrows.assertThrows(AssertThrows.java:37)
>     at org.junit.jupiter.api.Assertions.assertThrows(Assertions.java:2952)
> ```
> </details>
> <details>
> <summary>
> 
> ``` java
>     at datastructures.disjointsets.TestSecretArrayDisjointSets.testUnionNonexistentItemThrowsException(TestSecretArrayDisjointSets.java:71)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>
> <details>
> <summary>
> 
> ``` java
> Caused by: datastructures.dictionaries.NoSuchKeyException
>     at datastructures.dictionaries.ArrayDictionary.get(ArrayDictionary.java:71)
>     at datastructures.disjointsets.ArrayDisjointSets.union(ArrayDisjointSets.java:89)
>     at datastructures.disjointsets.TestSecretArrayDisjointSets.lambda$testUnionNonexistentItemThrowsException$4(TestSecretArrayDisjointSets.java:71)
> ```
> </summary>
> 
> ``` java
>     at org.junit.jupiter.api.AssertThrows.assertThrows(AssertThrows.java:55)
> ```
> </details>
> 
> ``` java
>     ... 15 more
> ```

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=0.5) <code>testUnionSameSet()</code> <i>[expand for description]</i></summary>

> Tests whether unioning two items in the same set correctly does nothing and returns false. (Causing
> an item to point to itself or one of its children will likely result in a stack overflow or timeout
> due to infinite recursion or infinite looping.)

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=0.5) <code>testUnionSelf()</code> <i>[expand for description]</i></summary>

> Tests whether unioning an item with itself correctly does nothing and returns false.

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testUnionSimple()</code></summary>
</details>

## Graph

Score: 24 / 30

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testAddEdgeWithNonexistentVertexThrowsException()</code></summary>
</details>

<details open>
<summary>❌ <b>FAIL</b>: (weight=1.0) <code>testFindMstRepeatedly()</code> <i>[expand for description]</i></summary>

> Tests that finding an MST does not mutate the graph by calling `findMinimumSpanningTree` multiple
> times.
>
> <details>
> <summary>
> 
> ``` java
> java.lang.AssertionError: Check that MST has correct edges
> Expected: is a set of size <4> containing <[0, 2, 4, 5]>
>      but: was a set of size <6> containing <[0, 2, 4, 5, 1, 3]>
>     at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:18)
>     at datastructures.graphs.TestSecretGraph.testFindMstRepeatedly(TestSecretGraph.java:316)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testFindMstSimple()</code></summary>
</details>

<details>
<summary>✔ <b>PASS</b>: (weight=2.0) <code>testFindMstWithParallelEdges()</code> <i>[expand for description]</i></summary>

> Tests that finding an MST on a graph with many parallel edges works correctly.

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testFindMstWithSelfLoop()</code> <i>[expand for description]</i></summary>

> Tests that finding an MST on a graph with a low-cost self-loop works correctly.

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=0.5) <code>testFindShortestPathComplex()</code></summary>
</details>

<details>
<summary>✔ <b>PASS</b>: (weight=0.5) <code>testFindShortestPathDisconnectedComponents()</code></summary>
</details>

<details open>
<summary>❌ <b>FAIL</b>: (weight=0.5) <code>testFindShortestPathOnRandomGraphs()</code> <i>[expand for description]</i></summary>

> Tests correctness of finding shortest paths on several random graphs (order of magnitude 10,000
> vertices, each with a few edges).
>
> <details>
> <summary>
> 
> ``` java
> org.opentest4j.AssertionFailedError: Exception while running test
> ```
> </summary>
> 
> ``` java
>     at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:43)
>     at org.junit.jupiter.api.Assertions.fail(Assertions.java:129)
> ```
> </details>
> <details>
> <summary>
> 
> ``` java
>     at secret.TestUtils$FailureResult.getResult(TestUtils.java:129)
>     at datastructures.graphs.TestSecretGraph.testFindShortestPathOnRandomGraphs(TestSecretGraph.java:143)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>
> 
> ``` java
> Caused by: datastructures.priorityqueues.InvalidElementException
>     at datastructures.priorityqueues.ArrayHeapPriorityQueue.replace(ArrayHeapPriorityQueue.java:130)
>     at datastructures.graphs.SolutionGraph.getBackpointers(SolutionGraph.java:211)
>     at datastructures.graphs.SolutionGraph.findShortestPathBetween(SolutionGraph.java:169)
>     at datastructures.graphs.TestSecretGraph.lambda$findShortestPathOnRandomGraphs$2(TestSecretGraph.java:127)
>     at secret.TestUtils.lambda$time$0(TestUtils.java:81)
>     ... 5 more
> ```

</details>

<details open>
<summary>❌ <b>FAIL</b>: (weight=1.5) <code>testFindShortestPathOnRandomGraphsIsEfficient()</code> <i>[expand for description]</i></summary>

> Tests correctness and efficiency of finding shortest paths on several random graphs (order of
> magnitude 10,000 vertices, each with only a few edges).
>
> <details>
> <summary>
> 
> ``` java
> java.lang.RuntimeException: Exception while running test with expected runtime 10000 ms
>     at secret.TestUtils$FailureResult.assertRuntimeLessThan(TestUtils.java:124)
>     at datastructures.graphs.TestSecretGraph.testFindShortestPathOnRandomGraphsIsEfficient(TestSecretGraph.java:148)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>
> 
> ``` java
> Caused by: datastructures.priorityqueues.InvalidElementException
>     at datastructures.priorityqueues.ArrayHeapPriorityQueue.replace(ArrayHeapPriorityQueue.java:130)
>     at datastructures.graphs.SolutionGraph.getBackpointers(SolutionGraph.java:211)
>     at datastructures.graphs.SolutionGraph.findShortestPathBetween(SolutionGraph.java:169)
>     at datastructures.graphs.TestSecretGraph.lambda$findShortestPathOnRandomGraphs$2(TestSecretGraph.java:127)
>     at secret.TestUtils.lambda$time$0(TestUtils.java:81)
>     ... 5 more
> ```

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testFindShortestPathRepeatedly()</code> <i>[expand for description]</i></summary>

> Tests that finding shortest paths does not mutate the graph by calling `findShortestPathBetween`
> multiple times.

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=0.5) <code>testFindShortestPathSameStartAndEnd()</code></summary>
</details>

<details>
<summary>✔ <b>PASS</b>: (weight=0.5) <code>testFindShortestPathSimple()</code></summary>
</details>

<details>
<summary>✔ <b>PASS</b>: (weight=2.0) <code>testFindShortestPathUpdatesPriorityQueueCorrectly()</code> <i>[expand for description]</i></summary>

> Tests whether finding shortest paths correctly updates the priority queue while running Dijkstra's
> algorithm by using a graph that will produce incorrect results otherwise (e.g., if elements in the
> priority queue are mutated).

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=2.0) <code>testFindShortestPathWithLongPathIsEfficient()</code> <i>[expand for description]</i></summary>

> Tests the efficiency of finding a shortest path when the shortest path is very long (order of
> magnitude 1000 edges long).

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testFindShortestPathWithNonIntegerWeights()</code> <i>[expand for description]</i></summary>

> Tests whether finding shortest paths works correctly when some paths differ in cost by less than 1.

</details>

<details>
<summary>✔ <b>PASS</b>: (weight=1.0) <code>testSizeMethods()</code></summary>
</details>

## KruskalMazeCarver

Score: 0 / 5

<details open>
<summary>❌ <b>FAIL</b>: (weight=3.0) <code>testOutputIsViableMST()</code> <i>[expand for description]</i></summary>

> Tests that the output of `returnWallsToRemove` forms a valid MST of the input maze.
>
> <details>
> <summary>
> 
> ``` java
> misc.exceptions.NotYetImplementedException
>     at misc.Sorter.topKSort(Sorter.java:11)
>     at datastructures.graphs.SolutionGraph.findMinimumSpanningTree(SolutionGraph.java:134)
>     at mazes.generators.carvers.KruskalMazeCarver.returnWallsToRemove(KruskalMazeCarver.java:49)
>     at secret.TestSecretKruskalMazeCarver.testOutputIsViableMST(TestSecretKruskalMazeCarver.java:48)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>

</details>

<details open>
<summary>❌ <b>FAIL</b>: (weight=2.0) <code>testOutputIsRandom()</code> <i>[expand for description]</i></summary>

> Tests that running `returnWallsToRemove` repeatedly produces different outputs.
>
> <details>
> <summary>
> 
> ``` java
> misc.exceptions.NotYetImplementedException
>     at misc.Sorter.topKSort(Sorter.java:11)
>     at datastructures.graphs.SolutionGraph.findMinimumSpanningTree(SolutionGraph.java:134)
>     at mazes.generators.carvers.KruskalMazeCarver.returnWallsToRemove(KruskalMazeCarver.java:49)
>     at secret.TestSecretKruskalMazeCarver.testOutputIsRandom(TestSecretKruskalMazeCarver.java:75)
> ```
> </summary>
> 
> ``` java
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
>     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
>     at java.base/java.lang.reflect.Method.invoke(Method.java:567)
>     at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:675)
>     at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
>     at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:125)
>     at org.junit.jupiter.api.AssertTimeout.lambda$assertTimeoutPreemptively$4(AssertTimeout.java:132)
>     at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
>     at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
>     at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
>     at java.base/java.lang.Thread.run(Thread.java:835)
> ```
> </details>

</details>
