/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2020, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
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
package tech.units.indriya.quantity;

import static javax.measure.Quantity.Scale.ABSOLUTE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import javax.measure.Quantity;
import javax.measure.Quantity.Scale;
import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;

import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.format.AbstractQuantityFormat;
import tech.units.indriya.format.SimpleQuantityFormat;
import tech.units.indriya.function.MixedRadix;

/**
 * Singleton class for accessing {@link Quantity} instances.
 * 
 * @version 2.1, January 25, 2021
 * @author keilw
 * @author otaviojava
 * @since 1.0
 */
public final class Quantities {
	/**
	 * Private singleton constructor.
	 */
	private Quantities() {
	}

	/**
	 * Returns the {@link #valueOf(java.math.BigDecimal, javax.measure.unit.Unit)
	 * decimal} quantity of unknown type corresponding to the specified
	 * representation. This method can be used to parse dimensionless
	 * quantities.<br>
	 * <code>
	 *     Quantity<Dimensionless> proportion = Quantities.getQuantity("0.234").asType(Dimensionless.class);
	 * </code>
	 *
	 * <p>
	 * Note: This method handles only Locale-neutral quantity formatting and parsing
	 * are handled by the {@link AbstractQuantityFormat} class and its subclasses.
	 * </p>
	 *
	 * @param csq the decimal value and its unit (if any) separated by space(s).
	 * @return <code>QuantityFormat.getInstance(LOCALE_NEUTRAL).parse(csq)</code>
	 */
	public static Quantity<?> getQuantity(CharSequence csq) {
		try {
			return SimpleQuantityFormat.getInstance("n u~ ").parse(csq);
		} catch (MeasurementParseException e) {
			throw new IllegalArgumentException(e.getParsedString());
		}
	}

	/**
	 * Returns the scalar quantity. When the {@link Number} was {@link BigDecimal}
	 * or {@link BigInteger} will uses {@link DecimalQuantity}, when the
	 * {@link Number} was {@link Double} will {@link DoubleQuantity} otherwise will
	 * {@link NumberQuantity}. in the specified unit.
	 * 
	 * @param value the measurement value.
	 * @param unit  the measurement unit.
	 * @param scale the measurement scale.
	 * @return the corresponding <code>numeric</code> quantity.
	 * @throws NullPointerException if value, unit or scale were null
	 * @since 2.0
	 */
	public static <Q extends Quantity<Q>> ComparableQuantity<Q> getQuantity(Number value, Unit<Q> unit, Scale scale) {
		Objects.requireNonNull(value);
		Objects.requireNonNull(unit);
		Objects.requireNonNull(scale);
		return new NumberQuantity<>(value, unit, scale);
	}

	/**
	 * Returns the scalar quantity. When the {@link Number} was {@link BigDecimal}
	 * or {@link BigInteger} will uses {@link DecimalQuantity}, when the
	 * {@link Number} was {@link Double} will {@link DoubleQuantity} otherwise will
	 * {@link NumberQuantity}. in the specified unit.
	 * 
	 * @param value the measurement value.
	 * @param unit  the measurement unit.
	 * @return the corresponding <code>numeric</code> quantity.
	 * @throws NullPointerException when value or unit were null
	 */
	public static <Q extends Quantity<Q>> ComparableQuantity<Q> getQuantity(Number value, Unit<Q> unit) {
		return getQuantity(value, unit, ABSOLUTE);
	}

	/**
	 * Returns the mixed radix values and units combined into a single quantity.
	 * When the {@link Number} was {@link BigDecimal} or {@link BigInteger} will
	 * uses {@link DecimalQuantity}, when the {@link Number} was {@link Double} will
	 * {@link DoubleQuantity} otherwise will {@link NumberQuantity}. in the
	 * specified unit.
	 * 
	 * @param values the measurement values.
	 * @param units  the measurement units.
	 * @param scale  the measurement scale.
	 * @return the corresponding quantity.
	 * @throws NullPointerException     if value or scale were null
	 * @throws IllegalArgumentException if the size of the values array does not
	 *                                  match that of units.
	 * @since 2.0
	 */
	public static <Q extends Quantity<Q>> Quantity<Q> getQuantity(Number[] values, Unit<Q>[] units, Scale scale) {
		Objects.requireNonNull(values);
		Objects.requireNonNull(units);
		if (values.length == units.length) {
			return MixedRadix.of(units).createQuantity(values, scale);
		} else {
			throw new IllegalArgumentException(
					String.format("%s values don't match %s units", values.length, units.length));
		}
	}

