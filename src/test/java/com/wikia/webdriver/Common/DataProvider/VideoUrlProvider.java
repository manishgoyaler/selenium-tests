package com.wikia.webdriver.Common.DataProvider;

import org.testng.annotations.DataProvider;

/**
 * @author Karol 'kkarolk' Kujawiak
 *
 */
public class VideoUrlProvider {

	@DataProvider
	private static final Object[][] videoUrl() {
		return new Object[][] {
			// Premium Provider Links
			{
				//ooyala
				"http://video.wikia.com/wiki/File:Mainpages_101_-_How_to_make_a_great_mainpage_for_your_wiki_10-19-12_3.05_PM.mp4",
				"Mainpages 101 - How to make a great mainpage for your wiki 10-19-12 3.05 PM.mp4"
			}, {
				//ign
				"http://video.wikia.com/wiki/File:Call_of_Duty_Black_Ops_2_Walkthrough_-_All_Cordis_Die_Intel_Locations",
				"Call of Duty Black Ops 2 Walkthrough - All Cordis Die Intel Locations"
			}, {
				//anyclip
				"http://video.wikia.com/wiki/File:Batman_-_Following",
				"Batman - Following"
			}, {
				//screenplay
				"http://video.wikia.com/wiki/File:Greek_Chapter_Four_(2010)_-_Clip_What_Would_You_Do%3F",
				"Greek Chapter Four (2010) - Clip What Would You Do?"
			}, {
				//real gravity
				"http://video.wikia.com/wiki/File:Psychedelic_70s_Makeup",
				"Psychedelic 70s Makeup"
			},
			// Non-Premium Provider Links
			{
				"http://blip.tv/q-tv/s-gus-unger-hamilton-and-gwil-sainsbury-in-studio-q-6587602",
				"Q with Jian Ghomeshi - ∆'s Gus Unger-Hamilton and Gwil Sainsbury in Studio Q"
			}, {
				"http://www.dailymotion.com/video/x101vdw_robert-pattison-y-kristen-stewart-se-dan-un-tiempo_people#.UZovsrWnqXw",
				"Robert Pattison y Kristen Stewart se dan un tiempo"
			}, {
				"http://www.gamestar.de/videos/action,9/batman-arkham-origins,70131.html",
				"Batman Arkham Origins - Erster Teaser-Trailer Batman vs. Deathstroke"
			}, {
				"http://www.hulu.com/watch/489169",
				"The Unnatural (Bob's Burgers)"
			}, {
				"http://www.metacafe.com/watch/10534054/fast_furious_6_favourite_stunts/",
				"Fast Furious 6 Favourite Stunts"
			}, {
				"http://www.myvideo.de/watch/9112478/Snowblind",
				"Snowblind"
			}, {
				"http://vimeo.com/channels/staffpicks/58024671",
				"Melt Yourself Down - Fix My Life"
			}, {
				"http://www.youtube.com/watch?v=d9r5_DDMMjY",
				"Top 5 Plays of the Night May 19th"
			}, {
				"http://www.viddler.com/v/f97fc5b0",
				"Times-Shamrock Communications Spotlight"
			},
		};
	}

}