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

final class Utils {
  private Utils() {}

  static int getCommonLength(char[] origChars, int off, char[] prefixChars) {
    int extent = Math.min(origChars.length-off, prefixChars.length);
    int i = 0;
    for (; i < extent; i++) {
      if (origChars[off+i] != prefixChars[i]) {
        break;
      }
    }
    return i;
  }
}
