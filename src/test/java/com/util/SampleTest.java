package com.util;


import org.junit.Assert;
import org.junit.Test;

import com.util.SampleExample; 

public class SampleTest { 

   @Test

   public void test() { 

	  SampleExample example = new SampleExample();

      example.addInteger(10);

      example.addInteger(100);
      
     // example.addInteger(100);

      Assert.assertEquals(example.getSize(), 2);

   }

}