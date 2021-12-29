package com.huso.yazilimsinama;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class messagegoruntulemesayfasiTest {

    @Rule
    public ActivityTestRule<messagegoruntulemesayfasi> messagegoruntulemesayfasiActivityTestRule=new ActivityTestRule<messagegoruntulemesayfasi>(messagegoruntulemesayfasi.class);
    private messagegoruntulemesayfasi messagegoruntulemesayfasi=null;


    @Before
    public void setUp() throws Exception {
        messagegoruntulemesayfasi=messagegoruntulemesayfasiActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view=messagegoruntulemesayfasi.findViewById(R.id.messaglarigoruntulu_textView);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        messagegoruntulemesayfasi=null;
    }
}