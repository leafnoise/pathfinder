package com.leafnoise.pathfinder.bbs;

import javax.inject.Inject;
import javax.inject.Named;

import com.leafnoise.pathfinder.service.EventService;

@Named("mainBB")
public class MainBB {

	@Inject 
	@Named("mongoEventService")
	private EventService es;
	
}
