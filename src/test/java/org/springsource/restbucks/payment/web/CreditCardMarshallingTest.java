/*
 * Copyright 2012-2014 the original author or authors.
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
package org.springsource.restbucks.payment.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springsource.restbucks.payment.CreditCard;
import org.springsource.restbucks.payment.CreditCardNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.jayway.jsonpath.JsonPath;

/**
 * Integration tests for marshalling of {@link CreditCard}.
 * 
 * @author Oliver Gierke
 */
public class CreditCardMarshallingTest {

	static final String REFERENCE = "{\"number\":\"1234123412341234\",\"cardHolderName\":\"Oliver Gierke\",\"expirationDate\":[2013,11,1]}";

	ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() {
		mapper.registerModule(new JSR310Module());
	}

	@Test
	public void serializesCreditCardWithOutIdAndWithAppropriateMontshAndYears() throws Exception {

		CreditCard creditCard = new CreditCard(new CreditCardNumber("1234123412341234"), "Oliver Gierke", Month.NOVEMBER,
				Year.of(2013));

		String result = mapper.writeValueAsString(creditCard);

		assertThat(JsonPath.<String> read(result, "$number"), is("1234123412341234"));
		assertThat(JsonPath.<String> read(result, "$cardHolderName"), is("Oliver Gierke"));
		assertThat(JsonPath.<Integer> read(result, "$expirationDate[0]"), is(2013));
		assertThat(JsonPath.<Integer> read(result, "$expirationDate[1]"), is(11));
		assertThat(JsonPath.<Integer> read(result, "$expirationDate[2]"), is(1));
	}

	@Test
	@Ignore
	public void deserializesCreditCardWithOutIdAndWithAppropriateMontshAndYears() throws Exception {

		CreditCard creditCard = mapper.readValue(REFERENCE, CreditCard.class);

		assertThat(creditCard.getId(), is(nullValue()));
		assertThat(creditCard.getExpirationDate(), is(LocalDate.of(2013, 11, 1)));
		assertThat(creditCard.getNumber(), is(notNullValue()));
	}
}
