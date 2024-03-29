package com.regexbuilder.sample;

import java.util.Arrays;
import java.util.List;

import com.regexbuilder.Group;
import com.regexbuilder.RegexBuilder;
import com.regexbuilder.RegexFactory;
import com.regexbuilder.ClassMatch.CharacterClass;
import com.regexbuilder.Group.TreeType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeSamples {

	
	public static RegexBuilder fullWrittenDate_en() {
		
		Group monthGroup = RegexFactory.alternativeGroup().setName("month");
		
		List<String> monthList = Arrays.asList("January", "February", "March", "April", "May", "June", "Jul", "August", "September", "October", "November", "December");
		for(String month : monthList) {
			monthGroup.unique(
				RegexFactory.sequenceGroup()
					.unique(month.substring(0, 3))
					.optional(RegexFactory.alternativeGroup().unique(".").unique(month.substring(3)))
			);
		}
		
		Group dayGroup = RegexFactory.alternativeGroup().setName("day");
		
		dayGroup
			.unique("1st")
			.unique("2nd")
			.unique("3rd")
			.unique("21st")
			.unique("22nd")
			.unique("23rd");
		
		for(int i = 0; i <= 31; i++) {
			if(i >= 21 && i <= 23) {
				continue;
			}
			dayGroup.unique(i+"th");	
		}
		
		
		
		RegexBuilder regexBuilder = RegexFactory.regexBuilder();
		regexBuilder.unique(monthGroup);
		regexBuilder.unique(CharacterClass.Space);
		regexBuilder.unique(dayGroup);
		regexBuilder.unique(CharacterClass.Space);
		regexBuilder.unique(numericYear().setName("year"));
		
		
		return regexBuilder;
	}
	

	
	public static RegexBuilder fullWrittenDate_fr() {
		RegexBuilder regexBuilder = RegexFactory.regexBuilder();

		regexBuilder
			.unique(numericDay().setName("Day"))
			.unique(CharacterClass.Space)
			.unique(RegexFactory.alternativeGroup(Arrays.asList("janvier", "février", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "décembre")).setName("month"))
			.some(CharacterClass.Space)
			.optional(numericYear().setName("year"));

		return regexBuilder;
	}
	
	public static RegexBuilder numericDay() {
		
		RegexBuilder regexBuilder = RegexFactory.regexBuilder(TreeType.Alternative);

		regexBuilder
		
			// case 1 to 9 or 01 to 09
			.unique(RegexFactory.sequenceGroup()
					.optional("0")
					.unique(RegexFactory.classMatchRange('1', '9'))
					)
			// case 10 to 29
			.unique(RegexFactory.sequenceGroup()
					.unique(RegexFactory.classMatch('1', '2'))
					.unique(RegexFactory.classMatch(CharacterClass.Numeric))
					)
			// case 30
			.unique("30")
			// case 31
			.unique("31");
		
		return regexBuilder;
		
	}
	
	public static RegexBuilder numericMonth() {
		
		RegexBuilder regexBuilder = RegexFactory.regexBuilder(TreeType.Alternative);
		
		regexBuilder
			// case 1 to 9, 01 to 09
			.unique(RegexFactory.sequenceGroup()
				.optional("0")
				.unique(RegexFactory.classMatchRange('1', '9'))
				)
			.unique("10")
			.unique("11")
			.unique("12");
		
		return regexBuilder;
	}

	public static RegexBuilder numericYear() {
		RegexBuilder regexBuilder = RegexFactory.regexBuilder();

		regexBuilder
			.unique(RegexFactory.classMatch('1', '2'))
			.exactly(CharacterClass.Numeric, 3);

		return regexBuilder;

	}

	public static RegexBuilder clockHHMM() {
		RegexBuilder regexBuilder = RegexFactory.regexBuilder();
		regexBuilder
			// Hours
			.unique(
					RegexFactory.alternativeGroup()
					.unique(RegexFactory.sequenceGroup()
						.unique(RegexFactory.classMatch('0','1'))
						.unique(CharacterClass.Numeric))
					.unique(RegexFactory.sequenceGroup()
						.unique("2")
						.unique(RegexFactory.classMatchRange('0', '3'))
						)
				)
			// Separator
			.unique(":")
			// Minutes
			.unique(RegexFactory.classMatchRange('0', '5'))
			.unique(CharacterClass.Numeric);
		
		return regexBuilder;
	}

	public static RegexBuilder regularDate() {
		RegexBuilder regex = RegexFactory.regexBuilder();

		regex
			.unique(numericDay().setName("day"))
			.unique("/")
			.unique(numericMonth().setName("month"))
			.unique("/")
			.unique(numericYear().setName("year"));

		return regex;

	}

	public static RegexBuilder timestampRegex() {

		RegexBuilder rb = RegexFactory.regexBuilder();
			rb
				.unique(RegexFactory.sequenceGroup().setName("year").between(CharacterClass.Numeric, 4, 4)) // Year
				.unique("-")
				.unique(RegexFactory.sequenceGroup().setName("month").between(CharacterClass.Numeric, 2, 2)) // Month
				.unique("-")
				.unique(RegexFactory.sequenceGroup().setName("day").between(CharacterClass.Numeric, 2, 2)) // Day
				.unique("T")
				.unique(RegexFactory.sequenceGroup().setName("hour").between(CharacterClass.Numeric, 2, 2)) // Hour
				.unique(":")
				.unique(RegexFactory.sequenceGroup().setName("minute").between(CharacterClass.Numeric, 2, 2)) // Minute
				.unique(":")
				.unique(RegexFactory.sequenceGroup().setName("second").between(CharacterClass.Numeric, 2, 2)) // Second
				.unique(".")
				.unique(RegexFactory.sequenceGroup().setName("millisecond").any(CharacterClass.Numeric)) // Millisecond
				.unique("Z");

		return rb;

	}

}
