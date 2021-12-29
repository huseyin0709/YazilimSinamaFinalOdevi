package com.huso.yazilimsinama;

import static org.junit.Assert.*;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class messagegondersayfasiTest {
    @Rule
    public ActivityTestRule<messagegondersayfasi> messagegondersayfasiActivityTestRule=new ActivityTestRule<messagegondersayfasi>(messagegondersayfasi.class);
    private messagegondersayfasi messagegondersayfasi=null;

    @Before
    public void setUp() throws Exception {
        messagegondersayfasi=messagegondersayfasiActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view=messagegondersayfasi.findViewById(R.id.message_recyclerview);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        messagegondersayfasi=null;

    }
}