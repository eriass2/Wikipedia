/**
 *  Copyright 2011 Rapleaf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package trie;

import java.util.Set;

final class SingleChildNode<V> extends AbstractNode<V> {
  private final char[] prefix;
  private final AbstractNode<V> child;

  protected SingleChildNode(char[] prefix, V value, AbstractNode<V> child) {
    super(value);
    this.prefix = prefix;
    this.child = child;
  }

  @Override
  public AbstractNode<V>[] getChildren() {
    return new AbstractNode[]{child};
  }

  @Override
  public char[] getPrefix() {
    return prefix;
  }

  @Override
  public V get(char[] searchArr, int startOffset) {
    int commonLength = Utils.getCommonLength(searchArr, startOffset, child.getPrefix());
    if (commonLength == child.getPrefix().length) {
      if (searchArr.length == commonLength + startOffset) {
        return child.value;
      } else {
        return child.get(searchArr, startOffset + commonLength);
      }
    }
    return null;
  }

  @Override
  public void getPartialMatches(Set<String> partialMatches, char[] searchArr, int searchArrOffset) {
    char keyPrefixFirst = searchArr[searchArrOffset];
    char childPrefixFirst = child.getPrefixFirst();
    if (keyPrefixFirst == childPrefixFirst) {
      // if we get here the, the first chars match, which is exciting. if the
      // rest of the strings don't match exactly, we don't have to explore any
      // further.

      // a quick check we can do is to see if this current child prefix is
      // longer than what's left in the search key, as that would guarantee a
      // non-match.
      // TODO: evaluate if this is actually beneficial
      final char[] childPrefix = child.getPrefix();
      if (childPrefix.length > searchArr.length - searchArrOffset) {
        return;
      }

      // now let's check if the next bit of searchArr matches the prefix. we
      // already checked the first char, so we can start with 1. note that this
      // will short-circuit itself right away if the child prefix is only 1
      // char.
      for (int i = 1; i < childPrefix.length; i++) {
        if (childPrefix[i] != searchArr[searchArrOffset + i]) {
          return;
        }
      }

      // if we reach this point, then we've got a child match. 
      // if this child has value, then add that partial match.
      if (child.value != null) {
        partialMatches.add(new String(searchArr, 0, searchArrOffset + childPrefix.length));
      }

      // recurse if there's any searchArr left
      if (searchArrOffset + childPrefix.length < searchArr.length) {
        child.getPartialMatches(partialMatches, searchArr, searchArrOffset + childPrefix.length);
      }
    }
//    int commonLength = Utils.getCommonLength(searchArr, searchArrOffset, child.getPrefix());
//    if (commonLength == child.getPrefix().length) {
//      if (child.value != null) {
//        partialMatches.add(new String(searchArr, 0, searchArrOffset + commonLength));
//      }
//      if (searchArrOffset + commonLength < searchArr.length) {
//        child.getPartialMatches(partialMatches, searchArr, searchArrOffset + commonLength);
//      }
//    }
  }

  @Override
  public char getPrefixFirst() {
    return prefix[0];
  }
}
