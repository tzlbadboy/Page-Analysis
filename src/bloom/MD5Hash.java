/*
Copyright 2012 Frederic Langlet
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
you may obtain a copy of the License at

                http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package bloom;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Hash implements HashFunction
{
   private static final MessageDigest DIGEST = init();

   private static MessageDigest init()
   {
      try
      {
         return MessageDigest.getInstance("MD5");
      }
      catch (NoSuchAlgorithmException e)
      {
         return null;
      }
   }
   
   private final int salt;


   public MD5Hash()
   {
      this.salt = 31;
   }


   public MD5Hash(int salt)
   {
      this.salt = salt;
   }


   public int hashCode(byte[] input)
   {
      final byte[] output = DIGEST.digest(input);
      final int len = output.length;
      final int len4 = len & -4;
      int hash = this.salt;
      int val;

      for (int i=0; i<len4; i+=4)
      {
         val  = (output[i]   & 0xFF) << 24;
         val |= (output[i+1] & 0xFF) << 16;
         val |= (output[i+2] & 0xFF) << 8;
         val |= (output[i+3] & 0xFF);
         hash ^= val;
      }

      val = 0;

      for (int i=len4; i<len; i++)
         val = (val <<= 8) | (input[i] & 0xFF);

      hash ^= val;
      return hash;
   }
}
