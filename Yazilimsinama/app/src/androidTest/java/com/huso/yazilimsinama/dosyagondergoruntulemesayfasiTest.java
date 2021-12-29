package com.huso.yazilimsinama;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class dosyagondergoruntulemesayfasiTest {

    @Rule
    public ActivityTestRule<dosyagondergoruntulemesayfasi> dosyagondergoruntulemesayfasiActivityTestRule=new ActivityTestRule<dosyagondergoruntulemesayfasi>(dosyagondergoruntulemesayfasi.class);
    private dosyagondergoruntulemesayfasi dosyagondergoruntulemesayfasi=null;


    @Before
    public void setUp() throws Exception {
        dosyagondergoruntulemesayfasi=dosyagondergoruntulemesayfasiActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view=dosyagondergoruntulemesayfasi.findViewById(R.id.gonderilendosya_imageView);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        dosyagondergoruntulemesayfasi=null;
    }
}