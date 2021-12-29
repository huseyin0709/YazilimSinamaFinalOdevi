package com.huso.yazilimsinama;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class dosyagondersayfasiTest {

    @Rule
    public ActivityTestRule<dosyagondersayfasi> dosyagondersayfasiActivityTestRule=new ActivityTestRule<dosyagondersayfasi>(dosyagondersayfasi.class);
    private dosyagondersayfasi dosyagondersayfasi=null;


    @Before
    public void setUp() throws Exception {
        dosyagondersayfasi=dosyagondersayfasiActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view=dosyagondersayfasi.findViewById(R.id.resimdosyasi_recyclerView);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        dosyagondersayfasi=null;
    }
}