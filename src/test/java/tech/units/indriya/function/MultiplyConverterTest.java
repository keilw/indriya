/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2019, Units of Measurement project.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-385, Indriya nor the names of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tech.units.indriya.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MultiplyConverterTest {

  private MultiplyConverter converter;

  @BeforeEach
  public void setUp() throws Exception {
    converter = new MultiplyConverter(2);
  }

  @Test
  public void testConvertMethod() {
    assertEquals(200, converter.convert(100), 0.1);
    assertEquals(0, converter.convert(0));
    assertEquals(-200, converter.convert(-100), 0.1);
  }

  @Test
  public void testEqualityOfTwoLogConverter() {
    assertFalse(converter.equals(null));
    assertEquals(new MultiplyConverter(2), converter);
  }

  @Test
  public void testGetValuePiDivisorConverter() {
    assertEquals(Double.valueOf(2d), converter.getValue());
  }

  @Test
  public void isLinearOfLogConverterTest() {
    assertTrue(converter.isLinear());
  }

  @Test
  public void inverseTest() {
    assertNotNull(converter.inverse());
    assertEquals(new MultiplyConverter(0.5), converter.inverse());
  }

  @Test
  public void identityTest() {
	  assertTrue(new MultiplyConverter(1).isIdentity());
  }

  @Test
  public void valueTest() {
    assertEquals(Double.valueOf(2), converter.getValue());
  }

  @Test
  public void toStringTest() {
    assertEquals("Multiply(x -> x * 2.0)", converter.toString());
  }
}
