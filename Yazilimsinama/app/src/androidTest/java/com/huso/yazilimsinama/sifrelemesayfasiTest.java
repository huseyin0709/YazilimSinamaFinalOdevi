package com.huso.yazilimsinama;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class sifrelemesayfasiTest {


    @Rule
    public ActivityTestRule<sifrelemesayfasi> sifrelemesayfasiActivityTestRule=new ActivityTestRule<sifrelemesayfasi>(com.huso.yazilimsinama.sifrelemesayfasi.class);
    private sifrelemesayfasi sifrelemesayfasi=null;


    @Before
    public void setUp() throws Exception {
        sifrelemesayfasi=sifrelemesayfasiActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view=sifrelemesayfasi.findViewById(R.id.sonuc_textView);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        sifrelemesayfasi=null;
    }
}