/*
 * Copyright 2009 IIZUKA Software Technologies Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jtype;

import static com.googlecode.jtype.test.SerializableAssert.assertSerializable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@code Generic}.
 * 
 * @author Mark Hobson
 * @version $Id: GenericTest.java 115 2011-11-25 18:17:40Z markhobson@gmail.com $
 * @see Generic
 */
public class GenericTest implements Serializable
{
	// test is serializable with transient fields for serializableWhenSubclassed
	
	// classes ----------------------------------------------------------------
	
	private static class GenericSubclass<T> extends Generic<T>
	{
		// simple subclass
	}
	
	// fields -----------------------------------------------------------------
	
	private transient GenericDeclaration declaration;
	
	// public methods ---------------------------------------------------------
	
	@Before
	public void setUp() throws NoSuchMethodException
	{
		declaration = getClass().getConstructor();
	}
	
	// test methods -----------------------------------------------------------
	
	@Test(expected = IllegalStateException.class)
	public void constructorWhenSubclassedTwice()
	{
		try
		{
			new GenericSubclass<List<String>>() {/**/};
		}
		catch (IllegalStateException exception)
		{
			assertEquals("Message", "Generic must only be subclassed once", exception.getMessage());
			
			throw exception;
		}
	}
	
	@Test
	public void constructorWhenSubclassedWithParameterType()
	{
		assertEquals(Types.parameterizedType(List.class, String.class), new Generic<List<String>>() {/**/}.getType());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public <T> void constructorWhenSubclassedWithTypeVariable()
	{
		try
		{
			new Generic<T>() {/**/};
		}
		catch (IllegalArgumentException exception)
		{
			assertEquals("Message", "Type variables are not supported: T", exception.getMessage());
			
			throw exception;
		}
	}
	
	@Test
	public void constructorWhenSubclassedWithGenericArrayType()
	{
		assertEquals(Types.genericArrayType(Types.parameterizedType(List.class, String.class)),
			new Generic<List<String>[]>() {/**/}.getType());
	}
	
	@Test
	public void getWithClass()
	{
		assertEquals(String.class, Generic.get(String.class).getType());
	}
	
	@Test
	public void getWithClassIsSerializable() throws IOException, ClassNotFoundException
	{
		assertSerializable(Generic.get(String.class));
	}
	
	@Test
	public void getWithRawTypeAndActualTypeArguments()
	{
		ParameterizedType parameterizedType = Types.parameterizedType(List.class, String.class);
		
		assertEquals(parameterizedType, Generic.get(List.class, String.class).getType());
	}
	
	@Test
	public void getWithRawTypeAndNullActualTypeArguments()
	{
		assertEquals(String.class, Generic.get(String.class, (Type[]) null).getType());
	}
	
	@Test
	public void getWithRawTypeAndEmptyActualTypeArguments()
	{
		assertEquals(String.class, Generic.get(String.class, new Type[0]).getType());
	}
	
	@Test
	public void getWithParameterizedType()
	{
		ParameterizedType parameterizedType = Types.parameterizedType(List.class, String.class);
		
		assertEquals(parameterizedType, Generic.get(parameterizedType).getType());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getWithWildcardType()
	{
		try
		{
			Generic.get(Types.unboundedWildcardType());
		}
		catch (IllegalArgumentException exception)
		{
			assertEquals("Message", "Wildcard types are not supported: ?", exception.getMessage());
			
			throw exception;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getWithTypeVariable()
	{
		try
		{
			Generic.get(Types.typeVariable(declaration, "T"));
		}
		catch (IllegalArgumentException exception)
		{
			assertEquals("Message", "Type variables are not supported: T", exception.getMessage());
			
			throw exception;
		}
	}
	
	@Test
	public void getWithGenericArrayType()
	{
		GenericArrayType genericArrayType = Types.genericArrayType(Types.parameterizedType(List.class, String.class));
		
		assertEquals(genericArrayType, Generic.get(genericArrayType).getType());
	}
	
	@Test
	public void getWithObjectClassIsCached()
	{
		assertSame(Generic.get(Object.class), Generic.get(Object.class));
	}
	
	@Test
	public void getWithBooleanClassIsCached()
	{
		assertSame(Generic.get(Boolean.class), Generic.get(Boolean.class));
	}
	
	@Test
	public void getWithByteClassIsCached()
	{
		assertSame(Generic.get(Byte.class), Generic.get(Byte.class));
	}
	
	@Test
	public void getWithCharacterClassIsCached()
	{
		assertSame(Generic.get(Character.class), Generic.get(Character.class));
	}
	
	@Test
	public void getWithDoubleClassIsCached()
	{
		assertSame(Generic.get(Double.class), Generic.get(Double.class));
	}
	
	@Test
	public void getWithFloatClassIsCached()
	{
		assertSame(Generic.get(Float.class), Generic.get(Float.class));
	}
	
	@Test
	public void getWithIntegerClassIsCached()
	{
		assertSame(Generic.get(Integer.class), Generic.get(Integer.class));
	}
	
	@Test
	public void getWithLongClassIsCached()
	{
		assertSame(Generic.get(Long.class), Generic.get(Long.class));
	}
	
	@Test
	public void getWithShortClassIsCached()
	{
		assertSame(Generic.get(Short.class), Generic.get(Short.class));
	}
	
	@Test
	public void getWithStringClassIsCached()
	{
		assertSame(Generic.get(String.class), Generic.get(String.class));
	}
	
	@Test
	public void valueOfWithClass()
	{
		assertEquals(String.class, Generic.valueOf("java.lang.String").getType());
	}
	
	@Test
	public void valueOfWithSingleParameterizedType()
	{
		assertEquals(Types.parameterizedType(List.class, String.class),
			Generic.valueOf("java.util.List<java.lang.String>").getType());
	}
	
	@Test
	public void valueOfWithDoubleParameterizedType()
	{
		assertEquals(Types.parameterizedType(Map.class, String.class, Integer.class),
			Generic.valueOf("java.util.Map<java.lang.String,java.lang.Integer>").getType());
	}
	
	@Test
	public void valueOfWithUnboundedWildcardType()
	{
		assertEquals(Types.parameterizedType(List.class, Types.unboundedWildcardType()),
			Generic.valueOf("java.util.List<?>").getType());
	}
	
	@Test
	public void hashCodeWithClass()
	{
		Generic<String> generic1 = Generic.get(String.class);
		Generic<String> generic2 = Generic.get(String.class);
		
		assertEquals(generic1.hashCode(), generic2.hashCode());
	}
	
	@Test
	public void hashCodeWithType()
	{
		Generic<List<String>> generic1 = new Generic<List<String>>() {/**/};
		Generic<List<String>> generic2 = new Generic<List<String>>() {/**/};
		
		assertEquals(generic1.hashCode(), generic2.hashCode());
	}
	
	@Test
	public void equalsWithClassWhenEqual()
	{
		Generic<String> generic1 = Generic.get(String.class);
		Generic<String> generic2 = Generic.get(String.class);
		
		assertTrue(generic1.equals(generic2));
	}
	
	@Test
	public void equalsWithClassWhenUnequal()
	{
		Generic<String> generic1 = Generic.get(String.class);
		Generic<Integer> generic2 = Generic.get(Integer.class);
		
		assertFalse(generic1.equals(generic2));
	}
	
	@Test
	public void equalsWithGenericWhenEqual()
	{
		Generic<List<String>> generic1 = new Generic<List<String>>() {/**/};
		Generic<List<String>> generic2 = new Generic<List<String>>() {/**/};
		
		assertTrue(generic1.equals(generic2));
	}
	
	@Test
	public void equalsWithGenericWhenUnequal()
	{
		Generic<List<String>> generic1 = new Generic<List<String>>() {/**/};
		Generic<List<Integer>> generic2 = new Generic<List<Integer>>() {/**/};
		
		assertFalse(generic1.equals(generic2));
	}
	
	@Test
	public void equalsWithDifferentClass()
	{
		Generic<String> generic = Generic.get(String.class);
		
		assertFalse(generic.equals("not a generic"));
	}
	
	@Test
	public void toStringWithClass()
	{
		assertEquals("java.lang.String", Generic.get(String.class).toString());
	}
	
	@Test
	public void toStringWithType()
	{
		assertEquals("java.util.List<java.lang.String>", new Generic<List<String>>() {/**/}.toString());
	}
	
	@Test
	public void toUnqualifiedStringWithClass()
	{
		assertEquals("String", Generic.get(String.class).toUnqualifiedString());
	}
	
	@Test
	public void toUnqualifiedStringWithType()
	{
		assertEquals("List<String>", new Generic<List<String>>() {/**/}.toUnqualifiedString());
	}

	@Test
	public void serializableWhenSubclassed() throws IOException, ClassNotFoundException
	{
		assertSerializable(new Generic<String>() {/**/});
	}
}