	/**
	 * Returns the mixed radix values and units combined into a single quantity in
	 * the {@code ABSOLUTE} scale.
	 * 
	 * @param values the measurement values.
	 * @param units  the measurement units.
	 * @return the corresponding quantity.
	 * @throws NullPointerException     if value or unit were null
	 * @throws IllegalArgumentException if the size of the values array does not
	 *                                  match that of units.
	 * @since 2.0
	 */
	@SafeVarargs
	public static <Q extends Quantity<Q>> Quantity<Q> getQuantity(Number[] values, Unit<Q>... units) {
		return getQuantity(values, units, ABSOLUTE);
	}

	/**
	 * Returns the mixed radix values and units as {@link CompoundQuantity} in the
	 * specified scale.
	 * 
	 * @param values the measurement values.
	 * @param units  the measurement units.
	 * @param scale  the measurement scale.
	 * @return the corresponding mixed quantity.
	 * @throws NullPointerException     if values, units or scale were null
	 * @throws IllegalArgumentException if the size of the values array does not
	 *                                  match that of units.
	 * @since 2.1.2
	 */
	public static <Q extends Quantity<Q>> MixedQuantity<Q> getMixedQuantity(Number[] values, Unit<Q>[] units,
			Scale scale) {
		Objects.requireNonNull(values);
		Objects.requireNonNull(units);
		if (values.length == units.length) {
			return MixedRadix.of(units).createMixedQuantity(values, scale);
		} else {
			throw new IllegalArgumentException(
					String.format("%s values don't match %s units", values.length, units.length));
		}
	}

	/**
	 * Returns the mixed radix values and units as {@link CompoundQuantity} in the
	 * {@code ABSOLUTE} scale.
	 * 
	 * @param values the measurement values.
	 * @param units  the measurement units.
	 * @return the corresponding mixed quantity.
	 * @throws NullPointerException     if values, units or scale were null
	 * @throws IllegalArgumentException if the size of the values array does not
	 *                                  match that of units.
	 * @since 2.1.2
	 */
	public static <Q extends Quantity<Q>> MixedQuantity<Q> getMixedQuantity(final Number[] values,
			final Unit<Q>[] units) {
		return getMixedQuantity(values, units, ABSOLUTE);
	}
	
	/**
	 * Returns the mixed radix values and units as {@link CompoundQuantity} in the
	 * specified scale.
	 * 
	 * @param values the measurement values.
	 * @param units  the measurement units.
	 * @param scale  the measurement scale.
	 * @return the corresponding compound quantity.
	 * @throws NullPointerException     if values, units or scale were null
	 * @throws IllegalArgumentException if the size of the values array does not
	 *                                  match that of units.
	 * @since 2.0
	 * @deprecated use {@link #getMixedQuantity(Number[], Unit[], Scale)}
	 */
	public static <Q extends Quantity<Q>> CompoundQuantity<Q> getCompoundQuantity(Number[] values, Unit<Q>[] units,
			Scale scale) {
		Objects.requireNonNull(values);
		Objects.requireNonNull(units);
		if (values.length == units.length) {
			return MixedRadix.of(units).createCompoundQuantity(values, scale);
		} else {
			throw new IllegalArgumentException(
					String.format("%s values don't match %s units", values.length, units.length));
		}
	}

	/**
	 * Returns the mixed radix values and units as {@link CompoundQuantity} in the
	 * {@code ABSOLUTE} scale.
	 * 
	 * @param values the measurement values.
	 * @param units  the measurement units.
	 * @return the corresponding compound quantity.
	 * @throws NullPointerException     if values, units or scale were null
	 * @throws IllegalArgumentException if the size of the values array does not
	 *                                  match that of units.
	 * @since 2.0
	 * @deprecated use {@link #getMixedQuantity(Number[], Unit[])}
	 */
	public static <Q extends Quantity<Q>> CompoundQuantity<Q> getCompoundQuantity(final Number[] values,
			final Unit<Q>[] units) {
		return getCompoundQuantity(values, units, ABSOLUTE);
	}
}
