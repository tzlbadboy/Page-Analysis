/*
Copyright 2014 NJU IIP
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


// A Bloom lter is a space-ecient representation of a set or a list that
// handles membership queries.
// [See http://portal.acm.org/citation.cfm?id=362692&dl=ACM&coll=portal]
// [See http://en.wikipedia.org/wiki/Bloom_filter]

public class BloomFilter<T>
{
  // 1024 * rate of false positives for given m/n and k
  // m = -kn / ( ln( 1 - c ^ 1/k ) )
  // Where c is the false positive rate, k is the number of hash functions,
  // n is the number of keys and m is the length of the bit array (in bits)
  // -1 indicates an impossible combination
  public static final int[] ERROR_RATE_1024 =
  {                                          // m/n
    //k = 1    2    3    4    5   6   7   8  //
        402, 410,  -1,  -1,  -1, -1, -1, -1, //  2
        290, 243, 259,  -1,  -1, -1, -1, -1, //  3
        226, 159, 151, 164,  -1, -1, -1, -1, //  4
        185, 112,  94,  94, 103, -1, -1, -1, //  5
		158,  82,  62,  57,  59, 65, -1, -1, //  6
		136,  63,  43,  37,  36, 37, -1, -1, //  7
		121,  50,  31,  25,  22, 22, 23, -1, //  8
		108,  41,  23,  17,  14, 14, 14, 15, //  9
	 	 97,  34,  18,  12,  10,  9,  8,  9, // 10
		 89,  28,  14,   9,   7,  6,  5,  5, // 11
		 82,  24,  11,   7,   5,  4,  3,  3, // 12
		 76,  21,   9,   5,   3,  3,  2,  2, // 13
		 71,  18,   7,   4,   2,  2,  1,  1, // 14
		 66,  16,   6,   3,   2,  1,  1,  1, // 15
		 62,  14,   5,   2,   1,  1,  1,  1, // 16
		 58,  13,   4,   2,   1,  1,  1,  0, // 17
		 55,  11,   4,   2,   1,  1,  0,  0, // 18
		 53,  10,   3,   1,   1,  0,  0,  0  // 19
  };


  private int items;
  private int capacity;
  private int errorRate1024;
  private byte[] bitArray;
  private HashFunction[] hashes;
  private ArrayList<Integer> hashRadom;

  /**
   * ���ļ��м���bloomfilter������ʼ��
   * @param bfFile
   */
  public BloomFilter(File bfFile)
  {
	  if(!bfFile.exists())
		  throw new IllegalArgumentException("bloomFilter�ļ�������:"+bfFile.getName()+"��������");
	  
	  FileReader fr=null;
	  BufferedReader br=null;
	  try {
		fr=new FileReader(bfFile);
		br=new BufferedReader(fr);
		String line=null;
		int bitArrySize=Integer.parseInt(br.readLine().split(":")[1]);
		this.bitArray=new byte[bitArrySize];
		
		this.capacity=Integer.parseInt(br.readLine().split(":")[1]);
		this.items=Integer.parseInt(br.readLine().split(":")[1]);
		
		int k=Integer.parseInt(br.readLine().split(":")[1]);
		this.hashes=new HashFunction[k];
		this.hashRadom=new ArrayList<Integer>();
		
		this.errorRate1024=Integer.parseInt(br.readLine().split(":")[1]);
	
		line=br.readLine().split(":")[1];
		String temp[]=line.split("\\|");
		for (int i = 0; i < temp.length; i++)
		{
			this.hashes[i]=new MD5Hash(Integer.parseInt(temp[i]));
			this.hashRadom.add(Integer.parseInt(temp[i]));
		}
		br.readLine();
		int count=0;
		while ((line=br.readLine())!=null)
		{
			String temps[]=line.split("\\|");
			for (int i = 0; i < temps.length; i++) 
			{
				this.getBitArray()[count]=Byte.parseByte(temps[i]);
				count++;
			}
			
		}
		br.close();
		fr.close();
	} catch (Exception e) 
	{
		e.printStackTrace();
	}
  }
  
  public BloomFilter(int errorRate1024, int capacity)
  {
     this(errorRate1024, capacity, null);
  }


  public BloomFilter(int errorRate1024, int capacity, HashFunction[] hashes)
  {
     if ((errorRate1024 < 1) || (errorRate1024 >= 1024))
       throw new IllegalArgumentException("Error rate invalid, must be in [1..1023]");

     if ((hashes != null) && ((hashes.length < 2) || (hashes.length > 9)))
       throw new IllegalArgumentException("The number of hash functions is invalid, must be in [2..9]");

     int index = findIndex(errorRate1024, (hashes == null) ? 0 : hashes.length);

     if (index < 0)
       throw new IllegalArgumentException("Cannot find combination (m/n, k)");

     final int k = (index & 7) + 1;
     final int size = capacity * ((index >> 3) + 2);
     this.capacity = capacity;
     this.errorRate1024 = errorRate1024;
     this.bitArray = new byte[(size + 7) >> 3];
     this.hashes = new HashFunction[k];
     this.hashRadom=new ArrayList<Integer>();
     if (hashes != null)
     {
        System.arraycopy(hashes, 0, this.hashes, 0, k);
     }
     else
     {
        Random rnd = new Random();

        for (int i=0; i<k; i++)
        {
        	int random=rnd.nextInt();
        	this.hashes[i] = new MD5Hash(random);
        	this.hashRadom.add(random);
        }
     }
  }


  public boolean add(T item)
  {
    if (this.items >= this.capacity)
       return false;

    this.items++;
    final int sz = this.bitArray.length << 3;
    final byte[] value = getBytes(item);

    for (HashFunction hf : this.hashes)
    {
      final int key = (hf.hashCode(value) & 0x7FFFFFFF) % sz;
      final int mask = 1 << (7 - (key & 7));
      this.bitArray[key >> 3] |= mask;
    }

    return true;
  }


  public boolean contains(T item)
  {
    final int sz = this.bitArray.length << 3;
    final byte[] value = getBytes(item);

    for (HashFunction hf : this.hashes)
    {
      final int key = (hf.hashCode(value) & 0x7FFFFFFF) % sz;
      final int mask = 1 << (7 - (key & 7));

      if ((this.bitArray[key >> 3] & mask) == 0)
         return false;
    }

    return true;
  }

  public ArrayList<Integer> getHashRandom()
  {
	  return this.hashRadom;
  }
  
  public byte[] getBitArray()
  {
	  return this.bitArray;
  }
  
  // find index in error table, given error rate and number of hash functions
  // k in [2..9] (or 0 is not provided)
  public static int findIndex(int errorRate1024, int k)
  {
    if ((k != 0) && ((k < 2) || (k > 9)))
       return -1;

    final int min_k = k;
    final int max_k = (k == 0) ? 8 : k;

    // Favor speed over memory by scanning by column first
    for (int i=min_k; i<=max_k; i++)
    {
       for (int j=0; j<18; j++)
       {
          final int offs = j << 3;
          final int val = ERROR_RATE_1024[offs+i];

          if (val <= 0)
            continue;

          if (val <= errorRate1024)
             return offs+i;
       }
    }

    return -1;
  }


  public static byte[] getBytes(Object obj)
  {
    if (obj == null)
       return new byte[0];

    if (obj.getClass().equals(byte[].class))
       return (byte[]) obj;

    if (obj instanceof Serializable)
    {
       try
       {
          ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(obj);
          return baos.toByteArray();
       }
       catch (IOException e)
       {
          // Fall through
       }
    }

    return String.valueOf(obj).getBytes();
  }


  public int getCapacity()
  {
    return this.capacity;
  }


  public int getSize()
  {
    return this.items;
  }


  public int getHashes()
  {
    return this.hashes.length;
  }


  public int getErrorRate()
  {
    return this.errorRate1024;
  }


  @Override
  public String toString()
  {
     StringBuilder builder = new StringBuilder((this.capacity >> 3) + 200);
     builder.append("Capacity:          ");
     builder.append(this.getCapacity());
     builder.append("\n");
     builder.append("Size:              ");
     builder.append(this.getSize());
     builder.append("\n");
     builder.append("Hashes:            ");
     builder.append(this.getHashes());
     builder.append("\n");
     builder.append("Target error rate: ");
     builder.append(((this.getErrorRate() * 100) + 512) >> 10);
     builder.append("%\n");
     builder.append("Bit array:\n");

     for (int i=0; i<this.bitArray.length; i++)
     {
        int mask = 0x80;

        while (mask >= 1)
        {
          builder.append(((this.bitArray[i>>3] & mask) == 0) ? "0" : "1");
          mask >>= 1;
        }

        if ((i & 31) == 31)
           builder.append("\n");
     }

     builder.append("\n");
     return builder.toString();
  }
  /**
   * ��bloomfilter�������ļ���
   * @param bfFile
   */
  public void save(String bfFile)
  {
	
     StringBuilder builder = new StringBuilder(200);
     builder.append("BetyArray Size:");
     builder.append(this.getBitArray().length);
     builder.append("\n");
     builder.append("Capacity:");
     builder.append(this.getCapacity());
     builder.append("\n");
     builder.append("Size:");
     builder.append(this.getSize());
     builder.append("\n");
     builder.append("Hashes:");
     builder.append(this.getHashes());
     builder.append("\n");
     builder.append("ErrorRate1024:");
     builder.append(this.getErrorRate());
     builder.append("\n");
     builder.append("HashRadom:");
     for (int i = 0; i <this.getHashes(); i++) 
     {
		builder.append(this.getHashRandom().get(i)+"|");
     }
     builder.append("\n");
     builder.append("Byte Array:\n");
  
     FileWriter fw=null;
     BufferedWriter bw=null;
     try {
		
    	 File file=new File(bfFile);
    	 fw=new FileWriter(file);
    	 bw=new BufferedWriter(fw);
    	 bw.write(builder.toString());
    	 int count=1;
    	 for (int i = 0; i < this.getBitArray().length; i++) 
         {
    		 
        	 bw.write(String.valueOf(this.getBitArray()[i]));
        	 bw.write("|");
        	 count++;
        	 if (count>=256) 
        	 {
        		 bw.write("\n");
        		 bw.flush();
        		 count=0;
        	 }
         }
    	 bw.flush();
    	 bw.close();
    	 fw.close();
	} catch (Exception e) 
	{
		e.printStackTrace();
	}
  }
}