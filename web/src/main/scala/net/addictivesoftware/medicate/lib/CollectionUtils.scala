/*
 * Copyright 2006-2011 Addictive Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.addictivesoftware.medicate.lib

trait CollectionUtils {

  /**
   * merges 2 hashmaps based on their keys, while executing the provided function on the values
   * 
   * @param maps A List containing 2 Maps
   * @param f A function to apply to values from each map with the same key
   * @return the merged map
   */
  def mergeMap[A, B](maps: List[Map[A, B]])(f: (B, B) => B): Map[A, B] =
    (Map[A, B]() /: (for (map <- maps; kv <- map) yield kv)) { (a, kv) =>
      a + (if (a.contains(kv._1)) kv._1 -> f(a(kv._1), kv._2) else kv)
    }

  /**
   * Creates an immutable Map from a mutable one
   * @param map the mutable map
   * @return an immutable map
   */
  def makeImmutable[A,B](map: scala.collection.mutable.Map[A,B]): Map[A, B] = {
    map.map(kv => (kv._1,kv._2)).toMap
  }

}