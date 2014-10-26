package com.wangyin.ak47.common;



/**
 * For fuzzing-test
 * 
 * @author wyhubingyin
 * 
 */
public class SimpleFuzz {
//	private static final Logger log = new Logger(FuzzUtil.class);
	
    private int seed;
    
    public SimpleFuzz(int seed){
        this.seed = seed;
    }
    
	/**
	 * Simple fuzzing, NOT support seed.
	 * 
	 * @param src
	 * @param permillage   0~1000
	 * @return
	 */
	public byte[] fuzz(byte[] src, int permillage){
	    if( null == src ){
	        throw new IllegalArgumentException("Src should NOT be null.");
	    }
	    if( permillage > 1000 || permillage < 0){
	        throw new IllegalArgumentException("Permillage must between 0 and 1000.");
	    }
        
	    int srclen = src.length;
	    byte[] dst = ByteUtil.copyOf(src, srclen);
	    
	    int fuzzNum = srclen * permillage / 1000;
	    
	    for(int i=0; i<fuzzNum; i++){
	        int pos = RandomUtil.nextInt(srclen);
	        dst[pos] = fuzz(dst[pos]);
	    }
	    
	    return dst;
	}
	
	public byte fuzz(byte src){
	    return (byte) (16127L * src + RandomUtil.nextInt(223) + seed);
	}
	
	public int fuzz(int src){
	    return (int) (16127L * src + RandomUtil.nextInt(223) + seed);
	}
	
	public long fuzz(long src){
	    return  16127L * src + RandomUtil.nextInt(223) + seed;
	}
	
	
}
