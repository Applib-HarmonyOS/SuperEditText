/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scut.carsonho.superedittext;

import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.utils.Color;
import org.junit.Test;
import scut.carsonho.diyview.SuperEditText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class ExampleOhosTest {

    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("scut.carsonho.superedittext", actualBundleName);
    }

    @Test
    public void testChangeParamToColor() {
        Color hmosColor =SuperEditText.changeParamToColor(Color.RED.getValue());
        assertEquals(Color.RED,hmosColor);
    }

    @Test
    public void testChangeParamToColor1() {
        Color hmosColor = SuperEditText.changeParamToColor(Color.RED.getValue());
        assertNotEquals(Color.BLUE,hmosColor);
    }

    @Test
    public void testChangeParamToString() {
        String param = SuperEditText.changeParamToString("charSequence");
        assertEquals("charSequence",param);
    }

    @Test
    public void testChangeParamToString1() {
        String param = SuperEditText.changeParamToString("charSequence1");
        assertNotEquals("char_Sequence",param);
    }
}