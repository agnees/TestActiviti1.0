package com.Final.Test;

import com.Final.model.PageBean;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by tb on 2016/10/30.
 */
public class MyTest {
    @Test
    public void testInteger(){
        String str = "hello";
        String[] str1 = str.split(",");
        Assert.assertNotNull(str1);
    }
}
